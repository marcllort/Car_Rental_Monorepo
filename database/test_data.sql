USE company_pressicar;

INSERT INTO `DriverUser`(Name, Email, Phone, Country, Role)
VALUES ('Chop Bolson', 'chop@gmail.com', 656801150, 'Catalunya', 'Driver');

INSERT INTO `ClientUser`(Name, Email, Phone, Country, Role)
VALUES ('Pau Bolson', 'pau@gmail.com', 656801151, 'Catalunya', 'Client');

INSERT INTO `Service`(origin, destination, clientid, driverid, description, servicedatetime, calendarevent,
                      payeddatetime, baseprice, extraprice, confirmeddatetime, passengers, specialneeds)
VALUES ('Aeroport BCN', 'Barcelona Centre', 1, 1, 'Test service', '2011-12-18 13:17:17', 'testurl.com',
        '2011-12-18 13:17:17', 23, 2, '2011-12-18 13:17:17', 4, 'none');

INSERT INTO `Service`(origin, destination, clientid, driverid, description, servicedatetime, calendarevent,
                      payeddatetime, baseprice, extraprice, confirmeddatetime, passengers, specialneeds)
VALUES ('Aeroport Girona', 'Barcelona Centre', 1, 1, 'Test service', '2010-12-18 13:17:17', 'testurl.com',
        '2011-12-18 13:17:17', 23, 2, '2011-12-18 13:17:17', 4, 'none');

