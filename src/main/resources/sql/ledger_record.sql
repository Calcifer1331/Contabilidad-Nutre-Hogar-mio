create table ledger_record
(
    id               integer
        primary key,
    credit           numeric(15, 2),
    debit            numeric(15, 2),
    document_type    varchar(255),
    reference        TEXT,
    voucher          TEXT,
    id_account       integer not null,
    id_journal_entry integer not null,
    check (document_type in ('INCOME', 'EXPENDITURE', 'ADJUSTMENT'))
);

