package com.inisw.moard.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inisw.moard.api.ContentAggregatorService;
import com.inisw.moard.searchquery.SearchQuery;
import com.inisw.moard.searchquery.SearchQueryRepository;
import com.inisw.moard.stock.StockInfo;
import com.inisw.moard.stock.StockInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentService {
	private final ContentRepository contentRepository;
	private final SearchQueryRepository searchQueryRepository;
	private final ContentAggregatorService contentAggregatorService;
	private final StockInfoRepository stockInfoRepository;

	public Content createContent(Content content) {
		return contentRepository.save(content);
	}

	public List<Content> createContentList(List<Content> contents) {
		return contentRepository.saveAll(contents);
	}

	public List<Content> readContentsByQuery(String query, Integer maxResults) {
		StockInfo stockInfo = stockInfoRepository.findByName(query).orElse(null);

		List<Content> result = (stockInfo == null)
			? aggregateFreshContents(query, maxResults)
			: getOrUpdateContents(query, stockInfo, maxResults);

		return sortAndLimit(result, maxResults);
	}

	private List<Content> aggregateFreshContents(String query, int maxResults) {
		return contentAggregatorService.aggregate(query, maxResults);
	}

	private List<Content> getOrUpdateContents(String query, StockInfo stockInfo, int maxResults) {
		SearchQuery searchQuery = searchQueryRepository.findByQuery(query).orElse(null);

		if (searchQuery == null) {
			return createNewSearchQueryWithContents(query, stockInfo, maxResults);
		}

		if (isExpired(searchQuery)) {
			return refreshSearchQueryContents(searchQuery, query, maxResults);
		}

		return searchQuery.getContentList();
	}

	private List<Content> createNewSearchQueryWithContents(String query, StockInfo stockInfo, int maxResults) {
		List<Content> contents = contentAggregatorService.aggregate(query, maxResults);
		SearchQuery searchQuery = SearchQuery.builder()
			.query(query)
			.stockInfo(stockInfo)
			.searchedAt(LocalDateTime.now())
			.build();

		for (Content content : contents) {
			content.setSearchQuery(searchQuery);
		}

		searchQuery = searchQueryRepository.save(searchQuery);
		return searchQuery.getContentList();
	}

	private List<Content> refreshSearchQueryContents(SearchQuery searchQuery, String query, int maxResults) {
		List<Content> newContents = contentAggregatorService.aggregate(query, maxResults);
		Map<String, Content> existingMap = searchQuery.getContentList().stream()
			.collect(Collectors.toMap(Content::getUrl, c -> c));

		List<Content> updated = new ArrayList<>();
		for (Content content : newContents) {
			Content existing = existingMap.get(content.getUrl());
			if (existing != null) {
				existing.setQueryAt(LocalDateTime.now());
				updated.add(existing);
			} else {
				content.setSearchQuery(searchQuery);
				updated.add(content);
			}
		}

		searchQuery.getContentList().clear();
		searchQuery.getContentList().addAll(updated);
		searchQuery.setSearchedAt(LocalDateTime.now());
		searchQueryRepository.save(searchQuery);

		return searchQuery.getContentList();
	}

	private boolean isExpired(SearchQuery searchQuery) {
		return searchQuery.getSearchedAt().plusMinutes(1).isBefore(LocalDateTime.now());
	}

	private List<Content> sortAndLimit(List<Content> contents, int maxResults) {
		return contents.stream()
			.sorted(Comparator.comparing(Content::getQueryAt, Comparator.nullsLast(Comparator.reverseOrder())))
			.limit(maxResults * 3L)
			.toList();
	}

}
