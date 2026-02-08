package com.blog.dto;

import com.blog.entity.Category;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryResponseTest {

    @Test
    void from_MapsAllFieldsCorrectly() {
        Category category = TestDataFactory.createCategory(1L, "Tech", "tech", "Technology articles");

        CategoryResponse response = CategoryResponse.from(category);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Tech");
        assertThat(response.slug()).isEqualTo("tech");
        assertThat(response.description()).isEqualTo("Technology articles");
    }

    @Test
    void from_HandlesNullDescription() {
        Category category = TestDataFactory.createCategory(1L, "Tech", "tech", null);

        CategoryResponse response = CategoryResponse.from(category);

        assertThat(response.description()).isNull();
    }
}
