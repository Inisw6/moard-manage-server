package com.inisw.moard.searchquery;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search-queries")
@RequiredArgsConstructor
public class SearchQueryController {
    private final SearchQueryService searchQueryService;

    @GetMapping("/{query}")
    public SearchQuery getSearchQueries(@PathVariable String query) {
        return searchQueryService.readSearchQuery(query);
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
