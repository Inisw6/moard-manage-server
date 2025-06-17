package com.inisw.moard.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inisw.moard.api.predict.PredictService;
import com.inisw.moard.api.predict.dto.PredictTopContentsRequest;
import com.inisw.moard.api.predict.dto.PredictTopContentsResponse;
import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentService;
import com.inisw.moard.content.ContentType;
import com.inisw.moard.recommendation.RecommendationResponseDto.RankedContent;
import com.inisw.moard.recommendation.content.RecommendationContent;
import com.inisw.moard.recommendation.content.RecommendationContentRepository;
import com.inisw.moard.user.User;
import com.inisw.moard.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {
	private final RecommendationRepository recommendationRepository;
	private final ContentService contentService;
	private final UserRepository userRepository;
	private final RecommendationContentRepository recommendationContentRepository;
	private final PredictService predictService;

	public List<Recommendation> getRecommendationList() {
		return recommendationRepository.findAll();
	}

	public RecommendationResponseDto getRecommendations(String query, Integer limit, UUID userId) {
		// User 조회
		User user = userRepository.findByUuid(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + userId));

		List<Content> contentList = contentService.readContentsByQuery(query, limit * 3);
		List<Content> recommendedContentList = new ArrayList<>();
		List<Double> userEmbedding = null;

		String modelName = null;

		// 사용자의 로그가 100개 미만이거나, 추천 관련 로그가 없거나, 모든 로그의 Recommendation이 null인 경우
		if (user.getUserLogList().size() < 100 ||
			user.getUserLogList().isEmpty() ||
			user.getUserLogList().stream().allMatch(log -> log.getRecommendation() == null)) {
			modelName = "random";
			Map<ContentType, List<Content>> contentByType = contentList.stream()
				.collect(Collectors.groupingBy(Content::getType));

			// 각 타입별로 2개씩 랜덤하게 선택
			for (ContentType type : ContentType.values()) {
				List<Content> typeContents = contentByType.getOrDefault(type, new ArrayList<>());
				if (!typeContents.isEmpty()) {
					Collections.shuffle(typeContents);
					recommendedContentList.addAll(typeContents.subList(0, Math.min(2, typeContents.size())));
				}
			}
		} else {
			List<Long> contentIds = contentList.stream()
				.map(Content::getId)
				.collect(Collectors.toList());

			PredictTopContentsRequest predictTopContentsRequest = new PredictTopContentsRequest(userId, contentIds);

			PredictTopContentsResponse predictTopContentsResponse = predictService.predictTopContents(
				predictTopContentsRequest);

			modelName = predictTopContentsResponse.model_name();
			userEmbedding = predictTopContentsResponse.user_embedding();

			// 1) 예측 결과로부터 ID 리스트 꺼내기
			List<Long> recommendedIds = predictTopContentsResponse.content_ids();

			// 2) 원래 Content 리스트를 ID → Content 맵으로 변환
			Map<Long, Content> contentMap = contentList.stream()
				.collect(Collectors.toMap(Content::getId, Function.identity()));

			// 3) 맵에서 ID 순서대로 꺼내서 recommendedContentList에 추가
			recommendedContentList = recommendedIds.stream()
				.map(contentMap::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		}

		// Recommendation 엔티티 생성 및 저장
		Recommendation recommendation = Recommendation.builder()
			.query(query)
			.modelVersion(modelName)
			.user(user)
			.embedding(userEmbedding)
			.build();
		recommendation = recommendationRepository.save(recommendation);

		// RecommendationContent 엔티티 생성 및 저장
		List<RecommendationContent> recommendationContents = new ArrayList<>();
		for (int i = 0; i < recommendedContentList.size(); i++) {
			Content content = recommendedContentList.get(i);
			RecommendationContent recommendationContent = RecommendationContent.builder()
				.recommendation(recommendation)
				.content(content)
				.ranks(i + 1)
				.build();
			recommendationContent = recommendationContentRepository.save(recommendationContent);
			recommendationContents.add(recommendationContent);
		}

		recommendation.setRecommendationContentList(recommendationContents);
		recommendation = recommendationRepository.save(recommendation);

		// rank 정보를 포함한 콘텐츠 리스트 생성
		List<RankedContent> rankedContents = recommendationContents.stream()
			.map(rc -> RankedContent.builder()
				.content(rc.getContent())
				.rank(rc.getRanks())
				.build())
			.collect(Collectors.toList());

		// RecommendationResponseDto 생성
		return RecommendationResponseDto.builder()
			.id(recommendation.getId())
			.query(recommendation.getQuery())
			.modelVersion(recommendation.getModelVersion())
			.recommendedAt(recommendation.getRecommendedAt())
			.contents(rankedContents)
			.build();
	}
}
