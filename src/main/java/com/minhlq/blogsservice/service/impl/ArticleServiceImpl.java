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
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
  public ArticleResponse createArticle(UserPrincipal currentUser, NewArticleRequest createRequest) {
    var author = UserMapper.MAPPER.toUser(currentUser);
    var savedArticle =
        articleRepository.saveAndFlush(
            ArticleEntity.builder()
                .author(author)
                .slug(ArticleUtils.toSlug(createRequest.title()))
                .title(createRequest.title())
                .description(createRequest.description())
                .body(createRequest.body())
                .build());

    var tagNames = createRequest.tagNames();
    if (CollectionUtils.isNotEmpty(tagNames)) {
      var articleTags =
          tagNames.stream()
              .map(
                  tagName -> {
                    var savedTag =
                        tagRepository
                            .findByName(tagName)
                            .orElseGet(() -> tagRepository.saveAndFlush(new TagEntity(tagName)));

                    var articleTagId = new ArticleTagKey(savedArticle.getId(), savedTag.getId());

                    return new ArticleTagEntity(articleTagId);
                  })
              .toList();

      articleTagRepository.saveAll(articleTags);
    }

    return ArticleMapper.MAPPER.toArticleResponse(savedArticle, tagNames);
  }

  @Override
  public PageResponse<ArticleResponse> findUserFeeds(UserPrincipal currentUser, Pageable pageable) {
    var followedUsers = followRepository.findByUserIdQuery(currentUser.id());
    if (CollectionUtils.isEmpty(followedUsers)) {
      return new PageResponse<>(Collections.emptyList(), 0);
    }

    var articles = articleRepository.findByFollowedUsersQuery(followedUsers, pageable);
    var contents = getArticleResponses(currentUser, articles.getContent());

    return new PageResponse<>(contents, articles.getTotalElements());
  }

  @Override
  public PageResponse<ArticleResponse> findRecentArticles(
      UserPrincipal currentUser,
      String tagName,
      String favoriteBy,
      String author,
      Pageable pageable) {
    var qTag = QTagEntity.tagEntity;
    var qArticle = QArticleEntity.articleEntity;
    var qUser = QUserEntity.userEntity;
    var qArticleTag = QArticleTagEntity.articleTagEntity;
    var qArticleFavorite = QArticleFavoriteEntity.articleFavoriteEntity;

    var conditions = new BooleanBuilder();
    if (StringUtils.isNotBlank(tagName)) {
      conditions.and(qTag.name.eq(tagName));
    }
    if (StringUtils.isNotBlank(author)) {
      conditions.and(qArticle.author.username.eq(author));
    }
    if (StringUtils.isNotBlank(favoriteBy)) {
      conditions.and(qUser.username.eq(favoriteBy));
    }

    var query =
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

    var totalElements = query.select(qArticle.countDistinct()).fetchFirst();

    var articles =
        query
            .distinct()
            .select(qArticle)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    var contents = getArticleResponses(currentUser, articles);

    return new PageResponse<>(contents, totalElements);
  }

  @Override
  public ArticleResponse findBySlug(UserPrincipal currentUser, String slug) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return getArticleResponse(currentUser, article);
  }

  @Override
  @Transactional
  public ArticleResponse updateArticle(
      UserPrincipal currentUser, String slug, UpdateArticleRequest updateRequest) {
    var newArticle =
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
  public void deleteArticle(UserPrincipal currentUser, String slug) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.id().equals(article.getAuthor().getId())) {
      throw new NoAuthorizationException();
    }

    var articleTags = articleTagRepository.findByArticleIdQuery(article.getId());
    articleTagRepository.deleteAll(articleTags);

    var articleFavorites = articleFavoriteRepository.findByArticleIdQuery(article.getId());
    articleFavoriteRepository.deleteAll(articleFavorites);

    articleRepository.delete(article);
  }

  @Override
  @Transactional
  public ArticleResponse favoriteArticle(UserPrincipal currentUser, String slug) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    var articleFavoriteKey = new ArticleFavoriteKey(article.getId(), currentUser.id());
    if (articleFavoriteRepository.findById(articleFavoriteKey).isEmpty()) {
      articleFavoriteRepository.save(new ArticleFavoriteEntity(articleFavoriteKey));
    }

    return getArticleResponse(currentUser, article);
  }

  @Override
  @Transactional
  public ArticleResponse unFavoriteArticle(UserPrincipal currentUser, String slug) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    var articleFavorite = new ArticleFavoriteKey(article.getId(), currentUser.id());
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
    var result = ArticleMapper.MAPPER.toArticleResponse(article);
    if (currentUser != null) {
      var followId = new FollowKey(currentUser.id(), article.getAuthor().getId());
      result.getAuthor().setFollowing(followRepository.existsById(followId));

      var articleFavoriteId = new ArticleFavoriteKey(article.getId(), currentUser.id());
      result.setFavorite(articleFavoriteRepository.existsById(articleFavoriteId));
    }

    result.setFavoritesCount(
        articleFavoriteRepository.countArticleFavoritesByArticleIdQuery(article.getId()));

    var qTag = QTagEntity.tagEntity;
    var qArticleTag = QArticleTagEntity.articleTagEntity;
    var qArticle = QArticleEntity.articleEntity;
    var tagNames =
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
  private List<ArticleResponse> getArticleResponses(
      UserPrincipal currentUser, List<ArticleEntity> articles) {
    return articles.stream().map(article -> getArticleResponse(currentUser, article)).toList();
  }
}
