package com.inisw.moard.api.predict.dto;

import java.util.List;
import java.util.UUID;

public record PredictTopContentsRequest(
    UUID user_id,
    List<Long> content_ids
) {} 