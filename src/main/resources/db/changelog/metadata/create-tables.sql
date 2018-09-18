--liquibase formatted sql

-- changeset tkurek:create-logImporter
create table logImporter (
  id            serial,
  tableName     character varying(255),
  importTime    timestamp NULL DEFAULT NULL,
  success       tinyint,
  primary key (id)
);
-- rollback drop table logImporter;

-- changeset tkurek:create-badDelivery
create table badDelivery (
  idDelivery      integer,
  nameProvider    character varying(255),
  codeProduct     character varying(255),
  quantityProduct integer,
  timestampFrom   timestamp NULL DEFAULT NULL,
  timestampTo     timestamp NULL DEFAULT NULL
);
-- rollback drop table badDelivery;

-- changeset tkurek:create-badSale
create table badSale (
  idSale          integer,
  codeProduct     character varying(255),
  nameShop        character varying(255),
  quantityProduct integer,
  timestampFrom   timestamp NULL DEFAULT NULL,
  timestampTo     timestamp NULL DEFAULT NULL
);
-- rollback drop table badSale;