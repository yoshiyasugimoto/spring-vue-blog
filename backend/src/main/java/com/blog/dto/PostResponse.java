package com.blog.dto;

import com.blog.entity.Post;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record PostResponse(
    Long id,
    String title,
    String slug,
    String content,
    String excerpt,
    String coverImage,
    String status,
    CategoryResponse category,
    UserResponse author,
    Set<TagResponse> tags,
    LocalDateTime publishedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getSlug(),
            post.getContent(),
            post.getExcerpt(),
            post.getCoverImage(),
            post.getStatus(),
            post.getCategory() != null ? CategoryResponse.from(post.getCategory()) : null,
            post.getAuthor() != null ? UserResponse.from(post.getAuthor()) : null,
            post.getTags() != null
                ? post.getTags().stream().map(TagResponse::from).collect(Collectors.toSet())
                : null,
            post.getPublishedAt(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}