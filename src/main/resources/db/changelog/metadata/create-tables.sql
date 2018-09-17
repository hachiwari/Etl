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