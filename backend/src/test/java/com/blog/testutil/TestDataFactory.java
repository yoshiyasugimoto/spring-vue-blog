package com.blog.testutil;

import com.blog.dto.CategoryRequest;
import com.blog.dto.PostRequest;
import com.blog.dto.TagRequest;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.entity.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TestDataFactory {

    public static User createUser() {
        return createUser(1L, "admin", "Admin User", "ADMIN");
    }

    public static User createUser(Long id, String username, String displayName, String role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("$2a$10$hashedpassword");
        user.setDisplayName(displayName);
        user.setRole(role);
        return user;
    }

    public static Category createCategory() {
        return createCategory(1L, "Tech", "tech", "Technology articles");
    }

    public static Category createCategory(Long id, String name, String slug, String description) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(description);
        return category;
    }

    public static Tag createTag() {
        return createTag(1L, "Java", "java");
    }

    public static Tag createTag(Long id, String name, String slug) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setSlug(slug);
        return tag;
    }

    public static Post createPost() {
        User author = createUser();
        Category category = createCategory();
        Set<Tag> tags = new HashSet<>(Set.of(createTag()));
        return createPost(1L, "Test Post", "test-post", "PUBLISHED", author, category, tags);
    }

    public static Post createPost(Long id, String title, String slug, String status,
                                   User author, Category category, Set<Tag> tags) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setSlug(slug);
        post.setContent("Test content");
        post.setExcerpt("Test excerpt");
        post.setCoverImage("https://example.com/image.jpg");
        post.setStatus(status);
        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(tags);
        if ("PUBLISHED".equals(status)) {
            post.setPublishedAt(LocalDateTime.of(2025, 1, 1, 12, 0));
        }
        return post;
    }

    public static PostRequest createPostRequest() {
        return new PostRequest("Test Post", "test-post", "Content", "Excerpt",
                "https://example.com/image.jpg", "DRAFT", 1L, Set.of(1L));
    }

    public static CategoryRequest createCategoryRequest() {
        return new CategoryRequest("Tech", "tech", "Technology articles");
    }

    public static TagRequest createTagRequest() {
        return new TagRequest("Java", "java");
    }
}
