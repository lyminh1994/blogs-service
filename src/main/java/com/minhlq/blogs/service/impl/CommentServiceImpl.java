package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.mapper.CommentMapper;
import com.minhlq.blogs.mapper.UserMapper;
import com.minhlq.blogs.dto.request.NewCommentRequest;
import com.minhlq.blogs.dto.response.CommentResponse;
import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.handler.exception.NoAuthorizationException;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.model.CommentEntity;
import com.minhlq.blogs.model.unionkey.FollowKey;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.repository.ArticleRepository;
import com.minhlq.blogs.repository.CommentRepository;
import com.minhlq.blogs.repository.FollowRepository;
import com.minhlq.blogs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
  public CommentResponse addCommentToArticle(
      UserPrincipal currentUser, String slug, NewCommentRequest newCommentRequest) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);

    var savedComment =
        commentRepository.save(
            CommentEntity.builder()
                .body(newCommentRequest.body())
                .article(article)
                .user(UserMapper.MAPPER.toUser(currentUser))
                .build());

    return CommentMapper.MAPPER.toCommentResponse(savedComment);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<CommentResponse> findArticleComments(
      UserPrincipal currentUser, String slug, Pageable pageable) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    var comments = commentRepository.findByArticle(article, pageable);
    var contents =
        comments.getContent().stream()
            .map(
                comment -> {
                  CommentResponse result = CommentMapper.MAPPER.toCommentResponse(comment);
                  if (currentUser != null) {
                    FollowKey followId = new FollowKey(currentUser.id(), comment.getUser().getId());
                    result.getUser().setFollowing(followRepository.existsById(followId));
                  }

                  return result;
                })
            .toList();

    return new PageResponse<>(contents, comments.getTotalElements());
  }

  @Override
  public void deleteCommentFromArticle(UserPrincipal currentUser, String slug, Long commentId) {
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    var comment =
        commentRepository
            .findByIdAndArticle(commentId, article)
            .orElseThrow(ResourceNotFoundException::new);
    if (!currentUser.id().equals(article.getAuthor().getId())
        || !currentUser.id().equals(comment.getUser().getId())) {
      throw new NoAuthorizationException();
    }

    commentRepository.delete(comment);
  }
}
