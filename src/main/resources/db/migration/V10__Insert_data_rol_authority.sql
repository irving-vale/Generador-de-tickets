INSERT INTO role_authorities (role_id, authority_id)
VALUES
    (1, 1), -- read
    (1, 2), -- write
    (1, 3), -- delete
    (1, 4), -- export
    (1, 5) -- import
ON CONFLICT (role_id, authority_id) DO NOTHING;

INSERT INTO role_authorities (role_id, authority_id)
VALUES
    (1, 1), -- read
    (1, 2), -- write
    (1, 3), -- delete
    (1, 4), -- export
    (1, 5) -- import
ON CONFLICT (role_id, authority_id) DO NOTHING;

INSERT INTO role_authorities (role_id, authority_id)
VALUES
    (2, 1)
ON CONFLICT (role_id, authority_id) DO NOTHING;
INSERT INTO role_authorities (role_id, authority_id)
VALUES
    (3, 1),
    (3, 2),
    (3, 4)
ON CONFLICT (role_id, authority_id) DO NOTHING;