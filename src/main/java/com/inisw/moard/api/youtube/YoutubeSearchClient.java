package com.inisw.moard.api.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class YoutubeSearchClient {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public YoutubeSearchClient(@Qualifier("youtubeWebClient") WebClient webClient, ObjectMapper objectMapper, @Value("${youtube.api.key}") String apiKey) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public List<Content> searchVideos(String query, int maxResults) {
        String json = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("key", apiKey)
                        .queryParam("type", "video")
                        .queryParam("maxResults", maxResults)
                        .queryParam("order", "relevance")
                        .queryParam("regionCode", "KR")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            // JSON → DTO
            YoutubeSearchResponse resp = objectMapper.readValue(json, YoutubeSearchResponse.class);

            // DTO → Content
            return resp.getItems().stream()
                    .map(this::toContent)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("YouTube API 응답 파싱 실패", e);
        }
    }

    private Content toContent(YoutubeSearchResponse.Item it) {
        LocalDateTime publishedAt = OffsetDateTime
                .parse(it.getSnippet().getPublishedAt())
                .toLocalDateTime();

        return Content.builder()
                .title(it.getSnippet().getTitle())
                .description(it.getSnippet().getDescription())
                .url("https://www.youtube.com/watch?v=" + it.getId().getVideoId())
                .type(ContentType.YOUTUBE)
                // 뷰어용 썸네일(중간 크기) URL
                .imageUrl(it.getSnippet().getThumbnails().getMedium().getUrl())
                .publishedAt(publishedAt)
                .build();
    }
}
