CREATE TABLE users (
                         id int NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
                         email varchar(45) not null,
                         pwd varchar(200) not null,
                         role varchar (45) not null
);