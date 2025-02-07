-- Create Schema
CREATE SCHEMA IF NOT EXISTS tempman;
USE tempman;

-- Create user and grant privileges
CREATE USER 'tempManUser' IDENTIFIED BY 'Q8qBS%I5I3O1';
GRANT INSERT, SELECT, UPDATE ON tempman.* TO 'tempManUser';
GRANT EXECUTE ON *.* TO 'tempManUser';
