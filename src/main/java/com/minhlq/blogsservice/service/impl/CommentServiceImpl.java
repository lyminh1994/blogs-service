package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.CommentResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.exceptions.NoAuthorizationException;
import com.minhlq.blogsservice.exceptions.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.CommentMapper;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.Article;
import com.minhlq.blogsservice.model.Comment;
import com.minhlq.blogsservice.repository.ArticleRepository;
import com.minhlq.blogsservice.repository.CommentRepository;
import com.minhlq.blogsservice.service.CommentService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final ArticleRepository articleRepository;

  private final CommentRepository commentRepository;

  @Override
  public CommentResponse createComment(
      String slug, NewCommentRequest newCommentRequest, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Comment comment =
        commentRepository.save(
            Comment.builder()
                .body(newCommentRequest.getBody())
                .article(article)
                .user(UserMapper.MAPPER.toUser(currentUser))
                .build());
    return CommentMapper.MAPPER.toCommentResponse(comment);
  }

  @Override
  public List<CommentResponse> findArticleComments(String slug, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    return commentRepository.findByArticle(article).stream()
        .map(
            comment -> {
              CommentResponse response = CommentMapper.MAPPER.toCommentResponse(comment);
              if (currentUser != null) {
                response
                    .getUser()
                    .setFollowing(
                        comment.getUser().getFollows().stream()
                            .anyMatch(follow -> follow.getId().equals(currentUser.getId())));
              }
              return response;
            })
        .collect(Collectors.toList());
  }

  @Override
  public void deleteComment(String slug, Long commentId, UserPrincipal currentUser) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Comment comment =
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
