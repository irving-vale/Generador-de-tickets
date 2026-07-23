CREATE TABLE authorities (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    constraint fk_authorities_users foreign key (user_id) references users(id) on delete cascade

);