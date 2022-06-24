INSERT INTO roles(id, name, description, created_at, updated_at)
VALUES (nextval('roles_id_seq'), 'USER', 'Role User', now(), now()),
       (nextval('roles_id_seq'), 'ADMIN', 'Role admin', now(), now());
COMMIT;