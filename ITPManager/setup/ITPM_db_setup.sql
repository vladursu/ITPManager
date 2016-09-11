CREATE USER vladu WITH ENCRYPTED PASSWORD 'abcdef' CREATEDB CREATEUSER;
CREATE DATABASE ITPManager OWNER vladu;
-- Owner has all rights to its database, but other roles need to be granted permissions

DROP TABLE IF EXISTS customers CASCADE;
CREATE TABLE customers (
	id SERIAL PRIMARY KEY,
	name VARCHAR(64) NOT NULL,
	car_model VARCHAR(64) NOT NULL,
	registration_id VARCHAR(10) NOT NULL,
	email VARCHAR(64) NOT NULL,
	phone_number VARCHAR(13),
	ITP_end_date DATE NOT NULL,
	email_sent BOOLEAN NOT NULL,
	other VARCHAR(512)
);

CREATE USER zomblin WITH ENCRYPTED PASSWORD 'zombie';
GRANT ALL ON customers TO zomblin;
GRANT ALL ON customers_id_seq TO zomblin;
