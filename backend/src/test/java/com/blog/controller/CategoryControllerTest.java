package com.blog.controller;

import com.blog.config.SecurityConfig;
import com.blog.dto.CategoryRequest;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private UserService userService;

    @Test
    void getAll_Returns200WithListOfCategories() throws Exception {
        Category cat1 = TestDataFactory.createCategory(1L, "Tech", "tech", "Technology");
        Category cat2 = TestDataFactory.createCategory(2L, "Life", "life", "Lifestyle");
        when(categoryService.findAll()).thenReturn(List.of(cat1, cat2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Tech"))
                .andExpect(jsonPath("$[0].slug").value("tech"))
                .andExpect(jsonPath("$[1].name").value("Life"));
    }

    @Test
    void getAll_Returns200WithEmptyList() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void create_Returns200_WhenAuthenticated() throws Exception {
        Category category = TestDataFactory.createCategory(1L, "Tech", "tech", "Technology");
        when(categoryService.create(any(CategoryRequest.class))).thenReturn(category);

        mockMvc.perform(post("/api/admin/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest("Tech", "tech", "Technology"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tech"))
                .andExpect(jsonPath("$.slug").value("tech"))
                .andExpect(jsonPath("$.description").value("Technology"));
    }

    @Test
    void create_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/admin/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest("Tech", "tech", "Technology"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void update_Returns200_WhenAuthenticated() throws Exception {
        Category category = TestDataFactory.createCategory(1L, "Updated", "updated", "Updated desc");
        when(categoryService.update(eq(1L), any(CategoryRequest.class))).thenReturn(category);

        mockMvc.perform(put("/api/admin/categories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest("Updated", "updated", "Updated desc"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void update_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(put("/api/admin/categories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest("Tech", "tech", "Tech"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void delete_Returns204_WhenAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/admin/categories/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1L);
    }

    @Test
    void delete_Returns401_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/admin/categories/1")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
