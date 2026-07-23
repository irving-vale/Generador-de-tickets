CREATE TABLE users (
                         id int NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
                         email varchar(45) not null,
                         pwd varchar(200) not null,
                         role_id int not null,
                         constraint fk_role foreign key (role_id) references roles(id)
);