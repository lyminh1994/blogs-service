package com.minhlq.blogsservice.annotation.validation.validator;

import com.minhlq.blogsservice.annotation.validation.DuplicatedArticleConstraint;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.util.ArticleUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

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

  private final ArticleRepository articleRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !articleRepository.findBySlug(ArticleUtils.toSlug(value)).isPresent();
  }
}
