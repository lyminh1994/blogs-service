package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.CommentResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import java.util.List;

public interface CommentService {

  CommentResponse createComment(
      String slug, NewCommentRequest newCommentRequest, UserPrincipal currentUser);

  List<CommentResponse> findArticleComments(String slug, UserPrincipal currentUser);

  void deleteComment(String slug, Long commentId, UserPrincipal currentUser);
}
