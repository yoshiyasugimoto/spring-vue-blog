package com.blog.dto;

import com.blog.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String coverImage;
    private String status;
    private CategoryResponse category;
    private UserResponse author;
    private Set<TagResponse> tags;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setSlug(post.getSlug());
        response.setContent(post.getContent());
        response.setExcerpt(post.getExcerpt());
        response.setCoverImage(post.getCoverImage());
        response.setStatus(post.getStatus());
        response.setPublishedAt(post.getPublishedAt());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        if (post.getCategory() != null) {
            response.setCategory(CategoryResponse.from(post.getCategory()));
        }
        if (post.getAuthor() != null) {
            response.setAuthor(UserResponse.from(post.getAuthor()));
        }
        if (post.getTags() != null) {
            response.setTags(post.getTags().stream()
                    .map(TagResponse::from)
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
