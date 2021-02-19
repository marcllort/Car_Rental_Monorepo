USE company_pressicar;

create table ClientUser
(
    user_id int auto_increment,
    name    VARCHAR(255) not null,
    email   VARCHAR(255) null,
    phone   VARCHAR(255) null,
    country VARCHAR(127) null,
    role    VARCHAR(63)  not null,
    PRIMARY KEY (user_id)
);

create table DriverUser
(
    user_id int auto_increment,
    name    VARCHAR(255) not null,
    email   VARCHAR(255) null,
    phone   VARCHAR(255) null,
    country VARCHAR(127) null,
    role    VARCHAR(63)  not null,
    PRIMARY KEY (user_id)
);