package com.blog.dto;

import com.blog.entity.PostStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PostRequest(
        @NotBlank String title,
        String slug,
        String content,
        String excerpt,
        String coverImage,
        PostStatus status,
        Long categoryId,
        Set<Long> tagIds
) {
    public PostRequest {
        if (status == null) {
            status = PostStatus.DRAFT;
        }
    }
}
