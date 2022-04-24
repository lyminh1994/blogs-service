DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email    VARCHAR(255) UNIQUE,
    bio      TEXT,
    image    VARCHAR(511)
);

INSERT INTO users(id, username, password, email, bio, image)
VALUES (1, 'username1', 'password1', 'email1@example.com', 'bio1', 'image1');
INSERT INTO users(id, username, password, email, bio, image)
VALUES (2, 'username2', 'password2', 'email2@example.com', 'bio2', 'image2');
INSERT INTO users(id, username, password, email, bio, image)
VALUES (3, 'username3', 'password3', 'email3@example.com', 'bio3', 'image3');
COMMIT;