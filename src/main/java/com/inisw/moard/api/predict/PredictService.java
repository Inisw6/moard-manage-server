package com.inisw.moard.api.predict;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.inisw.moard.api.predict.dto.PredictTopContentsRequest;
import com.inisw.moard.api.predict.dto.PredictTopContentsResponse;

@Service
public class PredictService {

	private final WebClient webClient;

	public PredictService(@Qualifier("predictWebClient") WebClient webClient) {
		this.webClient = webClient;
	}

	public PredictTopContentsResponse predictTopContents(PredictTopContentsRequest request) {
		return webClient.post()
			.uri("/api/v1/predict/top-contents")
			.bodyValue(request)
			.retrieve()
			.bodyToMono(PredictTopContentsResponse.class)
			.block();
	}
}
