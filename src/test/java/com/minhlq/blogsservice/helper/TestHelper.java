package com.minhlq.blogsservice.helper;

import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.entity.ArticleEntity;
import com.minhlq.blogsservice.entity.UserEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestHelper {

  public ArticleResponse articleResponseFixture(String seed, UserEntity user) {
    Date now = new Date();
    return new ArticleResponse(
        1L,
        new ProfileResponse(user.getUsername(), user.getBio(), user.getImage(), false),
        "title-" + seed,
        "title " + seed,
        "desc " + seed,
        "body " + seed,
        now,
        now,
        false,
        0L,
        new ArrayList<>());
  }

  public ArticleResponse getArticleResponseFromArticleAndUser(
      ArticleEntity article, UserEntity user) {
    return new ArticleResponse(
        article.getId(),
        new ProfileResponse(user.getUsername(), user.getBio(), user.getImage(), false),
        article.getSlug(),
        article.getTitle(),
        article.getDescription(),
        article.getBody(),
        Date.from(article.getCreatedAt()),
        Date.from(article.getUpdatedAt()),
        false,
        0,
        Collections.singletonList("joda"));
  }
}
