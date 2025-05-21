package com.inisw.moard.searchquery;

import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search-queries")
@RequiredArgsConstructor
@CrossOrigin
public class SearchQueryController {
    private final SearchQueryService searchQueryService;
    private final ContentService contentService;

    @GetMapping("/{query}")
    public List<Content> getSearchQueries(@PathVariable String query, @RequestParam("max_results") Integer maxResults) {
        return contentService.readContentsByQuery(query, maxResults);
    }

    @PostMapping
    public SearchQuery postSearchQuery(@RequestParam String query) {
        return searchQueryService.createSearchQuery(query);
    }

    @DeleteMapping
    public String deleteSearchQuery(@RequestParam String query) {
        searchQueryService.deleteSearchQuery(query);
        return "Deleted";
    }

}
