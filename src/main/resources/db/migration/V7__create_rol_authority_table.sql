create table role_authorities(
    role_id INT NOT NULL,
    authority_id INT NOT NULL,

    PRIMARY KEY (role_id, authority_id),

    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (authority_id) REFERENCES authorities(id)
)