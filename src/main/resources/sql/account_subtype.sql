create table account_subtype
(
    id           integer      not null
        primary key,
    account_type varchar(255) not null,
    name         varchar(255) not null,
    check (account_type in ('ASSETS', 'LIABILITIES', 'EQUITY', 'INCOME', 'EXPENSE', 'COST'))
);

INSERT INTO account_subtype (id, account_type, name) VALUES (11, 'ASSETS', 'ACTIVO CORRIENTE');
INSERT INTO account_subtype (id, account_type, name) VALUES (13, 'ASSETS', 'INVENTARIOS');
INSERT INTO account_subtype (id, account_type, name) VALUES (15, 'ASSETS', 'CUENTA POR COBRAR');
INSERT INTO account_subtype (id, account_type, name) VALUES (16, 'ASSETS', 'ACTIVOS FIJOS');
INSERT INTO account_subtype (id, account_type, name) VALUES (21, 'LIABILITIES', 'CUENTA POR PAGAR');
INSERT INTO account_subtype (id, account_type, name) VALUES (22, 'LIABILITIES', 'OTRAS CUENTAS POR PAGAR');
INSERT INTO account_subtype (id, account_type, name) VALUES (41, 'INCOME', 'INGRESOS');
INSERT INTO account_subtype (id, account_type, name) VALUES (42, 'INCOME', 'INGRESOS POR DONACIÓN');
INSERT INTO account_subtype (id, account_type, name) VALUES (51, 'EXPENSE', 'GASTOS');
INSERT INTO account_subtype (id, account_type, name) VALUES (52, 'EXPENSE', 'GASTOS OTROS/DONACIONES');
INSERT INTO account_subtype (id, account_type, name) VALUES (61, 'COST', 'COSTOS');
INSERT INTO account_subtype (id, account_type, name) VALUES (161, 'ASSETS', 'DEPRECIACIÓN ACUMULADA');
INSERT INTO account_subtype (id, account_type, name) VALUES (3999, 'EQUITY', 'PATRIMONIO');
