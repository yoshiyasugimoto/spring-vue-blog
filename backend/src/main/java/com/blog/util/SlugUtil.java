package com.blog.util;

import java.util.function.Predicate;

public final class SlugUtil {

    private SlugUtil() {}

    public static String generate(String source, String providedSlug, Predicate<String> existsCheck) {
        String slug = providedSlug;
        if (slug == null || slug.isBlank()) {
            slug = source
                    .toLowerCase()
                    .replaceAll("[^a-z0-9\\u3040-\\u9faf]+", "-")
                    .replaceAll("^-|-$", "");
        }
        if (existsCheck.test(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }
        return slug;
    }
}
