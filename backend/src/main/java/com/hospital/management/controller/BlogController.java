package com.hospital.management.controller;

import com.hospital.management.dto.request.BlogPostRequest;
import com.hospital.management.dto.response.BlogPostResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.service.BlogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "Blog")
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<BlogPostResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(blogService.getAll(page, size));
    }

    @GetMapping("/public")
    public ResponseEntity<PageResponse<BlogPostResponse>> getPublic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(blogService.getPublic(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(blogService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponse> create(@Valid @RequestBody BlogPostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody BlogPostRequest request) {
        return ResponseEntity.ok(blogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
