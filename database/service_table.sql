USE company_pressicar;

create table Service
(
    service_id         int auto_increment,
    origin             VARCHAR(255)   not null,
    destination        VARCHAR(255)   not null,
    client_id          int            not null,
    driver_id          int            not null,
    description        VARCHAR(32765) null,
    service_datetime   datetime       not null,
    calendar_event     VARCHAR(256)   null,
    payed_datetime     datetime       null,
    base_price         float          null,
    extra_price        float          null,
    confirmed_datetime datetime       null,
    passengers         int            null,
    special_needs      VARCHAR(1024)  null,
    PRIMARY KEY (service_id),
    FOREIGN KEY (client_id)
        REFERENCES ClientUser (user_id),
    FOREIGN KEY (driver_id)
        REFERENCES DriverUser (user_id)
);

