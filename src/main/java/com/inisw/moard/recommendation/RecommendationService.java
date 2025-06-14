package com.inisw.moard.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public List<Recommendation> getRecommendationList() {
		return recommendationRepository.findAll();
	}

	@Transactional
	public RecommendationResponseDto getRecommendations(String query, Integer limit, UUID userId) {
		List<Content> contentList = contentService.readContentsByQuery(query, limit * 3);

		// 추후 추천 로직 적용 부분
		Map<ContentType, List<Content>> contentByType = contentList.stream()
			.collect(Collectors.groupingBy(Content::getType));

		List<Content> recommendedContentList = new ArrayList<>();

		// 각 타입별로 2개씩 랜덤하게 선택
		for (ContentType type : ContentType.values()) {
			List<Content> typeContents = contentByType.getOrDefault(type, new ArrayList<>());
			if (!typeContents.isEmpty()) {
				Collections.shuffle(typeContents);
				recommendedContentList.addAll(typeContents.subList(0, Math.min(2, typeContents.size())));
			}
		}

		// User 조회
		User user = userRepository.findByUuid(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + userId));

		// Recommendation 엔티티 생성 및 저장
		Recommendation recommendation = Recommendation.builder()
			.query(query)
			.modelVersion("v1.0")
			.user(user)
			.build();
		recommendation = recommendationRepository.save(recommendation);

		// RecommendationContent 엔티티 생성 및 저장
		List<RecommendationContent> recommendationContents = new ArrayList<>();
		for (int i = 0; i < recommendedContentList.size(); i++) {
			Content content = recommendedContentList.get(i);
			RecommendationContent recommendationContent = RecommendationContent.builder()
				.recommendation(recommendation)
				.content(content)
				.rank(i + 1)
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
				.rank(rc.getRank())
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
