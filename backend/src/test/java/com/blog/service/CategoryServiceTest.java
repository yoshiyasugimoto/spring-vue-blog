package com.blog.service;

import com.blog.dto.CategoryRequest;
import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @Test
    void findAll_ReturnsAllCategories() {
        Category cat1 = TestDataFactory.createCategory(1L, "Tech", "tech", "Technology");
        Category cat2 = TestDataFactory.createCategory(2L, "Life", "life", "Lifestyle");
        Category cat3 = TestDataFactory.createCategory(3L, "News", "news", "Latest news");
        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2, cat3));

        List<Category> result = categoryService.findAll();

        assertThat(result).hasSize(3);
        verify(categoryRepository).findAll();
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findById_ReturnsCategory_WhenExists() {
        Category category = TestDataFactory.createCategory(1L, "Tech", "tech", "Technology");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Tech");
        assertThat(result.getSlug()).isEqualTo("tech");
    }

    @Test
    void findById_ThrowsException_WhenNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Category not found: 1");
    }

    @Test
    void create_CreatesCategoryWithAllFields() {
        CategoryRequest request = new CategoryRequest("Tech", "tech", "Technology");
        when(categoryRepository.existsBySlug("tech")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        categoryService.create(request);

        verify(categoryRepository).save(categoryCaptor.capture());
        Category saved = categoryCaptor.getValue();
        assertThat(saved.getName()).isEqualTo("Tech");
        assertThat(saved.getSlug()).isEqualTo("tech");
        assertThat(saved.getDescription()).isEqualTo("Technology");
    }

    @Test
    void create_GeneratesSlugFromName_WhenSlugIsNull() {
        CategoryRequest request = new CategoryRequest("Spring Boot", null, "Description");
        when(categoryRepository.existsBySlug("spring-boot")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        categoryService.create(request);

        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getValue().getSlug()).isEqualTo("spring-boot");
    }

    @Test
    void create_GeneratesSlugFromName_WhenSlugIsBlank() {
        CategoryRequest request = new CategoryRequest("Spring Boot", "  ", "Description");
        when(categoryRepository.existsBySlug("spring-boot")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        categoryService.create(request);

        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getValue().getSlug()).isEqualTo("spring-boot");
    }

    @Test
    void create_UsesExplicitSlug_WhenProvided() {
        CategoryRequest request = new CategoryRequest("Tech", "custom-slug", "Description");
        when(categoryRepository.existsBySlug("custom-slug")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        categoryService.create(request);

        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getValue().getSlug()).isEqualTo("custom-slug");
    }

    @Test
    void create_AppendsTimestamp_WhenSlugCollides() {
        CategoryRequest request = new CategoryRequest("Tech", "tech", "Description");
        when(categoryRepository.existsBySlug("tech")).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        categoryService.create(request);

        verify(categoryRepository).save(categoryCaptor.capture());
        String slug = categoryCaptor.getValue().getSlug();
        assertThat(slug).startsWith("tech-");
        assertThat(slug.length()).isGreaterThan("tech-".length());
    }

    @Test
    void update_UpdatesAllFields() {
        Category existing = TestDataFactory.createCategory(1L, "Old", "old", "Old desc");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryRequest request = new CategoryRequest("New", "new-slug", "New desc");
        categoryService.update(1L, request);

        verify(categoryRepository).save(categoryCaptor.capture());
        Category saved = categoryCaptor.getValue();
        assertThat(saved.getName()).isEqualTo("New");
        assertThat(saved.getSlug()).isEqualTo("new-slug");
        assertThat(saved.getDescription()).isEqualTo("New desc");
    }

    @Test
    void update_KeepsOriginalSlug_WhenSlugIsNull() {
        Category existing = TestDataFactory.createCategory(1L, "Tech", "original-slug", "Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryRequest request = new CategoryRequest("Updated", null, "Updated desc");
        categoryService.update(1L, request);

        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getValue().getSlug()).isEqualTo("original-slug");
    }

    @Test
    void update_KeepsOriginalSlug_WhenSlugIsBlank() {
        Category existing = TestDataFactory.createCategory(1L, "Tech", "original-slug", "Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryRequest request = new CategoryRequest("Updated", "  ", "Updated desc");
        categoryService.update(1L, request);

        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryCaptor.getValue().getSlug()).isEqualTo("original-slug");
    }

    @Test
    void update_ThrowsException_WhenCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(99L, new CategoryRequest("New", null, null)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Category not found: 99");
    }

    @Test
    void delete_CallsRepositoryDeleteById() {
        categoryService.delete(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
