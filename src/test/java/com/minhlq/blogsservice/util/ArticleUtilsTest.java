package com.minhlq.blogsservice.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArticleUtilsTest {

    @Test
    void givenTitle_whenCallingToSlug_thenReturnSlug() {
        String actual = ArticleUtils.toSlug("Title 1");

        Assertions.assertEquals("title-1", actual);
    }
}
