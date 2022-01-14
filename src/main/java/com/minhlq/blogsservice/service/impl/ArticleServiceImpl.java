package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.ArticleResponse;
import com.minhlq.blogsservice.dto.PagingResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.exception.NoAuthorizationException;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.ArticleMapper;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.Article;
import com.minhlq.blogsservice.model.ArticleFavorite;
import com.minhlq.blogsservice.model.ArticleTag;
import com.minhlq.blogsservice.model.QArticle;
import com.minhlq.blogsservice.model.Tag;
import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteId;
import com.minhlq.blogsservice.model.unionkey.ArticleTagId;
import com.minhlq.blogsservice.repository.ArticleFavoriteRepository;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.ArticleTagRepository;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.ArticleService;
import com.minhlq.blogsservice.util.ArticleUtils;
import com.querydsl.core.BooleanBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  private final ArticleTagRepository articleTagRepository;

  private final ArticleFavoriteRepository articleFavoriteRepository;

  private final TagRepository tagRepository;

  private final FollowRepository followRepository;

  @Override
  public ArticleResponse createArticle(NewArticleRequest createRequest, UserPrincipal user) {
    User author = UserMapper.MAPPER.toUser(user);
    Article article =
        articleRepository.saveAndFlush(
            Article.builder()
                .author(author)
                .slug(ArticleUtils.toSlug(createRequest.getTitle()))
                .title(createRequest.getTitle())
                .description(createRequest.getDescription())
                .body(createRequest.getBody())
                .build());

    List<String> tagNames = createRequest.getTagNames();
    if (!tagNames.isEmpty()) {
      List<ArticleTag> articleTags =
          tagNames.stream()
              .map(
                  tagName -> {
                    Tag tag =
                        tagRepository
                            .findByName(tagName)
                            .orElseGet(() -> tagRepository.saveAndFlush(new Tag(tagName)));

                    ArticleTagId articleTagId = new ArticleTagId(article.getId(), tag.getId());
                    return new ArticleTag(articleTagId);
                  })
              .collect(Collectors.toList());

      articleTagRepository.saveAll(articleTags);
    }

    return ArticleMapper.MAPPER.toArticleResponse(article, tagNames);
  }

  @Override
  public PagingResponse<ArticleResponse> findUserFeed(
      UserPrincipal currentUser, PageRequest pageRequest) {
    Set<Long> followedUsers = followRepository.findFollowedUsers(currentUser.getId());
    if (followedUsers.isEmpty()) {
      return new PagingResponse<>(Collections.emptyList(), 0);
    }

    Page<Article> articles = articleRepository.findByFollowedUsers(followedUsers, pageRequest);
    List<ArticleResponse> responses = getArticleResponses(articles.getContent(), currentUser);

    return new PagingResponse<>(responses, articles.getTotalElements());
  }

  @Override
  public PagingResponse<ArticleResponse> findRecentArticles(
      String tag,
      String favoriteBy,
      String author,
      PageRequest pageRequest,
      UserPrincipal currentUser) {
    QArticle article = QArticle.article;
    BooleanBuilder where = new BooleanBuilder();
    if (StringUtils.isNotBlank(tag)) {
      where.and(article.tags.any().name.eq(tag));
    }
    if (StringUtils.isNotBlank(author)) {
      where.and(article.author.username.eq(author));
    }
    if (StringUtils.isNotBlank(favoriteBy)) {
      where.and(article.favorites.any().username.eq(favoriteBy));
    }

    Page<Article> articles = articleRepository.findAll(where, pageRequest);
    List<ArticleResponse> responses = getArticleResponses(articles.getContent(), currentUser);

    return new PagingResponse<>(responses, articles.getTotalElements());
  }

  @Override
  public ArticleResponse findBySlug(String slug, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return getArticleResponse(currentUser, article);
  }

  @Override
  public ArticleResponse updateArticle(
      String slug, UpdateArticleRequest updateRequest, UserPrincipal currentUser) {
    Article article =
        articleRepository
            .findBySlug(slug)
            .map(
                mapper -> {
                  if (!currentUser.getId().equals(mapper.getAuthor().getId())) {
                    throw new NoAuthorizationException();
                  }

                  mapper.setSlug(ArticleUtils.toSlug(updateRequest.getTitle()));
                  mapper.setTitle(updateRequest.getTitle());
                  mapper.setDescription(updateRequest.getDescription());
                  mapper.setBody(updateRequest.getBody());
                  return articleRepository.save(mapper);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return findBySlug(article.getSlug(), currentUser);
  }

  @Override
  public void deleteArticle(String slug, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.getId().equals(article.getAuthor().getId())) {
      throw new NoAuthorizationException();
    }

    articleRepository.delete(article);
  }

  @Override
  public ArticleResponse favoriteArticle(String slug, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteId articleFavoriteId =
        new ArticleFavoriteId(article.getId(), currentUser.getId());
    if (articleFavoriteRepository.findById(articleFavoriteId).isEmpty()) {
      articleFavoriteRepository.save(new ArticleFavorite(articleFavoriteId));
    }

    return findBySlug(slug, currentUser);
  }

  @Override
  public ArticleResponse unFavoriteArticle(String slug, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteId articleFavoriteId =
        new ArticleFavoriteId(article.getId(), currentUser.getId());
    articleFavoriteRepository
        .findById(articleFavoriteId)
        .ifPresent(articleFavoriteRepository::delete);
    return findBySlug(slug, currentUser);
  }

  private ArticleResponse getArticleResponse(UserPrincipal currentUser, Article article) {
    ArticleResponse articleResponse = ArticleMapper.MAPPER.toArticleResponse(article);
    articleResponse.setFavoritesCount(article.getFavorites().size());
    if (currentUser != null) {
      articleResponse
          .getAuthor()
          .setFollowing(
              article.getAuthor().getFollows().stream()
                  .anyMatch(follow -> follow.getId().equals(currentUser.getId())));
      articleResponse.setFavorite(
          article.getFavorites().stream()
              .anyMatch(favorite -> favorite.getId().equals(currentUser.getId())));
    }
    return articleResponse;
  }

  private List<ArticleResponse> getArticleResponses(
      List<Article> articles, UserPrincipal currentUser) {
    return articles.stream()
        .map(article -> getArticleResponse(currentUser, article))
        .collect(Collectors.toList());
  }
}
