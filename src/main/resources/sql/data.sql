insert into roles (id, name, description)
values (nextval('roles_seq'), 'ROLE_USER', 'Normal user');
insert into roles (id, name, description)
values (nextval('roles_seq'), 'ROLE_ADMIN', 'Super user');
commit;
