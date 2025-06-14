package com.inisw.moard.recommendation.content;

import com.inisw.moard.content.Content;
import com.inisw.moard.recommendation.Recommendation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recommendation_contents")
@IdClass(RecommendationContentId.class)
public class RecommendationContent {
	@Id
	@ManyToOne
	@JoinColumn(name = "recommendation_id")
	private Recommendation recommendation;

	@Id
	@ManyToOne
	@JoinColumn(name = "content_id")
	private Content content;

	private Integer ranks;
}
