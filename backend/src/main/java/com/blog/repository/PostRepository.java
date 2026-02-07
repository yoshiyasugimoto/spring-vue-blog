package com.blog.repository;

import com.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.author LEFT JOIN FETCH p.tags WHERE p.status = 'PUBLISHED' ORDER BY p.publishedAt DESC")
    Page<Post> findPublished(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.author LEFT JOIN FETCH p.tags WHERE p.slug = :slug AND p.status = 'PUBLISHED'")
    Optional<Post> findPublishedBySlug(String slug);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.author LEFT JOIN FETCH p.tags ORDER BY p.updatedAt DESC")
    Page<Post> findAllWithDetails(Pageable pageable);

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
