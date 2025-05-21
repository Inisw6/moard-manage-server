package com.inisw.moard.api;

import com.inisw.moard.api.naver.blog.NaverBlogClient;
import com.inisw.moard.api.naver.news.NaverNewsClient;
import com.inisw.moard.api.youtube.YoutubeSearchClient;
import com.inisw.moard.content.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ContentAggregatorService {
    private final YoutubeSearchClient youtubeSearchClient;
    private final NaverBlogClient naverBlogClient;
    private final NaverNewsClient naverNewsClient;

    public List<Content> aggregate(String query, int maxResults) {
        List<Content> youtubeResultList = youtubeSearchClient.searchVideos(query, maxResults);
        List<Content> blogResultList = naverBlogClient.searchBlogs(query, maxResults, 1, "sim");
        List<Content> newsResultList = naverNewsClient.searchNews(query, maxResults, 1, "sim");

        return Stream.of(youtubeResultList, blogResultList, newsResultList)
                .flatMap(Collection::stream)
                .toList();
    }
}
