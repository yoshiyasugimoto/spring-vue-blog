package com.blog.controller;

import com.blog.dto.PostRequest;
import com.blog.dto.PostResponse;
import com.blog.entity.Post;
import com.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ResponseEntity<Page<PostResponse>> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.findPublished(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt")));
        return ResponseEntity.ok(posts.map(PostResponse::from));
    }

    @GetMapping("/api/posts/{slug}")
    public ResponseEntity<PostResponse> getBySlug(@PathVariable String slug) {
        Post post = postService.findPublishedBySlug(slug);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @GetMapping("/api/admin/posts/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        Post post = postService.findById(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @GetMapping("/api/admin/posts")
    public ResponseEntity<Page<PostResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt")));
        return ResponseEntity.ok(posts.map(PostResponse::from));
    }

    @PostMapping("/api/admin/posts")
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.create(request, userDetails.getUsername());
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @PutMapping("/api/admin/posts/{id}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request) {
        Post post = postService.update(id, request);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
