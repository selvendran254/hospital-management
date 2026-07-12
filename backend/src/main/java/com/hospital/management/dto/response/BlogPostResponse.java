package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponse {

    private UUID id;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private UUID authorId;
    private String authorName;
    private String imageUrl;
    private Boolean published;
    private Instant publishedAt;
    private Instant createdAt;
}
