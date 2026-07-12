package com.hospital.management.service;

import com.hospital.management.domain.entity.BlogPost;
import com.hospital.management.domain.entity.User;
import com.hospital.management.dto.request.BlogPostRequest;
import com.hospital.management.dto.response.BlogPostResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.BlogPostRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogPostRepository blogPostRepository;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    public PageResponse<BlogPostResponse> getAll(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(blogPostRepository.findAll(pageable).map(entityMapper::toBlogPostResponse));
    }

    public PageResponse<BlogPostResponse> getPublic(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "publishedAt", "desc");
        return PageResponse.from(blogPostRepository.findByPublishedTrue(pageable)
                .map(entityMapper::toBlogPostResponse));
    }

    public BlogPostResponse getById(UUID id) {
        return entityMapper.toBlogPostResponse(findPost(id));
    }

    @Transactional
    public BlogPostResponse create(BlogPostRequest request) {
        if (blogPostRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Slug already exists");
        }
        User user = securityUtils.getCurrentUser();
        BlogPost post = BlogPost.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .excerpt(request.getExcerpt())
                .content(request.getContent())
                .authorId(user.getId())
                .imageUrl(request.getImageUrl())
                .published(request.getPublished() != null ? request.getPublished() : false)
                .publishedAt(Boolean.TRUE.equals(request.getPublished()) ? Instant.now() : null)
                .build();
        return entityMapper.toBlogPostResponse(blogPostRepository.save(post));
    }

    @Transactional
    public BlogPostResponse update(UUID id, BlogPostRequest request) {
        BlogPost post = findPost(id);
        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setExcerpt(request.getExcerpt());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        if (request.getPublished() != null) {
            post.setPublished(request.getPublished());
            if (Boolean.TRUE.equals(request.getPublished()) && post.getPublishedAt() == null) {
                post.setPublishedAt(Instant.now());
            }
        }
        return entityMapper.toBlogPostResponse(blogPostRepository.save(post));
    }

    @Transactional
    public void delete(UUID id) {
        blogPostRepository.delete(findPost(id));
    }

    private BlogPost findPost(UUID id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found: " + id));
    }
}
