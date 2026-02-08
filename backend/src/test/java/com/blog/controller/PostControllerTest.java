package com.blog.controller;

import com.blog.entity.Post;
import com.blog.entity.PostStatus;
import com.blog.entity.User;
import com.blog.service.PostService;
import com.blog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser
    void getByIdReturnsPost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setSlug("test-post");
        post.setStatus(PostStatus.DRAFT);
        User author = new User();
        author.setUsername("admin");
        author.setDisplayName("Admin");
        post.setAuthor(author);

        when(postService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/api/admin/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void getByIdRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/admin/posts/1"))
                .andExpect(status().isUnauthorized());
    }
}
