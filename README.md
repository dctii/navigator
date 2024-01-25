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
- org.jooq/jooq v3.15.12
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
=== Airport Base Data Loading Tools: ===
[0] Exit
[1] Execute all 'Load' options
[2] Load Locations Data
[3] Load Storages Data
[4] Load Persons Data
[5] Load Employees Data
[6] Load Vehicles Data
[7] Load Drivers Data
[8] Load OrderRecipients Data
[9] Load Orders Data

Enter your choice:
0


```

### Run `GraphTester.class`

```shell
# Run `clean install`
mvn clean install

# Run the the primary profile to run the services
mvn exec:java -P graph-tester

```

### Run `Main.class`

```shell
# Run `clean install`
mvn clean install

# Run the the primary profile to run the services
mvn exec:java

```
