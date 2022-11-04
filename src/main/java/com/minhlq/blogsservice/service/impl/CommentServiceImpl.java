package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.mapper.CommentMapper;
import com.minhlq.blogsservice.dto.mapper.UserMapper;
import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.dto.response.PageResponse;
import com.minhlq.blogsservice.model.ArticleEntity;
import com.minhlq.blogsservice.model.CommentEntity;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import com.minhlq.blogsservice.exception.NoAuthorizationException;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.CommentRepository;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.service.CommentService;
import com.minhlq.blogsservice.util.SecurityUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is implement for the comment service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final ArticleRepository articleRepository;

  private final CommentRepository commentRepository;

  private final FollowRepository followRepository;

  @Override
  public CommentResponse addCommentToArticle(String slug, NewCommentRequest newCommentRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);

    CommentEntity savedComment =
        commentRepository.saveAndFlush(
            CommentEntity.builder()
                .body(newCommentRequest.getBody())
                .article(article)
                .user(UserMapper.MAPPER.toUser(currentUser))
                .build());

    return CommentMapper.MAPPER.toCommentResponse(savedComment);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<CommentResponse> findArticleComments(String slug, PageRequest pageRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);

    Page<CommentEntity> comments = commentRepository.findByArticle(article, pageRequest);

    List<CommentResponse> contents =
        comments.getContent().stream()
            .map(
                comment -> {
                  CommentResponse response = CommentMapper.MAPPER.toCommentResponse(comment);
                  if (currentUser != null) {
                    FollowKey followId =
                        new FollowKey(currentUser.getId(), comment.getUser().getId());
                    response.getUser().setFollowing(followRepository.existsById(followId));
                  }

                  return response;
                })
            .collect(Collectors.toList());

    return new PageResponse<>(contents, comments.getTotalElements());
  }

  @Override
  public void deleteCommentFromArticle(String slug, Long commentId) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    CommentEntity comment =
        commentRepository
            .findByIdAndArticle(commentId, article)
            .orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.getId().equals(article.getAuthor().getId())
        || !currentUser.getId().equals(comment.getUser().getId())) {
      throw new NoAuthorizationException();
    }

    commentRepository.delete(comment);
  }
}
