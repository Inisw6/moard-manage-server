package com.inisw.moard.api.predict;

import com.inisw.moard.api.predict.dto.PredictTopContentsRequest;
import com.inisw.moard.api.predict.dto.PredictTopContentsResponse;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    public PredictTopContentsResponse predictTopContents(PredictTopContentsRequest request) {
        // 받은 content_ids가 이미 추천 순서대로 정렬된 리스트이므로 그대로 반환
        return new PredictTopContentsResponse(request.content_ids());
    }
} 