package com.inisw.moard.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inisw.moard.api.ContentAggregatorService;
import com.inisw.moard.content.searchquery.SearchQuery;
import com.inisw.moard.content.searchquery.SearchQueryRepository;
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
		List<Content> result;
		StockInfo stockInfo = stockInfoRepository.findByName(query).orElse(null);
		if (stockInfo == null) {
			result = contentAggregatorService.aggregate(query, maxResults);
		} else {
			SearchQuery searchQuery = searchQueryRepository.findByQuery(query).orElse(null);
			if (searchQuery == null) {
				List<Content> contents = contentAggregatorService.aggregate(query, maxResults);
				searchQuery = SearchQuery.builder()
					.query(query)
					.stockInfo(stockInfo)
					.build();
				for (Content content : contents) {
					content.setSearchQuery(searchQuery);
				}
				searchQuery = searchQueryRepository.save(searchQuery);
				result = searchQuery.getContentList();
			} else {
				LocalDateTime expireTime = searchQuery.getSearchedAt().plusMinutes(1);
				if (expireTime.isAfter(LocalDateTime.now())) {
					result = searchQuery.getContentList();
				} else {
					List<Content> contents = contentAggregatorService.aggregate(query, maxResults);
					Map<String, Content> savedContents = searchQuery.getContentList().stream()
						.collect(Collectors.toMap(Content::getUrl, c -> c));

					List<Content> updatedContentList = new ArrayList<>();

					for (Content content : contents) {
						Content existing = savedContents.get(content.getUrl());

						if (existing != null) {
							existing.setQueryAt(LocalDateTime.now());
							updatedContentList.add(existing);
						} else {
							content.setSearchQuery(searchQuery);
							updatedContentList.add(content);
						}
					}

					searchQuery.getContentList().clear();
					searchQuery.getContentList().addAll(updatedContentList);

					searchQuery.setSearchedAt(LocalDateTime.now());
					searchQuery = searchQueryRepository.save(searchQuery);
				}
				result = searchQuery.getContentList();
			}
		}
		return result;
	}
}
