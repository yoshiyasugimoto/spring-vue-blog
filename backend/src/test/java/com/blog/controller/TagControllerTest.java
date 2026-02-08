package com.blog.controller;

import com.blog.config.SecurityConfig;
import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import com.blog.service.UserService;
import com.blog.testutil.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
@Import(SecurityConfig.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private UserService userService;

    @Test
    void getAll_Returns200WithListOfTags() throws Exception {
        Tag tag1 = TestDataFactory.createTag(1L, "Java", "java");
        Tag tag2 = TestDataFactory.createTag(2L, "Spring", "spring");
        when(tagService.findAll()).thenReturn(List.of(tag1, tag2));

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].name").value("Spring"));
    }

    @Test
    void getAll_Returns200WithEmptyList() throws Exception {
        when(tagService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void create_Returns200_WhenAuthenticated() throws Exception {
        Tag tag = TestDataFactory.createTag(1L, "Java", "java");
        when(tagService.create(any(TagRequest.class))).thenReturn(tag);

        mockMvc.perform(post("/api/admin/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TagRequest("Java", "java"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.slug").value("java"));
    }

    @Test
    void create_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/admin/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TagRequest("Java", "java"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void delete_Returns204_WhenAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/admin/tags/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(tagService).delete(1L);
    }

    @Test
    void delete_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/admin/tags/1")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
