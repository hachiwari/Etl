--liquibase formatted sql

-- changeset tkurek:create-WBrand
create table WBrand (
  id            serial,
  idBrand       integer,
  idProducer    integer,
  name          character varying(255),
  subBrand      character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WBrand;

-- changeset tkurek:create-WCountry
create table WCountry (
  id            serial,
  idCountry     integer,
  name          character varying(255),
  code          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WCountry;

-- changeset tkurek:create-WProducer
create table WProducer (
  id            serial,
  idProducer    integer,
  name          character varying(255),
  address       character varying(255),
  city          character varying(255),
  zipCode       character varying(255),
  phone         character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WProducer;

-- changeset tkurek:create-WProvider
create table WProvider (
  id            serial,
  idProvider    integer,
  idCountry     integer,
  name          character varying(255),
  address       character varying(255),
  city          character varying(255),
  zipCode       character varying(255),
  phone         character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WProvider;

-- changeset tkurek:create-WTypePrice
create table WTypePrice (
  id            serial,
  idTypePrice   integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WTypePrice;

-- changeset tkurek:create-WProduct
create table WProduct (
  id            serial,
  idProduct     integer,
  idBrand       integer,
  name          character varying(255),
  code          character varying(255),
  category      character varying(255),
  type          character varying(255),
  price         decimal(10, 2),
  idTypePrice   integer,
  quantity      integer,
  description   character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WProduct;

-- changeset tkurek:create-WLocality
create table WLocality (
  id            serial,
  idLocality    integer,
  idRegion      integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WLocality;

-- changeset tkurek:create-WRegion
create table WRegion (
  id            serial,
  idRegion      integer,
  name          character varying(255),
  country       character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WRegion;

-- changeset tkurek:create-WShop
create table WShop (
  id            serial,
  idShop        integer,
  idLocality    integer,
  name          character varying(255),
  phone         character varying(255),
  address       character varying(255),
  zipCode       character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WShop;

-- changeset tkurek:create-WTypeWorker
create table WTypeWorker (
  id            serial,
  idTypeWorker  integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WTypeWorker;

-- changeset tkurek:create-WWorker
create table WWorker (
  id            serial,
  idWorker      integer,
  firstName     character varying(255),
  lastName      character varying(255),
  idTypeWorker  integer,
  pesel         character varying(255),
  phone         character varying(255),
  address       character varying(255),
  city          character varying(255),
  zipCode       character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table WWorker;

-- changeset tkurek:create-FDelivery
create table FDelivery (
  id              serial,
  idDelivery      integer,
  idProvider      integer,
  idProduct       integer,
  quantityProduct integer,
  price           decimal(10, 2),
  idTypePrice     integer,
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table FDelivery;

-- changeset tkurek:create-FSale
create table FSale (
  id              serial,
  idSale          integer,
  idProduct       integer,
  idShop          integer,
  quantityProduct integer,
  price           decimal(10, 2),
  idTypePrice     integer,
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table FSale;
