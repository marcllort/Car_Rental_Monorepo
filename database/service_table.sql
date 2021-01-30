USE company_pressicar;

create table Service
(
    ServiceId         int auto_increment,
    Origin            VARCHAR(255)   not null,
    Destination       VARCHAR(255)   not null,
    ClientId          int            not null,
    DriverId          int            not null,
    Description       VARCHAR(32765) null,
    ServiceDatetime   datetime       not null,
    CalendarEvent     VARCHAR(256)   null,
    PayedDatetime     datetime       null,
    BasePrice         float          null,
    ExtraPrice        float          null,
    ConfirmedDatetime datetime       null,
    Passengers        int            null,
    SpecialNeeds      VARCHAR(1024)  null,
    PRIMARY KEY (ServiceId),
    FOREIGN KEY (ClientId)
        REFERENCES ClientUser (UserId),
    FOREIGN KEY (DriverId)
        REFERENCES DriverUser (UserId)
);

