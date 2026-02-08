package com.blog.service;

import com.blog.dto.PostRequest;
import com.blog.entity.Post;
import com.blog.entity.PostStatus;
import com.blog.entity.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void findByIdReturnsPost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test");
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.findById(1L);
        assertEquals("Test", result.getTitle());
    }

    @Test
    void findByIdThrowsWhenNotFound() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.findById(999L));
    }

    @Test
    void createPostSetsPublishedAtWhenPublished() {
        User author = new User();
        author.setUsername("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug(any())).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        PostRequest request = new PostRequest(
                "Title", null, "Content", null, null,
                PostStatus.PUBLISHED, null, Set.of()
        );

        Post result = postService.create(request, "admin");
        assertEquals(PostStatus.PUBLISHED, result.getStatus());
        assertNotNull(result.getPublishedAt());
    }

    @Test
    void createPostDraftHasNoPublishedAt() {
        User author = new User();
        author.setUsername("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(author));
        when(postRepository.existsBySlug(any())).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        PostRequest request = new PostRequest(
                "Title", null, "Content", null, null,
                PostStatus.DRAFT, null, Set.of()
        );

        Post result = postService.create(request, "admin");
        assertEquals(PostStatus.DRAFT, result.getStatus());
        assertNull(result.getPublishedAt());
    }

    @Test
    void deleteCallsRepository() {
        postService.delete(1L);
        verify(postRepository).deleteById(1L);
    }
}
