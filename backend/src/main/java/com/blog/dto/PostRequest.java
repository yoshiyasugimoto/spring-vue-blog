package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostRequest {
    @NotBlank
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String coverImage;
    private String status = "DRAFT";
    private Long categoryId;
    private Set<Long> tagIds;
}
