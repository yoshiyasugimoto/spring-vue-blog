package com.blog.service;

import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Captor
    private ArgumentCaptor<Tag> tagCaptor;

    @Test
    void findAll_ReturnsAllTags() {
        Tag tag1 = TestDataFactory.createTag(1L, "Java", "java");
        Tag tag2 = TestDataFactory.createTag(2L, "Spring", "spring");
        when(tagRepository.findAll()).thenReturn(List.of(tag1, tag2));

        List<Tag> result = tagService.findAll();

        assertThat(result).hasSize(2);
        verify(tagRepository).findAll();
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoTags() {
        when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        List<Tag> result = tagService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void create_CreatesTagWithAllFields() {
        TagRequest request = new TagRequest("Java", "java");
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tagService.create(request);

        verify(tagRepository).save(tagCaptor.capture());
        Tag saved = tagCaptor.getValue();
        assertThat(saved.getName()).isEqualTo("Java");
        assertThat(saved.getSlug()).isEqualTo("java");
    }

    @Test
    void create_GeneratesSlugFromName_WhenSlugIsNull() {
        TagRequest request = new TagRequest("Spring Boot", null);
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tagService.create(request);

        verify(tagRepository).save(tagCaptor.capture());
        assertThat(tagCaptor.getValue().getSlug()).isEqualTo("spring-boot");
    }

    @Test
    void create_GeneratesSlugFromName_WhenSlugIsBlank() {
        TagRequest request = new TagRequest("Spring Boot", "  ");
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tagService.create(request);

        verify(tagRepository).save(tagCaptor.capture());
        assertThat(tagCaptor.getValue().getSlug()).isEqualTo("spring-boot");
    }

    @Test
    void create_UsesExplicitSlug_WhenProvided() {
        TagRequest request = new TagRequest("Java", "custom-slug");
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tagService.create(request);

        verify(tagRepository).save(tagCaptor.capture());
        assertThat(tagCaptor.getValue().getSlug()).isEqualTo("custom-slug");
    }

    @Test
    void delete_CallsRepositoryDeleteById() {
        tagService.delete(1L);

        verify(tagRepository).deleteById(1L);
    }
}
