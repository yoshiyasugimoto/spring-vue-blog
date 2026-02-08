package com.blog.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlugUtilTest {

    @Test
    void generateFromTitle() {
        String slug = SlugUtil.generate("Hello World", null, s -> false);
        assertEquals("hello-world", slug);
    }

    @Test
    void generateFromTitleWithJapanese() {
        String slug = SlugUtil.generate("テスト記事", null, s -> false);
        assertEquals("テスト記事", slug);
    }

    @Test
    void useProvidedSlug() {
        String slug = SlugUtil.generate("Title", "custom-slug", s -> false);
        assertEquals("custom-slug", slug);
    }

    @Test
    void appendTimestampOnConflict() {
        String slug = SlugUtil.generate("Hello World", null, s -> s.equals("hello-world"));
        assertTrue(slug.startsWith("hello-world-"));
        assertNotEquals("hello-world", slug);
    }

    @Test
    void trimLeadingAndTrailingHyphens() {
        String slug = SlugUtil.generate("  Hello  ", null, s -> false);
        assertEquals("hello", slug);
    }
}
