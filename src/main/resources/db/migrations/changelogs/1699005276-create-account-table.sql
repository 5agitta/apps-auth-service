-- liquibase formatted sql
--- SELECT EXTRACT(EPOCH FROM NOW());
-- changeset Abbiirr:1699005276-1

drop table if exists tax.account;
CREATE TABLE tax.account (
    etin VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255),
    phone_number VARCHAR(20),
    password VARCHAR(255),
    is_active BOOLEAN,
    last_login TIMESTAMP,
    access_token TEXT
);
