USE company_pressicar;

create table ClientUser
(
    UserId  int auto_increment,
    Name    VARCHAR(255) not null,
    Email   VARCHAR(255) null,
    Phone   int          null,
    Country VARCHAR(127) null,
    Role    VARCHAR(63)  not null,
    PRIMARY KEY (UserId)
);

create table DriverUser
(
    UserId  int auto_increment,
    Name    VARCHAR(255) not null,
    Email   VARCHAR(255) null,
    Phone   int          null,
    Country VARCHAR(127) null,
    Role    VARCHAR(63)  not null,
    PRIMARY KEY (UserId)
);