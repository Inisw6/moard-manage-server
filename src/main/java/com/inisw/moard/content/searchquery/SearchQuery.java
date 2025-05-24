package com.inisw.moard.content.searchquery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inisw.moard.content.Content;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "search_queries")
public class SearchQuery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String query;
	@Builder.Default
	private LocalDateTime searchedAt = LocalDateTime.now();

	@OneToMany(mappedBy = "searchQuery", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	@JsonManagedReference
	private List<Content> contentList = new ArrayList<>();
}
