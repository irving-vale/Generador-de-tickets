CREATE TABLE users (
                         id int NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         email varchar(45) not null,
                         pwd varchar(200) not null,
                         enabled boolean default true,
                        role_id int not null,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (role_id) REFERENCES roles(id)
);