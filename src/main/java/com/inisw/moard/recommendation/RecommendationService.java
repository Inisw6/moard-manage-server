package com.inisw.moard.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentService;
import com.inisw.moard.content.ContentType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {
	private final RecommendationRepository recommendationRepository;
	private final ContentService contentService;

	public List<Content> getRecommendations(String query, Integer limit) {
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

		return recommendedContentList;
	}
}
