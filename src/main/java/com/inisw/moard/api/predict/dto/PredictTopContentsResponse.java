package com.inisw.moard.api.predict.dto;

import java.util.List;

public record PredictTopContentsResponse(
    List<Long> content_ids
) {} 