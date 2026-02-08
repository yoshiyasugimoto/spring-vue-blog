package com.blog.service;

import com.blog.dto.PostRequest;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Captor
    private ArgumentCaptor<Post> postCaptor;

    // --- findPublished ---

    @Test
    void findPublished_ReturnsPageOfPosts() {
        Post post1 = TestDataFactory.createPost();
        Post post2 = TestDataFactory.createPost(2L, "Post 2", "post-2", "PUBLISHED",
                TestDataFactory.createUser(), TestDataFactory.createCategory(), new HashSet<>());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> page = new PageImpl<>(List.of(post1, post2), pageable, 2);
        when(postRepository.findPublished(pageable)).thenReturn(page);

        Page<Post> result = postService.findPublished(pageable);

        assertThat(result.getContent()).hasSize(2);
        verify(postRepository).findPublished(pageable);
    }

    @Test
    void findPublished_ReturnsEmptyPage_WhenNoPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(postRepository.findPublished(pageable)).thenReturn(emptyPage);

        Page<Post> result = postService.findPublished(pageable);

        assertThat(result.getTotalElements()).isZero();
    }

    // --- findPublishedBySlug ---

    @Test
    void findPublishedBySlug_ReturnsPost_WhenExists() {
        Post post = TestDataFactory.createPost();
        when(postRepository.findPublishedBySlug("test-post")).thenReturn(Optional.of(post));

        Post result = postService.findPublishedBySlug("test-post");

        assertThat(result.getSlug()).isEqualTo("test-post");
        verify(postRepository).findPublishedBySlug("test-post");
    }

    @Test
    void findPublishedBySlug_ThrowsException_WhenNotFound() {
        when(postRepository.findPublishedBySlug("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.findPublishedBySlug("nonexistent"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found: nonexistent");
    }

    // --- findAll ---

    @Test
    void findAll_ReturnsPageOfAllPosts() {
        Post post = TestDataFactory.createPost();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> page = new PageImpl<>(List.of(post), pageable, 1);
        when(postRepository.findAllWithDetails(pageable)).thenReturn(page);

        Page<Post> result = postService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(postRepository).findAllWithDetails(pageable);
    }

    // --- findById ---

    @Test
    void findById_ReturnsPost_WhenExists() {
        Post post = TestDataFactory.createPost();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_ThrowsException_WhenNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found: 99");
    }

    // --- create ---

    @Test
    void create_CreatesPostWithAllFields() {
        User author = TestDataFactory.createUser();
        Category category = TestDataFactory.createCategory();
        Tag tag = TestDataFactory.createTag();
        PostRequest request = new PostRequest("My Post", "my-post", "Content", "Excerpt",
                "https://example.com/img.jpg", "DRAFT", 1L, Set.of(1L));

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(tagRepository.findByIdIn(Set.of(1L))).thenReturn(Set.of(tag));
        when(postRepository.existsBySlug("my-post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        Post saved = postCaptor.getValue();
        assertThat(saved.getTitle()).isEqualTo("My Post");
        assertThat(saved.getSlug()).isEqualTo("my-post");
        assertThat(saved.getContent()).isEqualTo("Content");
        assertThat(saved.getExcerpt()).isEqualTo("Excerpt");
        assertThat(saved.getCoverImage()).isEqualTo("https://example.com/img.jpg");
        assertThat(saved.getStatus()).isEqualTo("DRAFT");
        assertThat(saved.getAuthor()).isEqualTo(author);
        assertThat(saved.getCategory()).isEqualTo(category);
        assertThat(saved.getTags()).containsExactly(tag);
    }

    @Test
    void create_SetsPublishedAtWhenStatusIsPublished() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Post", "post", "Content", null, null, "PUBLISHED", null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getPublishedAt()).isNotNull();
    }

    @Test
    void create_DoesNotSetPublishedAtWhenStatusIsDraft() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Post", "post", "Content", null, null, "DRAFT", null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getPublishedAt()).isNull();
    }

    @Test
    void create_GeneratesSlugFromTitle_WhenSlugIsNull() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Hello World", null, "Content", null, null, null, null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("hello-world")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getSlug()).isEqualTo("hello-world");
    }

    @Test
    void create_GeneratesSlugFromTitle_WhenSlugIsBlank() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("My Post", "  ", "Content", null, null, null, null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("my-post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getSlug()).isEqualTo("my-post");
    }

    @Test
    void create_UsesExplicitSlug_WhenProvided() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Title", "custom-slug", "Content", null, null, null, null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("custom-slug")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getSlug()).isEqualTo("custom-slug");
    }

    @Test
    void create_AppendsTimestamp_WhenSlugCollides() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Hello World", null, "Content", null, null, null, null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("hello-world")).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(postRepository).save(postCaptor.capture());
        String slug = postCaptor.getValue().getSlug();
        assertThat(slug).startsWith("hello-world-");
        assertThat(slug.length()).isGreaterThan("hello-world-".length());
    }

    @Test
    void create_HandlesNullCategoryId() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Post", "post", "Content", null, null, null, null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(categoryRepository, never()).findById(any());
        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getCategory()).isNull();
    }

    @Test
    void create_HandlesNullTagIds() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Post", "post", "Content", null, null, null, null, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(tagRepository, never()).findByIdIn(any());
    }

    @Test
    void create_HandlesEmptyTagIds() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Post", "post", "Content", null, null, null, null, Set.of());

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("post")).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.create(request, "admin");

        verify(tagRepository, never()).findByIdIn(any());
    }

    @Test
    void create_ThrowsException_WhenUserNotFound() {
        PostRequest request = TestDataFactory.createPostRequest();
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.create(request, "unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void create_ThrowsException_WhenCategoryNotFound() {
        User author = TestDataFactory.createUser();
        PostRequest request = new PostRequest("Post", "post", "Content", null, null, null, 99L, null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug("post")).thenReturn(false);
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.create(request, "admin"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Category not found");
    }

    // --- update ---

    @Test
    void update_UpdatesAllFields() {
        Post existing = TestDataFactory.createPost(1L, "Old Title", "old-slug", "DRAFT",
                TestDataFactory.createUser(), TestDataFactory.createCategory(), new HashSet<>());
        Category newCategory = TestDataFactory.createCategory(2L, "New Cat", "new-cat", "New");
        Tag newTag = TestDataFactory.createTag(2L, "Spring", "spring");
        PostRequest request = new PostRequest("New Title", "new-slug", "New Content", "New Excerpt",
                "https://new-image.com", "DRAFT", 2L, Set.of(2L));

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(tagRepository.findByIdIn(Set.of(2L))).thenReturn(Set.of(newTag));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        Post saved = postCaptor.getValue();
        assertThat(saved.getTitle()).isEqualTo("New Title");
        assertThat(saved.getSlug()).isEqualTo("new-slug");
        assertThat(saved.getContent()).isEqualTo("New Content");
        assertThat(saved.getExcerpt()).isEqualTo("New Excerpt");
        assertThat(saved.getCoverImage()).isEqualTo("https://new-image.com");
        assertThat(saved.getCategory()).isEqualTo(newCategory);
        assertThat(saved.getTags()).containsExactly(newTag);
    }

    @Test
    void update_KeepsOriginalSlug_WhenSlugIsNull() {
        Post existing = TestDataFactory.createPost(1L, "Title", "original-slug", "DRAFT",
                TestDataFactory.createUser(), null, new HashSet<>());
        PostRequest request = new PostRequest("Updated", null, "Content", null, null, "DRAFT", null, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getSlug()).isEqualTo("original-slug");
    }

    @Test
    void update_KeepsOriginalSlug_WhenSlugIsBlank() {
        Post existing = TestDataFactory.createPost(1L, "Title", "original-slug", "DRAFT",
                TestDataFactory.createUser(), null, new HashSet<>());
        PostRequest request = new PostRequest("Updated", "  ", "Content", null, null, "DRAFT", null, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getSlug()).isEqualTo("original-slug");
    }

    @Test
    void update_SetsPublishedAt_WhenTransitioningToPublished() {
        Post existing = TestDataFactory.createPost(1L, "Title", "slug", "DRAFT",
                TestDataFactory.createUser(), null, new HashSet<>());
        PostRequest request = new PostRequest("Title", "slug", "Content", null, null, "PUBLISHED", null, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getPublishedAt()).isNotNull();
    }

    @Test
    void update_DoesNotOverwritePublishedAt_WhenAlreadyPublished() {
        LocalDateTime originalPublishedAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        Post existing = TestDataFactory.createPost(1L, "Title", "slug", "PUBLISHED",
                TestDataFactory.createUser(), null, new HashSet<>());
        existing.setPublishedAt(originalPublishedAt);
        PostRequest request = new PostRequest("Updated", "slug", "Content", null, null, "PUBLISHED", null, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getPublishedAt()).isEqualTo(originalPublishedAt);
    }

    @Test
    void update_ClearsCategory_WhenCategoryIdIsNull() {
        Post existing = TestDataFactory.createPost(1L, "Title", "slug", "DRAFT",
                TestDataFactory.createUser(), TestDataFactory.createCategory(), new HashSet<>());
        PostRequest request = new PostRequest("Title", "slug", "Content", null, null, "DRAFT", null, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getCategory()).isNull();
    }

    @Test
    void update_ClearsTags_WhenTagIdsIsEmptySet() {
        Post existing = TestDataFactory.createPost();
        PostRequest request = new PostRequest("Title", "slug", "Content", null, null, "DRAFT", null, Set.of());

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.update(1L, request);

        verify(postRepository).save(postCaptor.capture());
        assertThat(postCaptor.getValue().getTags()).isEmpty();
    }

    @Test
    void update_ThrowsException_WhenPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.update(99L, TestDataFactory.createPostRequest()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found: 99");
    }

    @Test
    void update_ThrowsException_WhenCategoryNotFound() {
        Post existing = TestDataFactory.createPost();
        PostRequest request = new PostRequest("Title", "slug", "Content", null, null, "DRAFT", 99L, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.update(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Category not found");
    }

    // --- delete ---

    @Test
    void delete_CallsRepositoryDeleteById() {
        postService.delete(1L);

        verify(postRepository).deleteById(1L);
    }
}
