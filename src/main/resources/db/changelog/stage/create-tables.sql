--liquibase formatted sql

-- changeset tkurek:create-country
create table country (
  id            serial,
  idCountry     integer,
  name          character varying(255),
  code          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table country;

-- changeset tkurek:create-provider
create table provider (
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
-- rollback drop table provider;

-- changeset tkurek:create-producer
create table producer (
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
-- rollback drop table producer;

-- changeset tkurek:create-brand
create table brand (
  id            serial,
  idBrand       integer,
  idProducer    integer,
  name          character varying(255),
  subBrand      character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table brand;

-- changeset tkurek:create-typePrice
create table typePrice (
  id            serial,
  idTypePrice   integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table typePrice;

-- changeset tkurek:create-product
create table product (
  id            serial,
  idProduct     integer,
  idBrand       integer,
  name          character varying(255),
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
-- rollback drop table product;

-- changeset tkurek:create-region
create table region (
  id            serial,
  idRegion      integer,
  name          character varying(255),
  country       character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table region;

-- changeset tkurek:create-locality
create table locality (
  id            serial,
  idLocality    integer,
  idRegion      integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table locality;

-- changeset tkurek:create-shop
create table shop (
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
-- rollback drop table shop;

-- changeset tkurek:create-typeWorker
create table typeWorker (
  id            serial,
  idTypeWorker  integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table typeWorker;

-- changeset tkurek:create-worker
create table worker (
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
-- rollback drop table worker;

-- changeset tkurek:create-sourceToStageIdMap
create table sourceToStageIdMap (
  id              serial,
  idSource        integer,
  sourceTableName character varying(255),
  idStage         integer,
  stageTableName  character varying(255),
  primary key (id)
);
-- rollback drop table sourceToStageIdMap;
