package com.blog.controller;

import com.blog.config.SecurityConfig;
import com.blog.dto.PostRequest;
import com.blog.entity.Post;
import com.blog.service.PostService;
import com.blog.service.UserService;
import com.blog.testutil.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private UserService userService;

    // --- Public endpoints ---

    @Test
    void getPublished_Returns200WithPageOfPosts() throws Exception {
        Post post = TestDataFactory.createPost();
        Page<Post> page = new PageImpl<>(List.of(post));
        when(postService.findPublished(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/posts").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Test Post"))
                .andExpect(jsonPath("$.content[0].slug").value("test-post"));
    }

    @Test
    void getPublished_Returns200WithEmptyPage() throws Exception {
        Page<Post> emptyPage = new PageImpl<>(Collections.emptyList());
        when(postService.findPublished(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getBySlug_Returns200WithPost() throws Exception {
        Post post = TestDataFactory.createPost();
        when(postService.findPublishedBySlug("test-post")).thenReturn(post);

        mockMvc.perform(get("/api/posts/test-post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.slug").value("test-post"))
                .andExpect(jsonPath("$.category.name").value("Tech"))
                .andExpect(jsonPath("$.author.username").value("admin"));
    }

    // --- Admin endpoints ---

    @Test
    @WithMockUser
    void getAll_Returns200WithPageOfPosts_WhenAuthenticated() throws Exception {
        Post post = TestDataFactory.createPost();
        Page<Post> page = new PageImpl<>(List.of(post));
        when(postService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/admin/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Test Post"));
    }

    @Test
    void getAll_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/admin/posts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin")
    void create_Returns200WithCreatedPost_WhenAuthenticated() throws Exception {
        Post post = TestDataFactory.createPost();
        when(postService.create(any(PostRequest.class), eq("admin"))).thenReturn(post);

        PostRequest request = new PostRequest("Test Post", "test-post", "Content", "Excerpt",
                "https://example.com/img.jpg", "DRAFT", 1L, Set.of(1L));

        mockMvc.perform(post("/api/admin/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.slug").value("test-post"));
    }

    @Test
    void create_Returns401_WhenNotAuthenticated() throws Exception {
        PostRequest request = new PostRequest("Test", "test", "Content", null, null, null, null, null);

        mockMvc.perform(post("/api/admin/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void update_Returns200WithUpdatedPost_WhenAuthenticated() throws Exception {
        Post post = TestDataFactory.createPost();
        when(postService.update(eq(1L), any(PostRequest.class))).thenReturn(post);

        PostRequest request = new PostRequest("Updated", "updated", "Content", null, null, "DRAFT", null, null);

        mockMvc.perform(put("/api/admin/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    void update_Returns401_WhenNotAuthenticated() throws Exception {
        PostRequest request = new PostRequest("Test", "test", "Content", null, null, null, null, null);

        mockMvc.perform(put("/api/admin/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void delete_Returns204_WhenAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/admin/posts/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(postService).delete(1L);
    }

    @Test
    void delete_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/admin/posts/1")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
