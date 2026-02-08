package com.blog.dto;

import com.blog.entity.Tag;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagResponseTest {

    @Test
    void from_MapsAllFieldsCorrectly() {
        Tag tag = TestDataFactory.createTag(1L, "Java", "java");

        TagResponse response = TagResponse.from(tag);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Java");
        assertThat(response.slug()).isEqualTo("java");
    }
}
