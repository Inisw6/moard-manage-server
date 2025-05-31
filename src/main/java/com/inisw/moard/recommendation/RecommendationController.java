package com.inisw.moard.recommendation;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Recommendation", description = "추천 API")
public class RecommendationController {
	private final RecommendationService recommendationService;

	@GetMapping
	@Operation(summary = "추천 콘텐츠 조회", description = "주어진 쿼리에 대한 추천 콘텐츠를 반환합니다.")
	public RecommendationResponseDto getRecommendations(
		@Parameter(description = "검색 쿼리") @RequestParam String query,
		@Parameter(description = "반환할 콘텐츠 수 (기본값: 6)") @RequestParam(required = false, defaultValue = "6") Integer limit,
		@Parameter(description = "사용자 UUID") @RequestParam UUID userId
	) {
		return recommendationService.getRecommendations(query, limit, userId);
	}
} 