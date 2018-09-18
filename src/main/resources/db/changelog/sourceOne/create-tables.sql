--liquibase formatted sql

-- changeset tkurek:create-country
create table country (
  idCountry   integer,
  name        character varying(255),
  code        character varying(255),
  primary key (idCountry)
);
-- rollback drop table country;

-- changeset tkurek:create-provider
create table provider (
  idProvider   integer,
  idCountry    integer,
  name         character varying(255),
  address      character varying(255),
  city         character varying(255),
  zipCode      character varying(255),
  phone        character varying(255),
  primary key (idProvider),
  foreign key (idCountry) references country (idCountry)
);
-- rollback drop table provider;

-- changeset tkurek:create-producer
create table producer (
  idProducer   integer,
  name         character varying(255),
  address      character varying(255),
  city         character varying(255),
  zipCode      character varying(255),
  phone        character varying(255),
  primary key (idProducer)
);
-- rollback drop table producer;

-- changeset tkurek:create-brand
create table brand (
  idBrand      integer,
  idProducer   integer,
  name         character varying(255),
  subBrand     character varying(255),
  primary key (idBrand),
  foreign key (idProducer) references producer (idProducer)
);
-- rollback drop table brand;

-- changeset tkurek:create-typePrice
create table typePrice (
  idTypePrice integer,
  name        character varying(255),
  primary key (idTypePrice)
);
-- rollback drop table typePrice;

-- changeset tkurek:create-product
create table product (
  idProduct   integer,
  idBrand     integer,
  name        character varying(255),
  code        character varying(255),
  category    character varying(255),
  type        character varying(255),
  price       decimal(10, 2),
  idTypePrice integer,
  quantity    integer,
  description character varying(255),
  primary key (idProduct),
  foreign key (idBrand) references brand (idBrand),
  foreign key (idTypePrice) references typePrice (idTypePrice)
);
-- rollback drop table product;
