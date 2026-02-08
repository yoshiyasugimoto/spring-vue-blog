package com.blog.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PostRequest(
        @NotBlank String title,
        String slug,
        String content,
        String excerpt,
        String coverImage,
        String status,
        Long categoryId,
        Set<Long> tagIds
) {
    public PostRequest {
        if (status == null) {
            status = "DRAFT";
        }
    }
}
