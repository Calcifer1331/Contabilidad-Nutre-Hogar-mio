create table sqlite_master
(
    type     TEXT,
    name     TEXT,
    tbl_name TEXT,
    rootpage INT,
    sql      TEXT
);

INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'account', 'account', 2, 'CREATE TABLE account (id integer not null, name varchar(255) not null, id_account_subtype integer not null, primary key (id))');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'account_subtype', 'account_subtype', 3, 'CREATE TABLE account_subtype (id integer not null, account_type varchar(255) not null check (account_type in (''ASSETS'',''LIABILITIES'',''EQUITY'',''INCOME'',''EXPENSE'',''COST'')), name varchar(255) not null, primary key (id))');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'journal_entry', 'journal_entry', 4, 'CREATE TABLE journal_entry (id integer, check_number TEXT, concept TEXT, date date not null, name TEXT not null, primary key (id))');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'ledger_record', 'ledger_record', 5, 'CREATE TABLE ledger_record (id integer, credit numeric(15,2), debit numeric(15,2), document_type varchar(255) check (document_type in (''INCOME'',''EXPENDITURE'',''ADJUSTMENT'')), reference TEXT, voucher TEXT, id_account integer not null, id_journal_entry integer not null, primary key (id))');
