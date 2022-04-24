DROP TABLE IF EXISTS comments;
CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    body       TEXT,
    article_id INT,
    user_id    INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO comments(id, body, article_id, user_id, created_at)
VALUES (1, 'body 1', 1, 1, current_timestamp);
INSERT INTO comments(id, body, article_id, user_id, created_at)
VALUES (2, 'body 2', 1, 2, current_timestamp);
COMMIT;