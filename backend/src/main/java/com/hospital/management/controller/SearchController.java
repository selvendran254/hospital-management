package com.hospital.management.controller;

import com.hospital.management.dto.response.SearchResultItem;
import com.hospital.management.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<Map<String, List<SearchResultItem>>> globalSearch(@RequestParam("q") String query) {
        return ResponseEntity.ok(searchService.globalSearch(query));
    }
}
