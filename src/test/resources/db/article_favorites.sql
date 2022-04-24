DROP TABLE IF EXISTS article_favorites;
CREATE TABLE article_favorites
(
    article_id INT NOT NULL,
    user_id    INT NOT NULL,
    PRIMARY KEY (article_id, user_id)
);

INSERT INTO article_favorites(article_id, user_id)
VALUES (1, 1);
INSERT INTO article_favorites(article_id, user_id)
VALUES (1, 2);
INSERT INTO article_favorites(article_id, user_id)
VALUES (1, 3);
INSERT INTO article_favorites(article_id, user_id)
VALUES (1, 4);
COMMIT;