package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.ArticleFavoriteEntity;
import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for the Article favorite.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface ArticleFavoriteRepository
        extends JpaRepository<ArticleFavoriteEntity, ArticleFavoriteKey> {

    @Query(
            "select count(AF.id.articleId) "
                    + "from ArticleFavoriteEntity AF "
                    + "where AF.id.articleId = :articleId")
    long countArticleFavoritesByArticleId(Long articleId);

    @Query("from ArticleFavoriteEntity AF where AF.id.articleId = :articleId")
    List<ArticleFavoriteEntity> findByArticleId(Long articleId);
}
