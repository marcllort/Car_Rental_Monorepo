CREATE VIEW company_pressicar.service_view AS
SELECT `Service`.`ServiceId`         AS `ServiceId`,
       `Service`.`Origin`            AS `Origin`,
       `Service`.`Destination`       AS `Destination`,
       `DriverUser`.`UserId`         AS `DriverId`,
       `DriverUser`.`Name`           AS `DriverName`,
       `DriverUser`.`Phone`          AS `DriverPhone`,
       `DriverUser`.`Email`          AS `DriverMail`,
       `DriverUser`.`Country`        AS `DriverCountry`,
       `ClientUser`.`UserId`         AS `ClientId`,
       `ClientUser`.`Name`           AS `ClientName`,
       `ClientUser`.`Phone`          AS `ClientPhone`,
       `ClientUser`.`Email`          AS `ClientMail`,
       `ClientUser`.`Country`        AS `ClientCountry`,
       `Service`.`Description`       AS `Description`,
       `Service`.`ServiceDatetime`   AS `ServiceDatetime`,
       `Service`.`CalendarEvent`     AS `CalendarEvent`,
       `Service`.`PayedDatetime`     AS `PayedDatetime`,
       `Service`.`BasePrice`         AS `BasePrice`,
       `Service`.`ExtraPrice`        AS `ExtraPrice`,
       `Service`.`ConfirmedDatetime` AS `ConfirmedDatetime`,
       `Service`.`Passengers`        AS `Passengers`,
       `Service`.`SpecialNeeds`      AS `SpecialNeeds`
FROM (`Service`
         JOIN `ClientUser` ON ((`ClientUser`.`UserId` = `Service`.`ClientId`))
         JOIN `DriverUser` ON ((`DriverUser`.`UserId` = `Service`.`ClientId`)))
ORDER BY `Service`.`ServiceDatetime` ASC