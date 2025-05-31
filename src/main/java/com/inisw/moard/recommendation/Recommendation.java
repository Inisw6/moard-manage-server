package com.inisw.moard.recommendation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.inisw.moard.recommendation.content.RecommendationContent;
import com.inisw.moard.user.User;
import com.inisw.moard.user.log.UserLog;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "recommendations")
public class Recommendation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Builder.Default
	private LocalDateTime recommendedAt = LocalDateTime.now();
	private String modelVersion;
	private String query;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "recommendation")
	@Builder.Default
	private List<RecommendationContent> recommendationContentList = new ArrayList<>();

	@OneToMany(mappedBy = "recommendation")
	@Builder.Default
	private List<UserLog> userLogs = new ArrayList<>();

}
