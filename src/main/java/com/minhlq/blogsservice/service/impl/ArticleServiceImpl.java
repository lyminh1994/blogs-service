package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.mapper.ArticleMapper;
import com.minhlq.blogsservice.dto.mapper.UserMapper;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.exception.NoAuthorizationException;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.ArticleEntity;
import com.minhlq.blogsservice.model.ArticleFavoriteEntity;
import com.minhlq.blogsservice.model.ArticleTagEntity;
import com.minhlq.blogsservice.model.QArticleEntity;
import com.minhlq.blogsservice.model.QArticleFavoriteEntity;
import com.minhlq.blogsservice.model.QArticleTagEntity;
import com.minhlq.blogsservice.model.QTagEntity;
import com.minhlq.blogsservice.model.QUserEntity;
import com.minhlq.blogsservice.model.TagEntity;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteKey;
import com.minhlq.blogsservice.model.unionkey.ArticleTagKey;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.ArticleFavoriteRepository;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.ArticleTagRepository;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.ArticleService;
import com.minhlq.blogsservice.util.ArticleUtils;
import com.minhlq.blogsservice.util.SecurityUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The article service implementation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {

  private final JPAQueryFactory queryFactory;

  private final ArticleRepository articleRepository;

  private final ArticleTagRepository articleTagRepository;

  private final ArticleFavoriteRepository articleFavoriteRepository;

  private final TagRepository tagRepository;

  private final FollowRepository followRepository;

  @Override
  @Transactional
  public ArticleResponse createArticle(NewArticleRequest createRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    UserEntity author = UserMapper.MAPPER.toUser(currentUser);
    ArticleEntity savedArticle =
        articleRepository.saveAndFlush(
            ArticleEntity.builder()
                .author(author)
                .slug(ArticleUtils.toSlug(createRequest.title()))
                .title(createRequest.title())
                .description(createRequest.description())
                .body(createRequest.body())
                .build());

    List<String> tagNames = createRequest.tagNames();
    if (CollectionUtils.isNotEmpty(tagNames)) {
      List<ArticleTagEntity> articleTags =
          tagNames.stream()
              .map(
                  tagName -> {
                    TagEntity savedTag =
                        tagRepository
                            .findByName(tagName)
                            .orElseGet(() -> tagRepository.saveAndFlush(new TagEntity(tagName)));

                    ArticleTagKey articleTagId =
                        new ArticleTagKey(savedArticle.getId(), savedTag.getId());

                    return new ArticleTagEntity(articleTagId);
                  })
              .toList();

      articleTagRepository.saveAll(articleTags);
    }

    return ArticleMapper.MAPPER.toArticleResponse(savedArticle, tagNames);
  }

  @Override
  public PageResponse<ArticleResponse> findUserFeeds(PageRequest pageRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    Set<Long> followedUsers = followRepository.findByUserIdQuery(currentUser.id());
    if (CollectionUtils.isEmpty(followedUsers)) {
      return new PageResponse<>(Collections.emptyList(), 0);
    }

    Page<ArticleEntity> articles =
        articleRepository.findByFollowedUsersQuery(followedUsers, pageRequest);
    List<ArticleResponse> contents = getArticleResponses(articles.getContent());

    return new PageResponse<>(contents, articles.getTotalElements());
  }

  @Override
  public PageResponse<ArticleResponse> findRecentArticles(
      String tagName, String favoriteBy, String author, PageRequest pageRequest) {
    QTagEntity qTag = QTagEntity.tagEntity;
    QArticleEntity qArticle = QArticleEntity.articleEntity;
    QUserEntity qUser = QUserEntity.userEntity;
    QArticleTagEntity qArticleTag = QArticleTagEntity.articleTagEntity;
    QArticleFavoriteEntity qArticleFavorite = QArticleFavoriteEntity.articleFavoriteEntity;

    BooleanBuilder conditions = new BooleanBuilder();
    if (StringUtils.isNotBlank(tagName)) {
      conditions.and(qTag.name.eq(tagName));
    }
    if (StringUtils.isNotBlank(author)) {
      conditions.and(qArticle.author.username.eq(author));
    }
    if (StringUtils.isNotBlank(favoriteBy)) {
      conditions.and(qUser.username.eq(favoriteBy));
    }

    JPAQuery<?> query =
        queryFactory
            .from(qArticle)
            .leftJoin(qArticleTag)
            .on(qArticle.id.eq(qArticleTag.id.articleId))
            .leftJoin(qTag)
            .on(qTag.id.eq(qArticleTag.id.tagId))
            .leftJoin(qArticleFavorite)
            .on(qArticleFavorite.id.articleId.eq(qArticle.id))
            .leftJoin(qUser)
            .on(qUser.id.eq(qArticleFavorite.id.userId))
            .where(conditions);

    long totalElements = query.select(qArticle.countDistinct()).fetchFirst();

    List<ArticleEntity> articles =
        query
            .distinct()
            .select(qArticle)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .fetch();

    List<ArticleResponse> contents = getArticleResponses(articles);

    return new PageResponse<>(contents, totalElements);
  }

  @Override
  public ArticleResponse findBySlug(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return getArticleResponse(currentUser, article);
  }

  @Override
  @Transactional
  public ArticleResponse updateArticle(String slug, UpdateArticleRequest updateRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity newArticle =
        articleRepository
            .findBySlug(slug)
            .map(
                currentArticle -> {
                  if (!currentUser.id().equals(currentArticle.getAuthor().getId())) {
                    throw new NoAuthorizationException();
                  }

                  currentArticle.setSlug(ArticleUtils.toSlug(updateRequest.title()));
                  currentArticle.setTitle(updateRequest.title());
                  currentArticle.setDescription(updateRequest.description());
                  currentArticle.setBody(updateRequest.body());

                  return articleRepository.save(currentArticle);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return getArticleResponse(currentUser, newArticle);
  }

  @Override
  @Transactional
  public void deleteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.id().equals(article.getAuthor().getId())) {
      throw new NoAuthorizationException();
    }

    List<ArticleTagEntity> articleTags = articleTagRepository.findByArticleIdQuery(article.getId());
    articleTagRepository.deleteAll(articleTags);

    List<ArticleFavoriteEntity> articleFavorites =
        articleFavoriteRepository.findByArticleIdQuery(article.getId());
    articleFavoriteRepository.deleteAll(articleFavorites);

    articleRepository.delete(article);
  }

  @Override
  @Transactional
  public ArticleResponse favoriteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteKey articleFavoriteKey =
        new ArticleFavoriteKey(article.getId(), currentUser.id());
    if (articleFavoriteRepository.findById(articleFavoriteKey).isEmpty()) {
      articleFavoriteRepository.save(new ArticleFavoriteEntity(articleFavoriteKey));
    }

    return getArticleResponse(currentUser, article);
  }

  @Override
  @Transactional
  public ArticleResponse unFavoriteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteKey articleFavorite = new ArticleFavoriteKey(article.getId(), currentUser.id());
    articleFavoriteRepository
        .findById(articleFavorite)
        .ifPresent(articleFavoriteRepository::delete);

    return getArticleResponse(currentUser, article);
  }

  @Override
  public boolean isSlugExited(String slug) {
    return articleRepository.findBySlug(ArticleUtils.toSlug(slug)).isPresent();
  }

  /**
   * Add user details and tag name to article.
   *
   * @param currentUser the user
   * @param article the article
   * @return article
   */
  private ArticleResponse getArticleResponse(UserPrincipal currentUser, ArticleEntity article) {
    ArticleResponse result = ArticleMapper.MAPPER.toArticleResponse(article);
    if (currentUser != null) {
      FollowKey followId = new FollowKey(currentUser.id(), article.getAuthor().getId());
      result.getAuthor().setFollowing(followRepository.existsById(followId));

      ArticleFavoriteKey articleFavoriteId =
          new ArticleFavoriteKey(article.getId(), currentUser.id());
      result.setFavorite(articleFavoriteRepository.existsById(articleFavoriteId));
    }

    result.setFavoritesCount(
        articleFavoriteRepository.countArticleFavoritesByArticleIdQuery(article.getId()));

    QTagEntity qTag = QTagEntity.tagEntity;
    QArticleTagEntity qArticleTag = QArticleTagEntity.articleTagEntity;
    QArticleEntity qArticle = QArticleEntity.articleEntity;
    List<String> tagNames =
        queryFactory
            .select(qTag.name)
            .from(qTag)
            .innerJoin(qArticleTag)
            .on(qArticleTag.id.tagId.eq(qTag.id))
            .innerJoin(qArticle)
            .on(qArticleTag.id.articleId.eq(qArticle.id))
            .where(qArticle.id.eq(article.getId()))
            .fetch();
    result.setTagNames(tagNames);

    return result;
  }

  /**
   * Mapping article with addition user details.
   *
   * @param articles the articles
   * @return articles
   */
  private List<ArticleResponse> getArticleResponses(List<ArticleEntity> articles) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    return articles.stream().map(article -> getArticleResponse(currentUser, article)).toList();
  }
}
