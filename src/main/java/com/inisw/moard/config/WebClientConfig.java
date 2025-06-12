package com.inisw.moard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	@Bean(name = "naverWebClient")
	public WebClient naverWebClient(@Value("${naver.client.id}") String clientId,
		@Value("${naver.client.secret}") String clientSecret) {
		return WebClient.builder()
			.baseUrl("https://openapi.naver.com")
			.defaultHeader("X-Naver-Client-Id", clientId)
			.defaultHeader("X-Naver-Client-Secret", clientSecret)
			.build();
	}

	@Bean(name = "youtubeWebClient")
	public WebClient youtubeWebClient(@Value("${youtube.api.key}") String apiKey) {
		return WebClient.builder()
			.baseUrl("https://www.googleapis.com/youtube/v3")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

	@Bean(name = "embeddingWebClient")
	public WebClient embeddingWebClient() {
		int maxBufferSize = 16 * 1024 * 1024; // 16MB
		ExchangeStrategies strategies = ExchangeStrategies.builder()
			.codecs(configurer ->
				configurer.defaultCodecs().maxInMemorySize(maxBufferSize)
			)
			.build();

		return WebClient.builder()
			.baseUrl("http://localhost:8000")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.exchangeStrategies(strategies)
			.build();
	}
}
