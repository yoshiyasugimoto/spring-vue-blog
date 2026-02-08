package com.blog.dto;

import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {

    @Test
    void from_MapsAllFieldsCorrectly() {
        Post post = TestDataFactory.createPost();

        PostResponse response = PostResponse.from(post);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Test Post");
        assertThat(response.slug()).isEqualTo("test-post");
        assertThat(response.content()).isEqualTo("Test content");
        assertThat(response.excerpt()).isEqualTo("Test excerpt");
        assertThat(response.coverImage()).isEqualTo("https://example.com/image.jpg");
        assertThat(response.status()).isEqualTo("PUBLISHED");
        assertThat(response.publishedAt()).isNotNull();
        assertThat(response.category()).isNotNull();
        assertThat(response.author()).isNotNull();
        assertThat(response.tags()).hasSize(1);
    }

    @Test
    void from_MapsNestedCategoryCorrectly() {
        Post post = TestDataFactory.createPost();

        PostResponse response = PostResponse.from(post);

        assertThat(response.category().id()).isEqualTo(1L);
        assertThat(response.category().name()).isEqualTo("Tech");
        assertThat(response.category().slug()).isEqualTo("tech");
    }

    @Test
    void from_MapsNestedAuthorCorrectly() {
        Post post = TestDataFactory.createPost();

        PostResponse response = PostResponse.from(post);

        assertThat(response.author().id()).isEqualTo(1L);
        assertThat(response.author().username()).isEqualTo("admin");
        assertThat(response.author().displayName()).isEqualTo("Admin User");
        assertThat(response.author().role()).isEqualTo("ADMIN");
    }

    @Test
    void from_MapsMultipleTagsCorrectly() {
        Tag tag1 = TestDataFactory.createTag(1L, "Java", "java");
        Tag tag2 = TestDataFactory.createTag(2L, "Spring", "spring");
        Tag tag3 = TestDataFactory.createTag(3L, "Boot", "boot");
        Post post = TestDataFactory.createPost(1L, "Test", "test", "PUBLISHED",
                TestDataFactory.createUser(), TestDataFactory.createCategory(),
                new HashSet<>(Set.of(tag1, tag2, tag3)));

        PostResponse response = PostResponse.from(post);

        assertThat(response.tags()).hasSize(3);
    }

    @Test
    void from_HandlesNullCategory() {
        Post post = TestDataFactory.createPost(1L, "Test", "test", "DRAFT",
                TestDataFactory.createUser(), null, new HashSet<>());

        PostResponse response = PostResponse.from(post);

        assertThat(response.category()).isNull();
    }

    @Test
    void from_HandlesNullAuthor() {
        Post post = TestDataFactory.createPost(1L, "Test", "test", "DRAFT",
                null, TestDataFactory.createCategory(), new HashSet<>());

        PostResponse response = PostResponse.from(post);

        assertThat(response.author()).isNull();
    }

    @Test
    void from_HandlesNullTags() {
        Post post = TestDataFactory.createPost(1L, "Test", "test", "DRAFT",
                TestDataFactory.createUser(), TestDataFactory.createCategory(), null);

        PostResponse response = PostResponse.from(post);

        assertThat(response.tags()).isNull();
    }

    @Test
    void from_HandlesEmptyTags() {
        Post post = TestDataFactory.createPost(1L, "Test", "test", "DRAFT",
                TestDataFactory.createUser(), TestDataFactory.createCategory(), new HashSet<>());

        PostResponse response = PostResponse.from(post);

        assertThat(response.tags()).isEmpty();
    }
}
