ALTER TABLE users
    ADD COLUMN first_name VARCHAR(255),
    ADD COLUMN last_name  VARCHAR(255),
    ADD COLUMN phone      VARCHAR(25) UNIQUE,
    ADD COLUMN birthday   TIMESTAMP,
    ADD COLUMN gender     INT,
    ADD COLUMN status     BOOLEAN;

CREATE TABLE IF NOT EXISTS roles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255),
    description TEXT,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
