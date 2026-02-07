package com.blog.controller;

import com.blog.dto.TagRequest;
import com.blog.dto.TagResponse;
import com.blog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public ResponseEntity<List<TagResponse>> getAll() {
        List<TagResponse> tags = tagService.findAll().stream()
                .map(TagResponse::from)
                .toList();
        return ResponseEntity.ok(tags);
    }

    @PostMapping("/api/admin/tags")
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(TagResponse.from(tagService.create(request)));
    }

    @DeleteMapping("/api/admin/tags/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
