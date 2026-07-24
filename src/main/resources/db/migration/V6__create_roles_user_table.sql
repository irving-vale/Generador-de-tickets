create table user_roles (
    user_id int not null primary key references users(id) on delete cascade,
    role_id int not null references roles(id) on delete cascade


);