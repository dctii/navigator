# Navigator

## Description

Two drivers need to get packages from storage warehouses and deliver them as fast as possible in an
8-hour work day.

### Requirements

1. 2 Cars, 4 storage warehouses, 8 orders per car, 8-hour working day, 1 mile or km is 10 minutes of
   time of car driver
2. Randomly generate coordinates (X,Y location) of storage warehouse points
3. Randomly generate coordinates of order destination points
4. Build route from storage to each destination point effectively through other points. The goal is
   to deliver 8 orders in one ride effectively, that is by using the shortest path possible.
5. Use _Factory_, _Builder_, _Listener_ design patterns in project
6. Use JDBC or MyBatis to communicate with the MySQL database.
7. Use GitHub to work as a team

### Uses:

- MySQL 8.0.35
- MySQL Workbench 8.0.34
- openjdk 11.0.21
- Apache Maven 3.9.6

### Dependencies

- org.apache.logging.log4j/log4j-api v2.21.1
- org.apache.logging.log4j/log4j-core v2.21.1
- org.apache.commons/commons-lang3 v3.12.0
- commons-io/commons-io v2.15.0
- org.mybatis/mybatis v3.5.15
- com.mysql/mysql-connector-j v8.2.0
- com.fasterxml.jackson.core/jackson-core v.2.16.1
- com.fasterxml.jackson.core/jackson-databind v.2.16.1

### Plugins

- org.apache.maven.plugins/maven-compiler-plugin v3.11.0
- org.codehaus.mojo/exec-maven-plugin v3.1.0

## How to Run

### Set up the Database Schema in MySQL

Note: the database must be named `navigation`.

```shell
# 1. Drop `navigation` database in case it exists. Will be dropped if it exists.
mysql -u {{username}} -p{{password}} < src/resources/sql/navigation_database_drop.sql

# 2. Create `navigation` database.
mysql -u {{username}} -p{{password}} < src/resources/sql/navigation_database_creation.sql

# 3. Create `navigation` schema.
mysql -u {{username}} -p{{password}} -D navigation < src/resources/sql/navigation_schema.sql

```

### Run `BaseDataLoader.class`

```shell
# Clean install dependencies
mvn clean install

# Run the `base-data-loader` profile and load all of the data
mvn exec:java -P base-data-loader


# The menu will load after executing
# After you've reset the database and have a fresh schema, select [1] to load all of the necessary data.
# Doing the other options must be done in order if you want it to upload 
#   successfully because they depend on each other
=== Navigator Base Data Loading Tools: ===
[0] Exit
[1] Load all data: locations, persons, employees, vehicles, drivers, order recipients, and orders

Enter your choice:
1


```


### Run `Main.class`

```shell
# Run `clean install`
mvn clean install

# Run the the primary profile to run the services
mvn exec:java

Menu:
[1] Start Workday for 2 Drivers
[0] Exit
Enter your choice:
1
```

#### Example Output of the `Main.class` For Option `[1]`

```shell
Employee checked in. Work day starting for 'Driver 1'.

'Driver 1' picked up order 'ORD25' from 'Storage 1'
'Driver 1' picked up order 'ORD26' from 'Storage 1'
'Driver 1' picked up order 'ORD27' from 'Storage 1'
'Driver 1' picked up order 'ORD28' from 'Storage 1'
'Driver 1' picked up order 'ORD29' from 'Storage 1'
'Driver 1' picked up order 'ORD30' from 'Storage 1'
'Driver 1' picked up order 'ORD31' from 'Storage 1'
'Driver 1' picked up order 'ORD32' from 'Storage 1'

'Driver 1' embarking on delivery journey, RoutePlan #d56874fe-aaf1-4d8b-a7d6-4a099c08d4d9
 --> Departing from 'Location 78 (7.0, 7.0)'
 --> Total Route Distance: 7.8 km;
 --> Total Route Minutes: 78.0 minutes
 --> Total Number of Orders: 8 orders
 --> Total Locations on Route: 9 locations
 --> Sequence of Deliveries for RoutePlan:
 ----> 'Location 78 (7.0, 7.0)';
 ----> 'Location 87 (6.0, 8.0)';
 ----> 'Location 97 (6.0, 9.0)';
 ----> 'Location 65 (4.0, 6.0)';
 ----> 'Location 43 (2.0, 4.0)';
 ----> 'Location 27 (6.0, 2.0)';
 ----> 'Location 16 (5.0, 1.0)';
 ----> 'Location 30 (9.0, 2.0)';
 ----> 'Location 29 (8.0, 2.0)';
 
 ...
 
 'Driver 1' Departing from 'Location 97 (6.0, 9.0)' to 'Location 65 (4.0, 6.0)'.
 --> Distance: 1.5 km.
 --> Time: 15.0 minutes.
'Driver 1' at 'Location 97 (6.0, 9.0)'
'Driver 1' at 'Location 87 (6.0, 8.0)'
'Driver 1' at 'Location 77 (6.0, 7.0)'
'Driver 1' at 'Location 76 (5.0, 7.0)'
'Driver 1' at 'Location 75 (4.0, 7.0)'
'Driver 1' at 'Location 65 (4.0, 6.0)'
Order 'ORD30' successfully delivered by Driver '1' to 'Location 65 (4.0, 6.0)'

...

All packages for RoutePlan ID# d56874fe-aaf1-4d8b-a7d6-4a099c08d4d9 have been delivered by 'Driver 1'.
 --> Going to the nearest storage, 'Warehouse Beta' at 'Location 29 (8.0, 2.0)'
 
 ...
 
 'Driver 1' -- 8.0 hour workday fulfilled.
--> Overtime in Minutes: 114.0
Total minutes spent delivering: 594.0 minutes
Total routes completed: 5 routes
Total orders delivered: 40 orders delivered
Total distance travelled: 59.4 km
'Driver 1' released from the DriverConnectionPool


```
