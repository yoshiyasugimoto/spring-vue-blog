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
                .orElseThrow(() -> new RuntimeException("Post not found: " + slug));
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllWithDetails(pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found: " + id));
    }

    @Transactional
    public Post create(PostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSlug(generateSlug(request));
        post.setContent(request.getContent());
        post.setExcerpt(request.getExcerpt());
        post.setCoverImage(request.getCoverImage());
        post.setStatus(request.getStatus());
        post.setAuthor(author);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        }

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = tagRepository.findByIdIn(request.getTagIds());
            post.setTags(tags);
        }

        if ("PUBLISHED".equals(request.getStatus())) {
            post.setPublishedAt(LocalDateTime.now());
        }

        return postRepository.save(post);
    }

    @Transactional
    public Post update(Long id, PostRequest request) {
        Post post = findById(id);

        post.setTitle(request.getTitle());
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            post.setSlug(request.getSlug());
        }
        post.setContent(request.getContent());
        post.setExcerpt(request.getExcerpt());
        post.setCoverImage(request.getCoverImage());

        if ("PUBLISHED".equals(request.getStatus()) && !"PUBLISHED".equals(post.getStatus())) {
            post.setPublishedAt(LocalDateTime.now());
        }
        post.setStatus(request.getStatus());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().isEmpty()
                    ? new HashSet<>()
                    : tagRepository.findByIdIn(request.getTagIds());
            post.setTags(tags);
        }

        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    private String generateSlug(PostRequest request) {
        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = request.getTitle()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9\\u3040-\\u9faf]+", "-")
                    .replaceAll("^-|-$", "");
        }
        if (postRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }
        return slug;
    }
}
