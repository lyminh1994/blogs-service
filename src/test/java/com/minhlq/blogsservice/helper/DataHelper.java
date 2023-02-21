package com.minhlq.blogsservice.helper;

import com.minhlq.blogsservice.util.ArticleUtils;
import net.datafaker.Faker;
import net.datafaker.transformations.Field;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.sql.SqlDialect;
import net.datafaker.transformations.sql.SqlTransformer;

import java.time.LocalDateTime;

public final class DataHelper {

  private static final Faker FAKER = new Faker();

  public static void generateSql() {
    Schema<Object, ?> tagSchema =
        Schema.of(
            Field.field("id", () -> "nextval('tags_seq')"),
            Field.field("name", () -> FAKER.book().author()));
    SqlTransformer<Object> transformerTag =
        new SqlTransformer.SqlTransformerBuilder<>()
            .batch(10)
            .tableName("tags")
            .dialect(SqlDialect.POSTGRES)
            .build();
    String outputTag = transformerTag.generate(tagSchema, 100);
    System.out.println(outputTag);

    Schema<Object, ?> userSchema =
        Schema.of(
            Field.field("id", () -> "nextval('users_seq')"),
            Field.field("created_at", () -> LocalDateTime.now()),
            Field.field("created_by", () -> "system"),
            Field.field("public_id", () -> FAKER.internet().uuid()),
            Field.field("updated_at", () -> LocalDateTime.now()),
            Field.field("updated_by", () -> "system"),
            Field.field("version", () -> 0),
            Field.field(
                "birthday", () -> FAKER.date().birthday(18, 65).toLocalDateTime().toLocalDate()),
            Field.field("email", () -> FAKER.internet().emailAddress()),
            Field.field("enabled", () -> true),
            Field.field("failed_login_attempts", () -> 0),
            Field.field("first_name", () -> FAKER.name().firstName()),
            Field.field("gender", () -> FAKER.random().nextInt(0, 1)),
            Field.field("last_name", () -> FAKER.name().lastName()),
            Field.field("last_successful_login", () -> LocalDateTime.now()),
            Field.field("password", () -> FAKER.internet().password()),
            Field.field("phone", () -> FAKER.phoneNumber().phoneNumber()),
            Field.field("profile_image", () -> FAKER.internet().image(320, 320)),
            Field.field("username", () -> FAKER.name().username().replaceAll("\\.", "")),
            Field.field("verification_token", () -> FAKER.internet().uuidv3()));
    SqlTransformer<Object> transformerUser =
        new SqlTransformer.SqlTransformerBuilder<>()
            .batch(10)
            .tableName("users")
            .dialect(SqlDialect.POSTGRES)
            .build();
    String outputUser = transformerUser.generate(userSchema, 10);
    System.out.println(outputUser);

    Schema<Object, ?> articleSchema =
        Schema.of(
            Field.field("id", () -> "nextval('articles_seq')"),
            Field.field("created_at", () -> LocalDateTime.now()),
            Field.field("created_by", () -> "system"),
            Field.field("public_id", () -> FAKER.internet().uuid()),
            Field.field("updated_at", () -> LocalDateTime.now()),
            Field.field("updated_by", () -> "system"),
            Field.field("version", () -> 0),
            Field.field("body", () -> FAKER.bigBangTheory().quote()),
            Field.field("description", () -> FAKER.bigBangTheory().character()),
            Field.field("slug", () -> ArticleUtils.toSlug(FAKER.book().title())),
            Field.field("title", () -> FAKER.book().title()),
            Field.field("user_id", () -> FAKER.random().nextInt(1, 10)));
    SqlTransformer<Object> transformerArticle =
        new SqlTransformer.SqlTransformerBuilder<>()
            .batch(10)
            .tableName("articles")
            .dialect(SqlDialect.POSTGRES)
            .build();
    String outputArticle = transformerArticle.generate(articleSchema, 10);
    System.out.println(outputArticle);

    Schema<Object, ?> commentSchema =
        Schema.of(
            Field.field("id", () -> "nextval('comments_seq')"),
            Field.field("created_at", () -> LocalDateTime.now()),
            Field.field("created_by", () -> "system"),
            Field.field("public_id", () -> FAKER.internet().uuid()),
            Field.field("updated_at", () -> LocalDateTime.now()),
            Field.field("updated_by", () -> "system"),
            Field.field("version", () -> 0),
            Field.field("body", () -> FAKER.community().quote()),
            Field.field("article_id", () -> FAKER.random().nextInt(1, 10)),
            Field.field("user_id", () -> FAKER.random().nextInt(1, 10)));
    SqlTransformer<Object> transformerComment =
        new SqlTransformer.SqlTransformerBuilder<>()
            .batch(10)
            .tableName("comments")
            .dialect(SqlDialect.POSTGRES)
            .build();
    String outputComment = transformerComment.generate(commentSchema, 100);
    System.out.println(outputComment);

    for (int i = 1; i <= 10; i++) {
      for (int j = 1; j <= 10; j++) {
        String sqlArticlesFavorites =
            "insert into articles_favorites (article_id, user_id) values (" + i + ", " + j + ");";
        String sqlArticlesTags =
            "insert into articles_tags (article_id, tag_id) values (" + i + ", " + j + ");";
        String sqlFollows =
            "insert into follows (follow_id, user_id) values (" + i + ", " + j + ");";
      }
    }
  }
}
