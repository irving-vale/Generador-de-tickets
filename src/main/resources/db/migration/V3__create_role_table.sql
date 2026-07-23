CREATE TABLE roles (
    id int not null generated always as identity primary key,
    name VARCHAR(50) NOT NULL
);