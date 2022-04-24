DROP TABLE IF EXISTS articles;
CREATE TABLE articles
(
    id          SERIAL PRIMARY KEY,
    user_id     INT,
    slug        VARCHAR(255) UNIQUE,
    title       VARCHAR(255),
    description TEXT,
    body        TEXT,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO articles(id, user_id, slug, title, description, body, created_at)
VALUES (1, 1, 'article-1', 'title1', 'description1', 'body1', current_timestamp);
INSERT INTO articles(id, user_id, slug, title, description, body, created_at)
VALUES (2, 2, 'article-2', 'title2', 'description2', 'body2', current_timestamp);
INSERT INTO articles(id, user_id, slug, title, description, body, created_at)
VALUES (3, 2, 'article-3', 'title3', 'description3', 'body3', current_timestamp);
COMMIT;