package com.inisw.moard.api.embedding;

import java.util.List;

public record EmbeddingResponse(List<List<Double>> embeddings) {
} 