package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.mapper.ArticleMapper;
import com.minhlq.blogsservice.dto.mapper.UserMapper;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.entity.Article;
import com.minhlq.blogsservice.entity.ArticleFavorite;
import com.minhlq.blogsservice.entity.ArticleTag;
import com.minhlq.blogsservice.entity.QArticle;
import com.minhlq.blogsservice.entity.QArticleFavorite;
import com.minhlq.blogsservice.entity.QArticleTag;
import com.minhlq.blogsservice.entity.QTag;
import com.minhlq.blogsservice.entity.QUser;
import com.minhlq.blogsservice.entity.Tag;
import com.minhlq.blogsservice.entity.User;
import com.minhlq.blogsservice.entity.unionkey.ArticleFavoriteKey;
import com.minhlq.blogsservice.entity.unionkey.ArticleTagKey;
import com.minhlq.blogsservice.entity.unionkey.FollowKey;
import com.minhlq.blogsservice.exception.NoAuthorizationException;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
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
import java.util.stream.Collectors;
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
    User author = UserMapper.MAPPER.toUser(currentUser);
    Article savedArticle =
        articleRepository.saveAndFlush(
            Article.builder()
                .author(author)
                .slug(ArticleUtils.toSlug(createRequest.getTitle()))
                .title(createRequest.getTitle())
                .description(createRequest.getDescription())
                .body(createRequest.getBody())
                .build());

    List<String> tagNames = createRequest.getTagNames();
    if (CollectionUtils.isNotEmpty(tagNames)) {
      List<ArticleTag> articleTags =
          tagNames.stream()
              .map(
                  tagName -> {
                    Tag savedTag =
                        tagRepository
                            .findByName(tagName)
                            .orElseGet(() -> tagRepository.saveAndFlush(new Tag(tagName)));

                    ArticleTagKey articleTagId =
                        new ArticleTagKey(savedArticle.getId(), savedTag.getId());

                    return new ArticleTag(articleTagId);
                  })
              .collect(Collectors.toList());

      articleTagRepository.saveAll(articleTags);
    }

    return ArticleMapper.MAPPER.toArticleResponse(savedArticle, tagNames);
  }

  @Override
  public PageResponse<ArticleResponse> findUserFeeds(PageRequest pageRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    Set<Long> followedUsers = followRepository.findByUserId(currentUser.getId());
    if (CollectionUtils.isEmpty(followedUsers)) {
      return new PageResponse<>(Collections.emptyList(), 0);
    }

    Page<Article> articles = articleRepository.findByFollowedUsers(followedUsers, pageRequest);
    List<ArticleResponse> contents = getArticleResponses(articles.getContent());

    return new PageResponse<>(contents, articles.getTotalElements());
  }

  @Override
  public PageResponse<ArticleResponse> findRecentArticles(
      String tagName, String favoriteBy, String author, PageRequest pageRequest) {
    QTag qTag = QTag.tag;
    QArticle qArticle = QArticle.article;
    QUser qUser = QUser.user;
    QArticleTag qArticleTag = QArticleTag.articleTag;
    QArticleFavorite qArticleFavorite = QArticleFavorite.articleFavorite;

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

    List<Article> articles =
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
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return getArticleResponse(currentUser, article);
  }

  @Override
  @Transactional
  public ArticleResponse updateArticle(String slug, UpdateArticleRequest updateRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    Article newArticle =
        articleRepository
            .findBySlug(slug)
            .map(
                currentArticle -> {
                  if (!currentUser.getId().equals(currentArticle.getAuthor().getId())) {
                    throw new NoAuthorizationException();
                  }

                  currentArticle.setSlug(ArticleUtils.toSlug(updateRequest.getTitle()));
                  currentArticle.setTitle(updateRequest.getTitle());
                  currentArticle.setDescription(updateRequest.getDescription());
                  currentArticle.setBody(updateRequest.getBody());

                  return articleRepository.save(currentArticle);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return getArticleResponse(currentUser, newArticle);
  }

  @Override
  @Transactional
  public void deleteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.getId().equals(article.getAuthor().getId())) {
      throw new NoAuthorizationException();
    }

    List<ArticleTag> articleTags = articleTagRepository.findByArticleId(article.getId());
    articleTagRepository.deleteAll(articleTags);

    List<ArticleFavorite> articleFavorites =
        articleFavoriteRepository.findByArticleId(article.getId());
    articleFavoriteRepository.deleteAll(articleFavorites);

    articleRepository.delete(article);
  }

  @Override
  @Transactional
  public ArticleResponse favoriteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteKey articleFavoriteKey =
        new ArticleFavoriteKey(article.getId(), currentUser.getId());
    if (articleFavoriteRepository.findById(articleFavoriteKey).isEmpty()) {
      articleFavoriteRepository.save(new ArticleFavorite(articleFavoriteKey));
    }

    return getArticleResponse(currentUser, article);
  }

  @Override
  @Transactional
  public ArticleResponse unFavoriteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteKey articleFavorite =
        new ArticleFavoriteKey(article.getId(), currentUser.getId());
    articleFavoriteRepository
        .findById(articleFavorite)
        .ifPresent(articleFavoriteRepository::delete);

    return getArticleResponse(currentUser, article);
  }

  /**
   * Add user details and tag name to article.
   *
   * @param currentUser the user
   * @param article the article
   * @return article
   */
  private ArticleResponse getArticleResponse(UserPrincipal currentUser, Article article) {
    ArticleResponse result = ArticleMapper.MAPPER.toArticleResponse(article);
    if (currentUser != null) {
      FollowKey followId = new FollowKey(currentUser.getId(), article.getAuthor().getId());
      result.getAuthor().setFollowing(followRepository.existsById(followId));

      ArticleFavoriteKey articleFavoriteId =
          new ArticleFavoriteKey(article.getId(), currentUser.getId());
      result.setFavorite(articleFavoriteRepository.existsById(articleFavoriteId));
    }

    result.setFavoritesCount(
        articleFavoriteRepository.countArticleFavoritesByArticleId(article.getId()));

    QTag qTag = QTag.tag;
    QArticleTag qArticleTag = QArticleTag.articleTag;
    QArticle qArticle = QArticle.article;
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
  private List<ArticleResponse> getArticleResponses(List<Article> articles) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    return articles.stream()
        .map(article -> getArticleResponse(currentUser, article))
        .collect(Collectors.toList());
  }
}
