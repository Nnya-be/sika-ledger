CREATE TABLE accounts (
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    TEXT    NOT NULL,
    currency CHAR(3)    NOT NULL CHECK(currency ~ '^[A-Z]{3}$'),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE journal_entries (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description TEXT  NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE postings (
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    journal_entry_id BIGINT     NOT NULL REFERENCES journal_entries(id),
    account_id      BIGINT      NOT NULL REFERENCES accounts(id),
    amount          NUMERIC(19, 4)  NOT NULL CHECK (amount <> 0)
);

CREATE INDEX idx_postings_journal_entry ON postings(journal_entry_id);
CREATE INDEX idx_postings_account       ON postings(account_id);
