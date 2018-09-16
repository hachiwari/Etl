--liquibase formatted sql

-- changeset tkurek:insert-region
insert into region values (1, 'mazowieckie', 'Polska');
insert into region values (2, 'świętokrzyskie', 'Polska');
insert into region values (3, 'małopolskie', 'Polska');
-- rollback delete table region;

-- changeset tkurek:insert-locality
insert into locality values (1, 1, 'Warszawa');
insert into locality values (2, 1, 'Piaseczno');
insert into locality values (3, 2, 'Kielce');
-- rollback delete table locality;

-- changeset tkurek:insert-shop
insert into shop values (1, 1, 'SHOP1', '111 222 333', 'ul. Prosta 2', '01-540');
insert into shop values (2, 1, 'SHOP2', '222 222 333', 'ul. Prosta 3', '02-530');
insert into shop values (3, 2, 'SHOP3', '111 444 333', 'ul. Prosta 4', '03-520');
insert into shop values (4, 2, 'SHOP4', '111 222 666', 'ul. Prosta 5', '04-510');
-- rollback delete table shop;

-- changeset tkurek:insert-typeWorker
insert into typeWorker values (1, 'Sprzedawca');
insert into typeWorker values (2, 'Administrator');
insert into typeWorker values (3, 'Ochroniaż');
insert into typeWorker values (4, 'Magazynier');
insert into typeWorker values (5, 'Kierownik');
-- rollback delete table typeWoker;

-- changeset tkurek:insert-worker
insert into worker values (1, 'Michał', 'Kopytko', 1, '96023103467', '111 222 333', 'ul. Krzywa 1', 'Warszawa', '01-460');
insert into worker values (2, 'Tomek', 'Buła', 1, '38652875618', '111 444 333', 'ul. Krzywa 2', 'Warszawa', '02-460');
insert into worker values (3, 'Patrycja', 'Bucik', 1, '46945796879', '111 222 666', 'ul. Krzywa 3', 'Warszawa', '03-460');
insert into worker values (4, 'Rafał', 'Zioło', 1, '476587906', '111 888 333', 'ul. Krzywa 4', 'Warszawa', '04-460');
insert into worker values (5, 'Bartosz', 'Szafa', 1, '34567890123', '999 222 333', 'ul. Krzywa 5', 'Warszawa', '05-460');
-- rollback delete table worker;
