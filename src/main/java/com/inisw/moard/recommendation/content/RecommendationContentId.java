package com.inisw.moard.recommendation.content;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class RecommendationContentId implements Serializable {
	private Long recommendation;
	private Long content;
}