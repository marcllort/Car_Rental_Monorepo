# Database

Running on [Amazon RDS MySQL](https://aws.amazon.com/es/rds/mysql/) free instance (t.2 micro instance) --> Basic/Simple
setup (hosted in Paris).

Steps to configure:

* Set its configuration to publicly available.

* In Security groups, add 2 inbound rules (port 3306 mysql), one for your development computer with your own IP, and
  another for the EC2 instances where the backend server is being hosted. No need to set the IP, just the name of its
  security group/launch-wizard number.

* The current covid-19 dataset in the DB has the information from [PassportIndex](passportindex.com) of the places you
  can travel with your passport.

* This dataset, which should be regularly updated, can be found
  in [GitHub](https://github.com/ilyankou/passport-index-dataset) in CSV. Transform it to
  MySQL [here](https://www.convertcsv.com/csv-to-sql.htm). If updated, and the list of countries has changed, it must
  also be changed in Countries.go list

* Once the import script is prepared, just connect to the DB with DataGrip/Workbench and run the script.

The DB credentials should be stored always in the Creds folder in the [Backend GoLang](#backend-golang), with the
following format:

```json
{
  "user": "admin",
  "hostname": "x.x.eu-west-3.rds.amazonaws.com (endpoint field in AWS)",
  "port": "3306",
  "database": "db name"
}
```

## Company

This directory contains the scripts for the schema creation of the Service, ClientUser and DriverUser DTO's. On the

other hand, there is also a View that relates the information of the Service with the ClientUser and DriverUser.

There is an extra script, to add some test data.

## Covid

This directory contains the scripts for the schema creation of the Covid table, and Passport table DTO's. 
