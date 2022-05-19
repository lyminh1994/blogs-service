package com.minhlq.blogsservice.validation.validator;

import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.util.ArticleUtils;
import com.minhlq.blogsservice.validation.DuplicatedArticleConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DuplicatedArticleValidator
    implements ConstraintValidator<DuplicatedArticleConstraint, String> {

  private final ArticleRepository articleRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return articleRepository.findBySlug(ArticleUtils.toSlug(value)).isEmpty();
  }
}
