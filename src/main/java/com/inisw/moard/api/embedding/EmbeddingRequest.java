package com.inisw.moard.api.embedding;

import java.util.List;

public record EmbeddingRequest(List<EmbeddingContent> contents) {
} 