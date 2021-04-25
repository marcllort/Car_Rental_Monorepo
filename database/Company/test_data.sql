USE company_pressicar;

INSERT INTO `DriverUser`(name, email, phone, country, role)
VALUES ('Chop Bolson', 'chop@gmail.com', '656801150', 'Catalunya', 'Driver');

INSERT INTO `ClientUser`(name, email, phone, country, role)
VALUES ('Pau Bolson', 'pau@gmail.com', '656801151', 'Catalunya', 'Client');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2011-12-18 13:17:17', 'testurl.com',
        '2011-12-18 13:17:17', 23, 2, '2011-12-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport Girona', 'Barcelona Centre', 1, 1, 'Test service', '2010-12-18 13:17:17', 'testurl.com',
        '2011-12-18 13:17:17', 23, 2, '2011-12-18 13:17:17', 4, 'none');

