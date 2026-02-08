package com.blog.dto;

import com.blog.entity.Tag;

public record TagResponse(
        Long id,
        String name,
        String slug
) {
    public static TagResponse from(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getSlug()
        );
    }
}
