package com.inisw.moard.recommendation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModelStatisticsDto {
	private String modelVersion;
	private Long totalRecommendations;
	private Long totalClicks;
} 