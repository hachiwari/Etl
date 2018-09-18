--liquibase formatted sql

-- changeset tkurek:insert-country
insert into country values (1, 'Polska', 'PL');
insert into country values (2, 'Niemcy', 'DE');
insert into country values (3, 'Francha', 'Fr');
-- rollback delete table country;

-- changeset tkurek:insert-provider
insert into provider values (1, 1, 'DOSTAWCA1', 'ul. Błękitna 1', 'Kraków', '01-430', '111 223 323');
insert into provider values (2, 1, 'DOSTAWCA2', 'ul. Kasprzaka 4', 'Kielce', '21-313', '342 344 546');
insert into provider values (3, 1, 'DOSTAWCA3', 'ul. Prosta 43a', 'Warszawa', '43-324', '923 345 831');
-- rollback delete table provider;

-- changeset tkurek:insert-producer
insert into producer values (1, 'JMP Flowers', 'ul. Zielona 48', 'Stężyca', '08-540', '+48 81 888 95 71');
insert into producer values (2, 'Firma Scholz', 'ul. Wiosenna 47', 'Czeladź', '41-253', '799 29 29 29');
insert into producer values (3, 'Zehnder Polska Sp. z o.o.', 'ul. Kurpiów 14a', 'Wrocław', '52-214', '+48 71 367 64 24');
-- rollback delete table producer;

-- changeset tkurek:insert-brand
insert into brand values (1, 1, 'MARKA1', 'SUB_BRAND1');
insert into brand values (2, 1, 'MARKA2', 'SUB_BRAND2');
insert into brand values (3, 2, 'MARKA3', 'SUB_BRAND3');
-- rollback delete table brand;

-- changeset tkurek:insert-typePrice
insert into typePrice values (1, 'PLN');
insert into typePrice values (2, 'USD');
insert into typePrice values (3, 'EUR');
insert into typePrice values (4, 'GPB');
insert into typePrice values (5, 'CHF');
insert into typePrice values (6, 'SEK');
-- rollback delete table typePrice;

-- changeset tkurek:insert-product
insert into product values (1, 1, 'Rurki z kremem', 'P000001', 'Słodycze', 'Jedzenie', 2.79, 1, 1, 'Super rurki z kremem');
insert into product values (2, 1, 'Kubuś', 'P000002', 'Słodkie napoje', 'Woda', 1.99, 2, 1, 'Kubuś słodki kubuś zdrowy!');
insert into product values (3, 2, 'ChupaChups', 'P000003', 'Słodycze', 'Jedzenie', 0.79, 1, 1, 'Lizaczek');
insert into product values (4, 2, 'Marchewka', 'P000004', 'Warzywo', 'Jedzenie', 0.55, 4, 3, 'Marchewa');
-- rollback delete table product;
