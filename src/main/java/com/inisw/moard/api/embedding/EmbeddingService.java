package com.inisw.moard.api.embedding;

import com.inisw.moard.content.Content;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class EmbeddingService {

    private final WebClient webClient;

    public EmbeddingService(@Qualifier("embeddingWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> setEmbeddings(List<Content> contents) {
        List<EmbeddingContent> embeddingContents = contents.stream()
                .map(content -> new EmbeddingContent(content.getTitle(), content.getDescription()))
                .toList();

        EmbeddingRequest request = new EmbeddingRequest(embeddingContents);

        return webClient.post()
                .uri("/api/v1/embedding/doc2vec/bulk")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .doOnNext(response -> {
                    List<List<Double>> embeddings = response.embeddings();
                    for (int i = 0; i < contents.size(); i++) {
                        contents.get(i).setEmbedding(embeddings.get(i));
                    }
                })
                .then();
    }
} 