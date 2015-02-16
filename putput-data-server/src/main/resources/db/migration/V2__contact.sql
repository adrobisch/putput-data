CREATE TABLE PP_CONTACT (
  _ID VARCHAR (40) NOT NULL PRIMARY KEY,
  _VERSION BIGINT NOT NULL DEFAULT 0,
  _FIRST_NAME VARCHAR (255) NOT NULL,
  _LAST_NAME VARCHAR (255),
  _ADDITIONAL_NAMES VARCHAR (255),
  _SALUTATION VARCHAR (255),
  _DATE_OF_BIRTH BIGINT,
  _ANNIVERSARY BIGINT,
  _ORGANISATION VARCHAR (255),
  _NOTES VARCHAR(4000),
  _OWNER_ID VARCHAR (40)
);

CREATE TABLE PP_CONTACT_ADDRESS (
  _ID VARCHAR (40) NOT NULL PRIMARY KEY,
  _TYPE VARCHAR (50),
  _STREET VARCHAR (255),
  _HOUSE_NO VARCHAR (255),
  _PO_BOX VARCHAR (255),
  _CITY VARCHAR (255),
  _COUNTRY VARCHAR (255),
  _POSTAL_CODE VARCHAR (255),
  _CONTACT_ID VARCHAR (40)
);

CREATE TABLE PP_CONTACT_INTERNET_ID (
  _ID VARCHAR (40) NOT NULL PRIMARY KEY,
  _TYPE VARCHAR (50),
  _ID_VALUE VARCHAR(100),
  _CONTACT_ID VARCHAR (40)
);

CREATE TABLE PP_CONTACT_PHONE (
  _ID VARCHAR (40) NOT NULL PRIMARY KEY,
  _TYPE VARCHAR (50),
  _NUMBER VARCHAR(100),
  _CONTACT_ID VARCHAR (40)
);

CREATE TABLE PP_CONTACT_EMAIL (
  _ID VARCHAR (40) NOT NULL PRIMARY KEY,
  _TYPE VARCHAR (50),
  _ADDRESS VARCHAR(512),
  _CONTACT_ID VARCHAR (40)
);


ALTER TABLE PP_CONTACT_ADDRESS ADD FOREIGN KEY (_CONTACT_ID) REFERENCES PP_CONTACT(_ID);
ALTER TABLE PP_CONTACT_INTERNET_ID ADD FOREIGN KEY (_CONTACT_ID) REFERENCES PP_CONTACT(_ID);
ALTER TABLE PP_CONTACT_PHONE ADD FOREIGN KEY (_CONTACT_ID) REFERENCES PP_CONTACT(_ID);
ALTER TABLE PP_CONTACT_EMAIL ADD FOREIGN KEY (_CONTACT_ID) REFERENCES PP_CONTACT(_ID);

ALTER TABLE PP_CONTACT ADD FOREIGN KEY (_OWNER_ID) REFERENCES PP_USER(_ID);

INSERT INTO PP_CONTACT(_ID, _OWNER_ID, _FIRST_NAME, _LAST_NAME) VALUES ('1', '1', 'John', 'Foo');
INSERT INTO PP_CONTACT_EMAIL(_ID, _CONTACT_ID, _TYPE, _ADDRESS) VALUES ('1', '1', 'HOME', 'john@foo.com');
INSERT INTO PP_CONTACT_PHONE(_ID, _CONTACT_ID, _TYPE, _NUMBER) VALUES ('1', '1', 'MOBILE', '017112312346');

INSERT INTO PP_CONTACT(_ID, _OWNER_ID, _FIRST_NAME, _LAST_NAME) VALUES ('2', '1', 'Andreas', 'Drobisch');
INSERT INTO PP_CONTACT_EMAIL(_ID, _CONTACT_ID, _TYPE, _ADDRESS) VALUES ('2', '2', 'HOME', 'andreas@drobisch.com');
INSERT INTO PP_CONTACT_PHONE(_ID, _CONTACT_ID, _TYPE, _NUMBER) VALUES ('2', '2', 'MOBILE', '+49 171 123 12345');
