package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlogPostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String slug;

    private String excerpt;

    @NotBlank
    private String content;

    private String imageUrl;
    private Boolean published;
}
