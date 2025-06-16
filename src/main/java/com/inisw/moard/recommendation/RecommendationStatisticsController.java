package com.inisw.moard.recommendation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recommendations/statistics")
@RequiredArgsConstructor
@CrossOrigin
public class RecommendationStatisticsController {

	private final RecommendationStatisticsService recommendationStatisticsService;

	@GetMapping("/models")
	public ResponseEntity<List<ModelStatisticsDto>> getModelStatistics() {
		return ResponseEntity.ok(recommendationStatisticsService.getModelStatistics());
	}
} 