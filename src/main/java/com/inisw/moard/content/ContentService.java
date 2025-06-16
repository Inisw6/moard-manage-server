package com.inisw.moard.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inisw.moard.api.ContentAggregatorService;
import com.inisw.moard.searchquery.SearchQuery;
import com.inisw.moard.searchquery.SearchQueryRepository;
import com.inisw.moard.stock.StockInfo;
import com.inisw.moard.stock.StockInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {
	private final ContentRepository contentRepository;
	private final SearchQueryRepository searchQueryRepository;
	private final ContentAggregatorService contentAggregatorService;
	private final StockInfoRepository stockInfoRepository;

	@Transactional
	public List<Content> readContentsByQuery(String query, Integer maxResults) {
		StockInfo stockInfo = stockInfoRepository.findByName(query).orElse(null);

		if (stockInfo == null) {
			// It is not a stock, just aggregate
			StockInfo stocKInfo = StockInfo.builder()
				.name(query)
				.industryDetail(" ")
				.build();
			stockInfo = stockInfoRepository.save(stocKInfo);
			// return aggregateFreshContents(query, maxResults);
		}

		String stockName = stockInfo.getName().replaceAll("\\s+", "");
		List<String> keywords = Arrays.asList("실적", "전망", "주가", stockInfo.getIndustryDetail());
		List<Content> allContents = new ArrayList<>();

		for (String keyword : keywords) {
			String keywordQuery = stockName + ' ' + keyword;
			allContents.addAll(getOrUpdateContents(keywordQuery, stockInfo, 3));
		}

		return allContents.stream()
			.distinct()
			.sorted(Comparator.comparing(Content::getQueryAt, Comparator.nullsLast(Comparator.reverseOrder())))
			.collect(Collectors.toList());
	}

	private List<Content> aggregateFreshContents(String query, int maxResults) {
		List<Content> contents = contentAggregatorService.aggregate(query, maxResults);
		return contentRepository.saveAll(contents);
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
		searchQuery.setContentList(contents);

		searchQuery = searchQueryRepository.save(searchQuery);
		return searchQuery.getContentList();
	}

	private List<Content> refreshSearchQueryContents(SearchQuery searchQuery, String query, int maxResults) {
		List<Content> newContents = contentAggregatorService.aggregate(query, maxResults);

		// Get current URLs to avoid adding duplicates
		List<String> currentUrls = searchQuery.getContentList().stream()
			.map(Content::getUrl)
			.collect(Collectors.toList());

		List<Content> contentsToAdd = newContents.stream()
			.filter(c -> !currentUrls.contains(c.getUrl()))
			.collect(Collectors.toList());

		for (Content content : contentsToAdd) {
			content.setSearchQuery(searchQuery);
		}

		searchQuery.getContentList().addAll(contentsToAdd);
		searchQuery.setSearchedAt(LocalDateTime.now());
		contentRepository.saveAll(contentsToAdd); // Save only new contents
		searchQueryRepository.save(searchQuery);

		return searchQuery.getContentList();
	}

	private boolean isExpired(SearchQuery searchQuery) {
		return searchQuery.getSearchedAt().plusMinutes(2400).isBefore(LocalDateTime.now());
	}
}
