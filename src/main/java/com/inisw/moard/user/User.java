package com.inisw.moard.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.inisw.moard.recommendation.Recommendation;
import com.inisw.moard.user.log.UserLog;
import com.inisw.moard.user.log.stock.StockLog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private UUID uuid;

	@OneToMany(mappedBy = "user")
	@Builder.Default
	@JsonBackReference
	private List<UserLog> userLogList = new ArrayList<UserLog>();

	@OneToMany(mappedBy = "user")
	@Builder.Default
	@JsonBackReference
	private List<Recommendation> recommendationList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@Builder.Default
	@JsonBackReference
	private List<StockLog> stockLogList = new ArrayList<>();

}
