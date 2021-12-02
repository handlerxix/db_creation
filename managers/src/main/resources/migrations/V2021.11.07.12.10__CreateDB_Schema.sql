CREATE TABLE COMPANY(
  ID SERIAL NOT NULL,
  NAME VARCHAR NOT NULL,
  CONSTRAINT COMPANY_PK PRIMARY KEY (ID)
);

CREATE TABLE PRODUCT(
  ID SERIAL NOT NULL,
  NAME VARCHAR NOT NULL,
  CONSTRAINT PRODUCT_PK PRIMARY KEY (ID)
);

CREATE TABLE CREATED_PRODUCTS(
  COMPANY_ID INT NOT NULL REFERENCES COMPANY(id) ON UPDATE CASCADE ON DELETE CASCADE,
  PRODUCT_ID INT NOT NULL REFERENCES PRODUCT(id) ON UPDATE CASCADE ON DELETE CASCADE,
  COUNT INT NOT NULL,
  DATE TIMESTAMP WITH TIME ZONE NOT NULL
);