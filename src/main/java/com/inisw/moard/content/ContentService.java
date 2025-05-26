package com.inisw.moard.content;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inisw.moard.api.ContentAggregatorService;
import com.inisw.moard.content.searchquery.SearchQuery;
import com.inisw.moard.content.searchquery.SearchQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentService {
	private final ContentRepository contentRepository;
	private final SearchQueryRepository searchQueryRepository;
	private final ContentAggregatorService contentAggregatorService;

	public Content createContent(Content content) {
		return contentRepository.save(content);
	}

	public List<Content> createContentList(List<Content> contents) {
		return contentRepository.saveAll(contents);
	}

	public List<Content> readContentsByQuery(String query, Integer maxResults) {
		List<Content> result;

		SearchQuery searchQuery = searchQueryRepository.findByQuery(query).orElse(null);
		if (searchQuery != null) {
			result = searchQuery.getContentList();
		} else {
			List<Content> contents = contentAggregatorService.aggregate(query, maxResults);

			searchQuery = SearchQuery.builder()
				.query(query)
				.contentList(contents)
				.build();
			SearchQuery savedSearchQuery = searchQueryRepository.save(searchQuery);
			result = searchQuery.getContentList();
		}
		return result;
	}
}
