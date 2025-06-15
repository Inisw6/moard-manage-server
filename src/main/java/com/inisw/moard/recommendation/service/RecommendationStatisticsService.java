package com.inisw.moard.recommendation.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inisw.moard.recommendation.Recommendation;
import com.inisw.moard.recommendation.RecommendationRepository;
import com.inisw.moard.recommendation.dto.ModelStatisticsDto;
import com.inisw.moard.user.log.EventType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationStatisticsService {

	private final RecommendationRepository recommendationRepository;

	public List<ModelStatisticsDto> getModelStatistics() {
		List<Recommendation> recommendations = recommendationRepository.findAll();

		Map<String, List<Recommendation>> recommendationsByModel = recommendations.stream()
			.collect(Collectors.groupingBy(Recommendation::getModelVersion));

		return recommendationsByModel.entrySet().stream()
			.map(entry -> ModelStatisticsDto.builder()
				.modelVersion(entry.getKey())
				.totalRecommendations((long)entry.getValue().size() * 6)
				.totalClicks(entry.getValue().stream()
					.mapToLong(recommendation -> recommendation.getUserLogs().stream()
						.filter(log -> log.getEventType() == EventType.CLICK)
						.count())
					.sum())
				.build())
			.collect(Collectors.toList());
	}
} 