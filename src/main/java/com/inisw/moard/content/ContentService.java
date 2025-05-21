package com.inisw.moard.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    public List<Content> createContentList(List<Content> contents) {
        return contentRepository.saveAll(contents);
    }
}
