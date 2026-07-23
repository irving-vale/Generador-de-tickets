INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_CUSTOMER');


INSERT INTO authorities (user_id, name) VALUES (1, 'read');
INSERT INTO authorities (user_id, name) VALUES (1, 'write');
INSERT INTO authorities (user_id, name) VALUES (1, 'delete');