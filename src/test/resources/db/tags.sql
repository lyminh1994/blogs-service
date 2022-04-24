DROP TABLE IF EXISTS tags;
CREATE TABLE tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO tags (id, name)
VALUES (1, 'tag1');
INSERT INTO tags (id, name)
VALUES (2, 'tag2');
COMMIT;