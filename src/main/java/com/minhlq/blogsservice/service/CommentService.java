package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.request.NewCommentRequest;
import com.minhlq.blogsservice.dto.response.CommentResponse;
import java.util.List;

public interface CommentService {

  CommentResponse createComment(String slug, NewCommentRequest newCommentRequest);

  List<CommentResponse> findArticleComments(String slug);

  void deleteComment(String slug, Long commentId);
}
