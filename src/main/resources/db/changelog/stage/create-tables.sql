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

-- changeset tkurek:create-delivery
create table delivery (
  id              serial,
  idDelivery      integer,
  nameProvider    character varying(255),
  codeProduct     character varying(255),
  quantityProduct integer,
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table delivery;

-- changeset tkurek:create-sale
create table sale (
  id              serial,
  idSale          integer,
  codeProduct     character varying(255),
  nameShop        character varying(255),
  quantityProduct integer,
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table sale;

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

-- changeset tkurek:create-stageToWarehouseIdMap
create table stageToWarehouseIdMap (
  id                  serial,
  idStage             integer,
  StageTableName      character varying(255),
  idWarehouse         integer,
  warehouseTableName  character varying(255),
  primary key (id)
);
-- rollback drop table stageToWarehouseIdMap;


-- changeset tkurek:create-tmpWBrand
create table tmpWBrand (
  id            serial,
  idBrand       integer,
  idProducer    integer,
  name          character varying(255),
  subBrand      character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table tmpWBrand;

-- changeset tkurek:create-tmpWCountry
create table tmpWCountry (
  id            serial,
  idCountry     integer,
  name          character varying(255),
  code          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table tmpWCountry;

-- changeset tkurek:create-tmpWProducer
create table tmpWProducer (
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
-- rollback drop table tmpWProducer;

-- changeset tkurek:create-tmpWProvider
create table tmpWProvider (
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
-- rollback drop table tmpWProvider;

-- changeset tkurek:create-tmpWTypePrice
create table tmpWTypePrice (
  id            serial,
  idTypePrice   integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table tmpWTypePrice;

-- changeset tkurek:create-tmpWProduct
create table tmpWProduct (
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
-- rollback drop table tmpWProduct;

-- changeset tkurek:create-tmpWLocality
create table tmpWLocality (
  id            serial,
  idLocality    integer,
  idRegion      integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table tmpWLocality;

-- changeset tkurek:create-tmpWRegion
create table tmpWRegion (
  id            serial,
  idRegion      integer,
  name          character varying(255),
  country       character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table tmpWRegion;

-- changeset tkurek:create-tmpWShop
create table tmpWShop (
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
-- rollback drop table tmpWShop;

-- changeset tkurek:create-tmpWTypeWorker
create table tmpWTypeWorker (
  id            serial,
  idTypeWorker  integer,
  name          character varying(255),
  timestampFrom timestamp NULL DEFAULT NULL,
  timestampTo   timestamp NULL DEFAULT NULL,
  primary key (id)
);
-- rollback drop table tmpWTypeWorker;

-- changeset tkurek:create-tmpWWorker
create table tmpWWorker (
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
-- rollback drop table tmpWWorker;

-- changeset tkurek:create-tmpFDelivery
create table tmpFDelivery (
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
-- rollback drop table tmpFDelivery;

-- changeset tkurek:create-tmpFSale
create table tmpFSale (
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
-- rollback drop table tmpFSale;
