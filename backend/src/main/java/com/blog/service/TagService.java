package com.blog.service;

import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import com.blog.util.SlugUtil;
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
        tag.setSlug(SlugUtil.generate(request.name(), request.slug(), s -> tagRepository.findBySlug(s).isPresent()));
        return tagRepository.save(tag);
    }

    @Transactional
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}
