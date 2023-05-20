package com.minhlq.blogs.annotation.validator;

import com.minhlq.blogs.annotation.DuplicatedArticleConstraint;
import com.minhlq.blogs.service.ArticleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * The implement for DuplicatedArticleConstraint annotation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
public class DuplicatedArticleValidator
    implements ConstraintValidator<DuplicatedArticleConstraint, String> {

  private final ArticleService articleService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isBlank(value) || !articleService.isSlugExited(value);
  }
}
