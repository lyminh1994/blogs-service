CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email    VARCHAR(255) UNIQUE,
    bio      TEXT,
    image    VARCHAR(511)
);
CREATE TABLE follows
(
    user_id   INT NOT NULL,
    follow_id INT NOT NULL,
    PRIMARY KEY (user_id, follow_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (follow_id) REFERENCES users(id)
);
