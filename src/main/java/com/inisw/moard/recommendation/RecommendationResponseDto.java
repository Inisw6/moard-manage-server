package com.inisw.moard.recommendation;

import java.time.LocalDateTime;
import java.util.List;

import com.inisw.moard.content.Content;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendationResponseDto {
	private Long id;
	private String query;
	private String modelVersion;
	private LocalDateTime recommendedAt;
	private List<RankedContent> contents;

	@Getter
	@Builder
	public static class RankedContent {
		private Content content;
		private Integer rank;
	}
}
