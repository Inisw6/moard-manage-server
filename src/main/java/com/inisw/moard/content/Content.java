package com.inisw.moard.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.inisw.moard.searchquery.SearchQuery;
import com.inisw.moard.user.log.UserLog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
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
@Table(name = "contents")
public class Content {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "text")
	private String title;
	@Column(columnDefinition = "text")
	private String description;
	@Column(columnDefinition = "text", unique = true)
	private String url;
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "text")
	private ContentType type;
	@Column(name = "image_url", columnDefinition = "text")
	private String imageUrl;
	
	@Convert(converter = DoubleListConverter.class)
	@Column(name = "embedding", columnDefinition = "text")
	private List<Double> embedding;

	@Column(name = "published_at")
	private LocalDateTime publishedAt;
	@Column
	@Builder.Default
	private LocalDateTime queryAt = LocalDateTime.now();

	@OneToMany(mappedBy = "content")
	@Builder.Default
	private List<UserLog> userLogList = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "search_query_id")
	@JsonBackReference
	private SearchQuery searchQuery;

	public void setSearchQuery(SearchQuery searchQuery) {
		this.searchQuery = searchQuery;
		if (searchQuery != null && !searchQuery.getContentList().contains(this)) {
			searchQuery.getContentList().add(this);
		}
	}
}
