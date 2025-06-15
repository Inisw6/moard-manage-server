package com.inisw.moard.recommendation.controller;

import com.inisw.moard.recommendation.dto.ModelStatisticsDto;
import com.inisw.moard.recommendation.service.RecommendationStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations/statistics")
@RequiredArgsConstructor
public class RecommendationStatisticsController {

    private final RecommendationStatisticsService recommendationStatisticsService;

    @GetMapping("/models")
    public ResponseEntity<List<ModelStatisticsDto>> getModelStatistics() {
        return ResponseEntity.ok(recommendationStatisticsService.getModelStatistics());
    }
} 