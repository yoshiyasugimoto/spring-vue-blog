package com.blog.service;

import com.blog.dto.PostRequest;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.PostStatus;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import com.blog.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public Page<Post> findPublished(Pageable pageable) {
        return postRepository.findPublished(pageable);
    }

    public Post findPublishedBySlug(String slug) {
        return postRepository.findPublishedBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post", slug));
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllWithDetails(pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
    }

    @Transactional
    public Post create(PostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        Post post = new Post();
        post.setTitle(request.title());
        post.setSlug(SlugUtil.generate(request.title(), request.slug(), postRepository::existsBySlug));
        post.setContent(request.content());
        post.setExcerpt(request.excerpt());
        post.setCoverImage(request.coverImage());
        post.setStatus(request.status());
        post.setAuthor(author);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
            post.setCategory(category);
        }

        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            Set<Tag> tags = tagRepository.findByIdIn(request.tagIds());
            post.setTags(tags);
        }

        if (request.status() == PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }

        return postRepository.save(post);
    }

    @Transactional
    public Post update(Long id, PostRequest request) {
        Post post = findById(id);

        post.setTitle(request.title());
        if (request.slug() != null && !request.slug().isBlank()) {
            post.setSlug(request.slug());
        }
        post.setContent(request.content());
        post.setExcerpt(request.excerpt());
        post.setCoverImage(request.coverImage());

        if (request.status() == PostStatus.PUBLISHED && post.getStatus() != PostStatus.PUBLISHED) {
            post.setPublishedAt(LocalDateTime.now());
        }
        post.setStatus(request.status());

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (request.tagIds() != null) {
            Set<Tag> tags = request.tagIds().isEmpty()
                    ? new HashSet<>()
                    : tagRepository.findByIdIn(request.tagIds());
            post.setTags(tags);
        }

        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
