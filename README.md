# Bruno Faria Almeida - coding exercise
## How to run the code
### Setup the database
The necessary files to set up the database are in the <i>data</i> folder. They must be run in the following order:

CREATE_TABLE_DEVICE.sql

CREATE_TABLE_RECORDS.sql

SELECT_RECORDS_FOR_AVERAGE.sql

During development a MySQL database was used with the following config:

- url: jdbc:mysql://localhost:3306/tempman?noAccessToProcedureBodies=true

- username: tempManUser (This user has been given Schema Privileges: EXECUTE, INSERT, SELECT, UPDATE and Global Privileges: EXECUTE)

- password: This will have been passed via email

Please ensure you have a correctly set up database. If you need to change any configuration to run the code localy you can modify
the file application-local.yaml.

### Running the code
When the database is successfully configured, you can now simply run the code as a simple Spring Boot project.
