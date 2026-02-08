package com.blog.service;

import com.blog.dto.CategoryRequest;
import com.blog.entity.Category;
import com.blog.exception.ResourceNotFoundException;
import com.blog.repository.CategoryRepository;
import com.blog.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Transactional
    public Category create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(SlugUtil.generate(request.name(), request.slug(), categoryRepository::existsBySlug));
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category category = findById(id);
        category.setName(request.name());
        if (request.slug() != null && !request.slug().isBlank()) {
            category.setSlug(request.slug());
        }
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
