DROP TABLE IF EXISTS follows;
CREATE TABLE follows
(
    user_id   INT NOT NULL,
    follow_id INT NOT NULL,
    PRIMARY KEY (user_id, follow_id)
);

INSERT INTO follows(user_id, follow_id)
VALUES (1, 2);
INSERT INTO follows(user_id, follow_id)
VALUES (1, 3);
COMMIT;