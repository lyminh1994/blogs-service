package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.response.CommentResponse;
import com.minhlq.blogsservice.exceptions.NoAuthorizationException;
import com.minhlq.blogsservice.exceptions.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.CommentMapper;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.ArticleEntity;
import com.minhlq.blogsservice.model.CommentEntity;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.CommentRepository;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.service.CommentService;
import com.minhlq.blogsservice.utils.SecurityUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final ArticleRepository articleRepository;

  private final CommentRepository commentRepository;

  private final FollowRepository followRepository;

  @Override
  public CommentResponse createComment(String slug, NewCommentRequest newCommentRequest) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    CommentEntity comment =
        commentRepository.save(
            CommentEntity.builder()
                .body(newCommentRequest.getBody())
                .article(article)
                .user(UserMapper.MAPPER.toUser(currentUser))
                .build());

    return CommentMapper.MAPPER.toCommentResponse(comment);
  }

  @Override
  public List<CommentResponse> findArticleComments(String slug) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    ArticleEntity article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);

    return commentRepository.findByArticle(article).stream()
        .map(
            comment -> {
              CommentResponse response = CommentMapper.MAPPER.toCommentResponse(comment);
              if (currentUser != null) {
                response
                    .getUser()
                    .setFollowing(
                        followRepository.existsById(
                            new FollowKey(currentUser.getId(), comment.getUser().getId())));
              }
              return response;
            })
        .collect(Collectors.toList());
  }

  @Override
  public void deleteComment(String slug, Long commentId) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
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