--liquibase formatted sql

-- changeset tkurek:create-region
create table region (
  idRegion    integer,
  name        character varying(255),
  country     character varying(255),
  primary key (idRegion)
);
-- rollback drop table region;

-- changeset tkurek:create-locality
create table locality (
  idLocality   integer,
  idRegion     integer,
  name         character varying(255),
  primary key (idLocality),
  foreign key (idRegion) references region (idRegion)
);
-- rollback drop table locality;

-- changeset tkurek:create-shop
create table shop (
  idShop       integer,
  idLocality   integer,
  name         character varying(255),
  phone        character varying(255),
  address      character varying(255),
  zipCode      character varying(255),
  primary key (idShop),
  foreign key (idLocality) references locality (idLocality)
);
-- rollback drop table shop;

-- changeset tkurek:create-typeWorker
create table typeWorker (
  idTypeWorker    integer,
  name        character varying(255),
  primary key (idTypeWorker)
);
-- rollback drop table typeWorker;

-- changeset tkurek:create-worker
create table worker (
  idWorker     integer,
  firstName    character varying(255),
  lastName     character varying(255),
  idTypeWorker integer,
  pesel        character varying(255),
  phone        character varying(255),
  address      character varying(255),
  city         character varying(255),
  zipCode      character varying(255),
  primary key (idWorker),
  foreign key (idTypeWorker) references typeWorker (idTypeWorker)
);
-- rollback drop table worker;
