package com.blog.service;

import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Transactional
    public Tag create(TagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.name());
        String slug = request.slug();
        if (slug == null || slug.isBlank()) {
            slug = request.name()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9\\u3040-\\u9faf]+", "-")
                    .replaceAll("^-|-$", "");
        }
        tag.setSlug(slug);
        return tagRepository.save(tag);
    }

    @Transactional
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}
