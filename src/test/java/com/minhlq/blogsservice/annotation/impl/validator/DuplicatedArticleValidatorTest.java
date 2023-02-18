package com.minhlq.blogsservice.annotation.impl.validator;

import com.minhlq.blogsservice.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DuplicatedArticleValidatorTest {

  @Mock ArticleService articleService;

  @Test
  void givenBlankTitle_whenValidate_thenReturnTrue() {
    DuplicatedArticleValidator validator = new DuplicatedArticleValidator(articleService);
    boolean valid = validator.isValid("", null);

    assertTrue(valid);
  }

  @Test
  void givenExistedTitle_whenValidate_thenReturnFalse() {
    DuplicatedArticleValidator validator = new DuplicatedArticleValidator(articleService);
    given(articleService.isSlugExited("slug")).willReturn(true);
    boolean valid = validator.isValid("slug", null);

    assertFalse(valid);
  }

  @Test
  void givenTitleNotBlankAndNotExisted_whenValidate_thenReturnTrue() {
    DuplicatedArticleValidator validator = new DuplicatedArticleValidator(articleService);
    given(articleService.isSlugExited("slug")).willReturn(false);
    boolean valid = validator.isValid("slug", null);

    assertTrue(valid);
  }
}
