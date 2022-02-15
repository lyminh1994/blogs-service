CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email    VARCHAR(255) UNIQUE,
    bio      TEXT,
    image    VARCHAR(511)
);

CREATE TABLE articles
(
    id          SERIAL PRIMARY KEY,
    user_id     INT,
    slug        VARCHAR(255) UNIQUE,
    title       VARCHAR(255),
    description TEXT,
    body        TEXT,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    body       TEXT,
    article_id INT,
    user_id    INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE follows
(
    user_id   INT NOT NULL,
    follow_id INT NOT NULL,
    PRIMARY KEY (user_id, follow_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (follow_id) REFERENCES users (id)
);

CREATE TABLE article_favorites
(
    article_id INT NOT NULL,
    user_id    INT NOT NULL,
    PRIMARY KEY (article_id, user_id),
    FOREIGN KEY (article_id) REFERENCES articles (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE article_tags
(
    article_id INT NOT NULL,
    tag_id     INT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES articles (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);
