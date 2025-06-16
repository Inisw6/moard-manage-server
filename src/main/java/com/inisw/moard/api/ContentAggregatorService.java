package com.inisw.moard.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.inisw.moard.api.embedding.EmbeddingService;
import com.inisw.moard.api.naver.blog.NaverBlogClient;
import com.inisw.moard.api.naver.news.NaverNewsClient;
import com.inisw.moard.api.youtube.YoutubeSearchClient;
import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentAggregatorService {
	private final YoutubeSearchClient youtubeSearchClient;
	private final NaverBlogClient naverBlogClient;
	private final NaverNewsClient naverNewsClient;
	private final EmbeddingService embeddingService;
	private final ContentRepository contentRepository;

	public List<Content> aggregate(String query, int maxResults) {
		List<Content> youtubeResultList = youtubeSearchClient.searchVideos(query, maxResults);
		List<Content> blogResultList = naverBlogClient.searchBlogs(query, maxResults, 1, "sim");
		List<Content> newsResultList = naverNewsClient.searchNews(query, maxResults, 1, "sim");

		List<Content> aggregatedContent = Stream.of(youtubeResultList, blogResultList, newsResultList)
			.flatMap(Collection::stream)
			.toList();

		// Filter out contents that already exist in the database
		List<String> existingUrls = contentRepository.findUrlsByUrlIn(
			aggregatedContent.stream().map(Content::getUrl).collect(Collectors.toList()));
		List<Content> newContents = aggregatedContent.stream()
			.filter(content -> !existingUrls.contains(content.getUrl()))
			.collect(Collectors.toList());

		if (newContents.isEmpty()) {
			return newContents;
		}

		embeddingService.setEmbeddings(newContents).block(); // block for simplicity

		return newContents;
	}
}
