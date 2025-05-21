package com.inisw.moard.api.naver.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NaverNewsClient {
    private static final DateTimeFormatter NAVER_NEWS_DATE_FORMAT =
            DateTimeFormatter.RFC_1123_DATE_TIME;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public NaverNewsClient(@Qualifier("naverWebClient") WebClient webClient,
                           ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public List<Content> searchNews(String query, int display, int start, String sort) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/news.json")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .queryParam("start", start)
                        .queryParam("sort", sort)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            NaverNewsResponse result = objectMapper.readValue(response, NaverNewsResponse.class);
            return result.getItems().stream()
                    .map(this::toContent)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Naver 뉴스 API 응답 파싱 실패", e);
        }
    }

    private Content toContent(NaverNewsItem item) {
        return Content.builder()
                .title(item.getTitle())
                .url(item.getLink())
                .type(ContentType.NEWS)
                .publishedAt(ZonedDateTime.parse(item.getPubDate(), NAVER_NEWS_DATE_FORMAT).toLocalDateTime())
                .build();
    }
}
