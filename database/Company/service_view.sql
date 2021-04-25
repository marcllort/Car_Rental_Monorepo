CREATE VIEW company_pressicar.service_view AS
SELECT `Service`.`service_id`         AS `ServiceId`,
       `Service`.`origin`             AS `Origin`,
       `Service`.`destination`        AS `Destination`,
       `DriverUser`.`user_id`         AS `DriverId`,
       `DriverUser`.`name`            AS `DriverName`,
       `DriverUser`.`phone`           AS `DriverPhone`,
       `DriverUser`.`email`           AS `DriverMail`,
       `DriverUser`.`country`         AS `DriverCountry`,
       `ClientUser`.`user_id`         AS `ClientId`,
       `ClientUser`.`name`            AS `ClientName`,
       `ClientUser`.`phone`           AS `ClientPhone`,
       `ClientUser`.`email`           AS `ClientMail`,
       `ClientUser`.`country`         AS `ClientCountry`,
       `Service`.`description`        AS `Description`,
       `Service`.`service_datetime`   AS `ServiceDatetime`,
       `Service`.`calendar_event`     AS `CalendarEvent`,
       `Service`.`payed_datetime`     AS `PayedDatetime`,
       `Service`.`base_price`         AS `BasePrice`,
       `Service`.`extra_price`        AS `ExtraPrice`,
       `Service`.`confirmed_datetime` AS `ConfirmedDatetime`,
       `Service`.`passengers`         AS `Passengers`,
       `Service`.`special_needs`      AS `SpecialNeeds`
FROM (`Service`
         JOIN `ClientUser` ON ((`ClientUser`.`user_id` = `Service`.`client_id`))
         JOIN `DriverUser` ON ((`DriverUser`.`user_id` = `Service`.`client_id`)))
ORDER BY `Service`.`service_datetime` ASC