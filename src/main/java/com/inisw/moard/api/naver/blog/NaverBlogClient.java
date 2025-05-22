package com.inisw.moard.api.naver.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NaverBlogClient {
    private static final DateTimeFormatter NAVER_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public NaverBlogClient(@Qualifier("naverWebClient") WebClient webClient,
                           ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public List<Content> searchBlogs(String query, int display, int start, String sort) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/blog.json")
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
            NaverBlogResponse result = objectMapper.readValue(response, NaverBlogResponse.class);
            return result.getItems().stream()
                    .map(this::toContent)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Naver API 응답 파싱 실패", e);
        }
    }

    private Content toContent(NaverBlogItem item) {
        return Content.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .url(item.getLink())
                .type(ContentType.BLOG)
                .publishedAt(parsePostDate(item.getPostdate()))
                .build();
    }

    private LocalDateTime parsePostDate(String postdate) {
        return LocalDate.parse(postdate, NAVER_DATE_FORMAT).atStartOfDay();
    }
}
