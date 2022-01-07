CREATE SEQUENCE users_id_seq;
CREATE TABLE users
(
    id       VARCHAR(255) PRIMARY KEY DEFAULT nextval('users_id_seq'),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email    VARCHAR(255) UNIQUE,
    bio      TEXT,
    image    VARCHAR(511)
);
ALTER SEQUENCE users_id_seq OWNED BY users.id;
