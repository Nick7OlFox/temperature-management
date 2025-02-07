# Bruno Faria Almeida - coding exercise
## How to run the code
### Setup the database
The necessary files to set up the database are in the <i>data</i> folder. They must be run in the following order:

CREATE_DATABASE.sql

CREATE_TABLE_DEVICE.sql

CREATE_TABLE_RECORDS.sql

SELECT_RECORDS_FOR_AVERAGE.sql

During development a MySQL database was used with the following config:

- url: jdbc:mysql://localhost:3306/tempman?noAccessToProcedureBodies=true

- username: tempManUser (This user has been given Schema Privileges: EXECUTE, INSERT, SELECT, UPDATE and Global Privileges: EXECUTE)

- password: This is stored in the repository as an environment variable. For the purposes of this exercise, you can find it in the
CREATE_DATABASE.sql file, when we create the user.

Please ensure you have a correctly set up database. If you need to change any configuration to run the code localy you can modify
the file application-local.yaml.

### Running the code
When the database is successfully configured, you can now simply run the code as a simple Spring Boot project.

### Using the API
This API has a swagger UI configured. To use it when running the code on a local machine access the following URL: http://localhost:8080/temperature-manager/swagger-ui/index.html
