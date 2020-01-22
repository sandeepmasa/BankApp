DROP TABLE IF EXISTS Customer;

CREATE TABLE Customer (
 CustomerId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
 UserName VARCHAR(40) NOT NULL,
 Country  VARCHAR(20) NOT NULL,
 ContactNumber LONG,
 EmailId VARCHAR(40) NOT NULL);

INSERT INTO Customer (UserName, Country, ContactNumber, EmailId) VALUES ('test1','UK', '44', 'test@uk.com');
INSERT INTO Customer (UserName, Country, ContactNumber, EmailId) VALUES ('test2','IN', '91', 'test2@in.com');

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
CustomerId LONG,
Balance DECIMAL(19,4),
CurrencyCode VARCHAR(3)
);

INSERT INTO Account (CustomerId,Balance,CurrencyCode) VALUES ('11',100.0000,'USD');
INSERT INTO Account (CustomerId,Balance,CurrencyCode) VALUES ('12',200.0000,'USD');