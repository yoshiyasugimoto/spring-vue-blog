package com.blog.dto;

import com.blog.entity.User;

public record UserResponse(
        Long id,
        String username,
        String displayName,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getRole().name()
        );
    }
}
