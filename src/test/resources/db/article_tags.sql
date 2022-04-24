DROP TABLE IF EXISTS article_tags;
CREATE TABLE article_tags
(
    article_id INT NOT NULL,
    tag_id     INT NOT NULL,
    PRIMARY KEY (article_id, tag_id)
);

INSERT INTO article_tags(article_id, tag_id)
VALUES (1, 1);
INSERT INTO article_tags(article_id, tag_id)
VALUES (1, 2);

COMMIT;