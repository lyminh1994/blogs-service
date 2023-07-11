package com.minhlq.blogs.client;

import com.minhlq.blogs.client.dto.ArticleResponse;
import com.minhlq.blogs.client.dto.ArticlesResponse;
import com.minhlq.blogs.client.dto.CommentResponse;
import com.minhlq.blogs.client.dto.LoginRequest;
import com.minhlq.blogs.client.dto.NewArticleRequest;
import com.minhlq.blogs.client.dto.NewCommentRequest;
import com.minhlq.blogs.client.dto.ProfileResponse;
import com.minhlq.blogs.client.dto.RegisterRequest;
import com.minhlq.blogs.client.dto.TagResponse;
import com.minhlq.blogs.client.dto.UpdateArticleReq;
import com.minhlq.blogs.client.dto.UpdateUserRequest;
import com.minhlq.blogs.client.dto.UserResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("conduit")
public interface ConduitClient {

  @PostMapping("/users/login")
  UserResponse login(@RequestBody LoginRequest body);

  @PostMapping("/users")
  UserResponse register(@RequestBody RegisterRequest body);

  @GetMapping("/user")
  UserResponse getCurrentUser(@RequestHeader String authorization);

  @PutMapping("/user")
  UserResponse updateUser(@RequestHeader String authorization, @RequestBody UpdateUserRequest body);

  @GetMapping("/profiles/{username}")
  ProfileResponse getProfile(@RequestHeader String authorization, @PathVariable String username);

  @PostMapping("/profiles/{username}/follow")
  ProfileResponse follow(@RequestHeader String authorization, @PathVariable String username);

  @DeleteMapping("/profiles/{username}/follow")
  ProfileResponse unFollow(@RequestHeader String authorization, @PathVariable String username);

  @GetMapping("tags")
  TagResponse getTags();

  @PostMapping("/articles")
  Map<String, ArticleResponse> createArticle(
      @RequestHeader String authorization, @RequestBody NewArticleRequest newArticleParam);

  @GetMapping("/articles/feed")
  ArticlesResponse getFeed(
      @RequestHeader String authorization,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "20") int limit);

  @GetMapping("/articles")
  ArticlesResponse getArticles(
      @RequestHeader String authorization,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String favorited,
      @RequestParam(required = false) String author);

  @GetMapping("/articles/{slug}")
  Map<String, ArticleResponse> article(
      @RequestHeader String authorization, @PathVariable String slug);

  @PutMapping("/articles/{slug}")
  Map<String, ArticleResponse> updateArticle(
      @RequestHeader String authorization,
      @PathVariable("slug") String slug,
      @RequestBody UpdateArticleReq updateArticleParam);

  @DeleteMapping("/articles/{slug}")
  ResponseEntity<Void> deleteArticle(
      @RequestHeader String authorization, @PathVariable String slug);

  @PostMapping("articles/{slug}/favorite")
  Map<String, ArticleResponse> favoriteArticle(
      @RequestHeader String authorization, @PathVariable String slug);

  @DeleteMapping("articles/{slug}/favorite")
  Map<String, ArticleResponse> unFavoriteArticle(
      @RequestHeader String authorization, @PathVariable String slug);

  @PostMapping("/articles/{slug}/comments")
  Map<String, CommentResponse> createComment(
      @RequestHeader String authorization,
      @PathVariable String slug,
      @RequestBody NewCommentRequest newCommentParam);

  @GetMapping("/articles/{slug}/comments")
  Map<String, List<CommentResponse>> getComments(
      @RequestHeader String authorization, @PathVariable String slug);

  @DeleteMapping("/articles/{slug}/comments/{id}")
  ResponseEntity<Void> deleteComment(
      @RequestHeader String authorization, @PathVariable String slug, @PathVariable String id);
}
