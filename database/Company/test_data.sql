USE company_pressicar;

INSERT INTO `DriverUser`(name, email, phone, country, role)
VALUES ('Driver 1', 'futbolsupplier@gmail.com', '656801150', 'Catalunya', 'Driver');

INSERT INTO `DriverUser`(name, email, phone, country, role)
VALUES ('Driver 2', 'futbolsupplier@gmail.com', '656801150', 'Catalunya', 'Driver');

INSERT INTO `DriverUser`(name, email, phone, country, role)
VALUES ('Driver 3', 'futbolsupplier@gmail.com', '656801150', 'Catalunya', 'Driver');

INSERT INTO `ClientUser`(name, email, phone, country, role)
VALUES ('Pau Bolson', 'pau@gmail.com', '656801151', 'Catalunya', 'Client');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-1-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 100, 2, '2021-1-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-1-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 123, 2, '2021-1-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-1-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 100, 2, '2021-1-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-2-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 123, 2, '2021-2-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-2-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 100, 2, '2021-2-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-3-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 523, 2, '2021-3-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-4-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 2300, 2, '2021-4-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-5-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 1230, 2, '2021-5-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-5-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 2230, 2, '2021-5-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, client_id, driver_id, description, service_datetime, calendar_event,
                      payed_datetime, base_price, extra_price, confirmed_datetime, passengers, special_needs)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2021-6-18 13:17:17', 'testurl.com',
        '2021-1-18 13:17:17', 10230, 2, '2021-6-18 13:17:17', 4, 'none');


