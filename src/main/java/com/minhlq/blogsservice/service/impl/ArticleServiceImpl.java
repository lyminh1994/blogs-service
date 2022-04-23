package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewArticleRequest;
import com.minhlq.blogsservice.dto.request.UpdateArticleRequest;
import com.minhlq.blogsservice.dto.response.ArticleResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.entity.ArticleEntity;
import com.minhlq.blogsservice.entity.ArticleFavoriteEntity;
import com.minhlq.blogsservice.entity.ArticleTagEntity;
import com.minhlq.blogsservice.entity.CommentEntity;
import com.minhlq.blogsservice.entity.QArticleEntity;
import com.minhlq.blogsservice.entity.QArticleFavoriteEntity;
import com.minhlq.blogsservice.entity.QArticleTagEntity;
import com.minhlq.blogsservice.entity.QTagEntity;
import com.minhlq.blogsservice.entity.QUserEntity;
import com.minhlq.blogsservice.entity.TagEntity;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.entity.unionkey.ArticleFavoriteKey;
import com.minhlq.blogsservice.entity.unionkey.ArticleTagKey;
import com.minhlq.blogsservice.entity.unionkey.FollowKey;
import com.minhlq.blogsservice.exceptions.NoAuthorizationException;
import com.minhlq.blogsservice.exceptions.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.ArticleMapper;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.repository.ArticleFavoriteRepository;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.ArticleTagRepository;
import com.minhlq.blogsservice.repository.CommentRepository;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.TagRepository;
import com.minhlq.blogsservice.service.ArticleService;
import com.minhlq.blogsservice.utils.ArticleUtils;
import com.minhlq.blogsservice.utils.SecurityUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

  private final JPAQueryFactory queryFactory;

  private final ArticleRepository articleRepository;

  private final ArticleTagRepository articleTagRepository;

  private final ArticleFavoriteRepository articleFavoriteRepository;

  private final TagRepository tagRepository;

  private final FollowRepository followRepository;

  private final CommentRepository commentRepository;

  @Override
  public ArticleResponse createArticle(NewArticleRequest createRequest) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    UserEntity author = UserMapper.MAPPER.toUser(currentUser);
    ArticleEntity article =
        articleRepository.saveAndFlush(
            ArticleEntity.builder()
                .author(author)
                .slug(ArticleUtils.toSlug(createRequest.getTitle()))
                .title(createRequest.getTitle())
                .description(createRequest.getDescription())
                .body(createRequest.getBody())
                .build());

    List<String> tagNames = createRequest.getTagNames();
    if (!tagNames.isEmpty()) {
      List<ArticleTagEntity> articleTags =
          tagNames.stream()
              .map(
                  tagName -> {
                    TagEntity tag =
                        tagRepository
                            .findByName(tagName)
                            .orElseGet(() -> tagRepository.saveAndFlush(new TagEntity(tagName)));

                    ArticleTagKey articleTagKey = new ArticleTagKey(article.getId(), tag.getId());
                    return ArticleTagEntity.builder().id(articleTagKey).build();
                  })
              .collect(Collectors.toList());

      articleTagRepository.saveAll(articleTags);
    }

    return ArticleMapper.MAPPER.toArticleResponse(article, tagNames);
  }

  @Override
  public PageResponse<ArticleResponse> findUserFeeds(PageRequest pageRequest) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    Set<Long> followedUsers = followRepository.findFollowedUsers(currentUser.getId());
    if (followedUsers.isEmpty()) {
      return new PageResponse<>(Collections.emptyList(), 0);
    }

    Page<ArticleEntity> articles =
        articleRepository.findByFollowedUsers(followedUsers, pageRequest);
    List<ArticleResponse> responses = getArticleResponses(articles.getContent());

    return new PageResponse<>(responses, articles.getTotalElements());
  }

  @Override
  public PageResponse<ArticleResponse> findRecentArticles(
      String tagName, String favoriteBy, String author, PageRequest pageRequest) {
    BooleanBuilder conditions = new BooleanBuilder();
    if (StringUtils.isNotBlank(tagName)) {
      conditions.and(QTagEntity.tagEntity.name.eq(tagName));
    }
    if (StringUtils.isNotBlank(author)) {
      conditions.and(QArticleEntity.articleEntity.author.username.eq(author));
    }
    if (StringUtils.isNotBlank(favoriteBy)) {
      conditions.and(QUserEntity.userEntity.username.eq(favoriteBy));
    }

    JPAQuery<?> query =
        queryFactory
            .from(QArticleEntity.articleEntity)
            .leftJoin(QArticleTagEntity.articleTagEntity)
            .on(QArticleEntity.articleEntity.id.eq(QArticleTagEntity.articleTagEntity.id.articleId))
            .leftJoin(QTagEntity.tagEntity)
            .on(QTagEntity.tagEntity.id.eq(QArticleTagEntity.articleTagEntity.id.tagId))
            .leftJoin(QArticleFavoriteEntity.articleFavoriteEntity)
            .on(
                QArticleFavoriteEntity.articleFavoriteEntity.id.articleId.eq(
                    QArticleEntity.articleEntity.id))
            .leftJoin(QUserEntity.userEntity)
            .on(
                QUserEntity.userEntity.id.eq(
                    QArticleFavoriteEntity.articleFavoriteEntity.id.userId))
            .where(conditions);

    long totalElements = query.select(QArticleEntity.articleEntity.countDistinct()).fetchFirst();

    List<ArticleEntity> articles =
        query
            .distinct()
            .select(QArticleEntity.articleEntity)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .fetch();
    List<ArticleResponse> responses = getArticleResponses(articles);

    return new PageResponse<>(responses, totalElements);
  }

  @Override
  public ArticleResponse findBySlug(String slug) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return getArticleResponse(currentUser, article);
  }

  @Override
  public ArticleResponse updateArticle(String slug, UpdateArticleRequest updateRequest) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity newArticle =
        articleRepository
            .findBySlug(slug)
            .map(
                oldArticle -> {
                  if (!currentUser.getId().equals(oldArticle.getAuthor().getId())) {
                    throw new NoAuthorizationException();
                  }

                  oldArticle.setSlug(ArticleUtils.toSlug(updateRequest.getTitle()));
                  oldArticle.setTitle(updateRequest.getTitle());
                  oldArticle.setDescription(updateRequest.getDescription());
                  oldArticle.setBody(updateRequest.getBody());

                  return articleRepository.save(oldArticle);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return getArticleResponse(currentUser, newArticle);
  }

  @Override
  public void deleteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.getId().equals(article.getAuthor().getId())) {
      throw new NoAuthorizationException();
    }

    List<ArticleTagEntity> articleTags = articleTagRepository.findByArticleId(article.getId());
    articleTagRepository.deleteAll(articleTags);

    List<ArticleFavoriteEntity> articleFavorites =
        articleFavoriteRepository.findByArticleId(article.getId());
    articleFavoriteRepository.deleteAll(articleFavorites);

    List<CommentEntity> comments = commentRepository.findByArticle(article);
    commentRepository.deleteAll(comments);

    articleRepository.delete(article);
  }

  @Override
  public ArticleResponse favoriteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavoriteKey articleFavoriteKey =
        new ArticleFavoriteKey(article.getId(), currentUser.getId());
    if (articleFavoriteRepository.findById(articleFavoriteKey).isEmpty()) {
      articleFavoriteRepository.save(new ArticleFavoriteEntity(articleFavoriteKey));
    }

    return getArticleResponse(currentUser, article);
  }

  @Override
  public ArticleResponse unFavoriteArticle(String slug) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    articleFavoriteRepository
        .findById(new ArticleFavoriteKey(article.getId(), currentUser.getId()))
        .ifPresent(articleFavoriteRepository::delete);

    return getArticleResponse(currentUser, article);
  }

  private ArticleResponse getArticleResponse(UserPrincipal currentUser, ArticleEntity article) {
    ArticleResponse articleResponse = ArticleMapper.MAPPER.toArticleResponse(article);
    if (currentUser != null) {
      articleResponse
          .getAuthor()
          .setFollowing(
              followRepository.existsById(
                  new FollowKey(currentUser.getId(), article.getAuthor().getId())));

      articleResponse.setFavorite(
          articleFavoriteRepository.existsById(
              new ArticleFavoriteKey(article.getId(), currentUser.getId())));
    }

    articleResponse.setFavoritesCount(
        articleFavoriteRepository.countArticleFavoritesByArticleId(article.getId()));

    List<String> tagNames =
        queryFactory
            .select(QTagEntity.tagEntity.name)
            .from(QTagEntity.tagEntity)
            .innerJoin(QArticleTagEntity.articleTagEntity)
            .on(QArticleTagEntity.articleTagEntity.id.tagId.eq(QTagEntity.tagEntity.id))
            .innerJoin(QArticleEntity.articleEntity)
            .on(QArticleTagEntity.articleTagEntity.id.articleId.eq(QArticleEntity.articleEntity.id))
            .where(QArticleEntity.articleEntity.id.eq(article.getId()))
            .fetch();
    articleResponse.setTagNames(tagNames);

    return articleResponse;
  }

  private List<ArticleResponse> getArticleResponses(List<ArticleEntity> articles) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    return articles.stream()
        .map(article -> getArticleResponse(currentUser, article))
        .collect(Collectors.toList());
  }
}
