create table journal_entry
(
    id           integer
        primary key,
    check_number TEXT,
    concept      TEXT,
    date         date not null,
    name         TEXT not null
);

