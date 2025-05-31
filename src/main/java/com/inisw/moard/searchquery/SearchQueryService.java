package com.inisw.moard.searchquery;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchQueryService {
	private final SearchQueryRepository searchQueryRepository;

	public SearchQuery createSearchQuery(String query) {
		SearchQuery searchQuery = SearchQuery.builder().query(query).build();
		return searchQueryRepository.save(searchQuery);
	}

	public SearchQuery readSearchQuery(String query) {
		return searchQueryRepository.findByQuery(query).orElse(null);
	}

	public void deleteSearchQuery(String query) {
		SearchQuery searchQuery = searchQueryRepository.findByQuery(query).orElse(null);
		searchQueryRepository.delete(searchQuery);
	}

}
