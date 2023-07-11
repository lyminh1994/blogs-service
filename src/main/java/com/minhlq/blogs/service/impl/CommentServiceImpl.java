package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.dto.request.NewCommentRequest;
import com.minhlq.blogs.dto.response.CommentResponse;
import com.minhlq.blogs.dto.response.PageResponse;
import com.minhlq.blogs.handler.exception.NoAuthorizationException;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.mapper.CommentMapper;
import com.minhlq.blogs.model.CommentEntity;
import com.minhlq.blogs.model.unionkey.FollowKey;
import com.minhlq.blogs.repository.ArticleRepository;
import com.minhlq.blogs.repository.CommentRepository;
import com.minhlq.blogs.repository.FollowRepository;
import com.minhlq.blogs.service.CommentService;
import com.minhlq.blogs.service.UserService;
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

  private final UserService userService;

  @Override
  public CommentResponse addCommentToArticle(String slug, NewCommentRequest newCommentRequest) {
    var currentUser = userService.getCurrentUser();
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);

    var savedComment =
        commentRepository.save(
            CommentEntity.builder()
                .body(newCommentRequest.body())
                .article(article)
                .user(currentUser)
                .build());

    return CommentMapper.MAPPER.toCommentResponse(savedComment);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<CommentResponse> findArticleComments(String slug, Pageable pageable) {
    var currentUser = userService.getCurrentUser();
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    var comments = commentRepository.findByArticle(article, pageable);
    var contents =
        comments.getContent().stream()
            .map(
                comment -> {
                  CommentResponse result = CommentMapper.MAPPER.toCommentResponse(comment);
                  if (currentUser != null) {
                    FollowKey followId =
                        new FollowKey(currentUser.getId(), comment.getUser().getId());
                    result.getUser().setFollowing(followRepository.existsById(followId));
                  }

                  return result;
                })
            .toList();

    return PageResponse.of(contents, comments.getTotalElements());
  }

  @Override
  public void deleteCommentFromArticle(String slug, Long commentId) {
    var currentUser = userService.getCurrentUser();
    var article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    var comment =
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
