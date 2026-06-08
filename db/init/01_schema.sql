-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255),
    nickname    VARCHAR(255),
    email       VARCHAR(255) UNIQUE ,
    cpf         VARCHAR(14) UNIQUE ,
    birth_date  DATE,
    password    VARCHAR(255),
    balance     NUMERIC(19, 4) NOT NULL DEFAULT 0,
    created_at  TIMESTAMP,
    active      BOOLEAN
);

CREATE TABLE stocks (
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticker  VARCHAR(20) UNIQUE,
    name    VARCHAR(255),
    active  BOOLEAN
);

CREATE TABLE transactions (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users(id),
    stock_id    UUID REFERENCES stocks(id),
    type        VARCHAR(20),  -- TransactionType enum
    quantity    BIGINT,
    price       NUMERIC(19, 4),
    amount      NUMERIC(19, 4),
    fee         NUMERIC(19, 4),
    created_at  TIMESTAMP
);

CREATE TABLE position_ledger (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    transaction_id  UUID REFERENCES transactions(id),
    stock_id        UUID REFERENCES stocks(id),
    quantity        BIGINT,
    type            VARCHAR(20),  -- PositionLedgerType enum
    created_at      TIMESTAMP
);

CREATE TABLE positions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    stock_id        UUID REFERENCES stocks(id),
    quantity        BIGINT,
    average_price   NUMERIC(19, 4),
    UNIQUE (user_id, stock_id)
);

CREATE TABLE idempotency_keys (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key UUID,
    user_id         UUID,
    path            VARCHAR(255),
    status          VARCHAR(20),
    http_status     INTEGER,
    response        VARCHAR(5000),
    created_at      TIMESTAMP,
    expire_at       TIMESTAMP,

    CONSTRAINT uq_idempotency_keys UNIQUE (idempotency_key, user_id, path)
);

CREATE TABLE balance_ledger (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    transaction_id  UUID REFERENCES transactions(id),
    type            VARCHAR(20),  -- BalanceLedgerType enum
    amount          NUMERIC(19, 4),
    created_at      TIMESTAMP
);

-- Users
CREATE UNIQUE INDEX idx_users_cpf ON users(cpf);

-- Stocks
CREATE UNIQUE INDEX idx_stocks_ticker ON stocks(ticker);

-- Transactions
CREATE INDEX idx_transactions_user_id ON transactions(user_id);

-- Position Ledger
CREATE INDEX idx_position_ledger_user_stock ON position_ledger(user_id, stock_id);

-- Positions
CREATE INDEX idx_positions_user_id ON positions(user_id);

-- Balance Ledger
CREATE INDEX idx_balance_ledger_user_id ON balance_ledger(user_id);

--Idempotency key
CREATE INDEX idx_idempotency_key_key ON idempotency_keys(idempotency_key);

INSERT INTO stocks (id, ticker, name, active) VALUES
    (gen_random_uuid(), 'PETR4', 'Petrobras PN', true),
    (gen_random_uuid(), 'VALE3', 'Vale ON', true),
    (gen_random_uuid(), 'ITUB4', 'Itaú Unibanco PN', true),
    (gen_random_uuid(), 'BBDC4', 'Bradesco PN', true),
    (gen_random_uuid(), 'BBAS3', 'Banco do Brasil ON', true),
    (gen_random_uuid(), 'WEGE3', 'WEG ON', true),
    (gen_random_uuid(), 'ABEV3', 'Ambev ON', true),
    (gen_random_uuid(), 'MGLU3', 'Magazine Luiza ON', true),
    (gen_random_uuid(), 'RENT3', 'Localiza ON', true),
    (gen_random_uuid(), 'SUZB3', 'Suzano ON', true),
    (gen_random_uuid(), 'HAPV3', 'Hapvida ON', true),
    (gen_random_uuid(), 'GNDI3', 'Notre Dame Intermédica ON', true),
    (gen_random_uuid(), 'RAIL3', 'Rumo ON', true),
    (gen_random_uuid(), 'CCRO3', 'CCR ON', true),
    (gen_random_uuid(), 'EMBR3', 'Embraer ON', true),
    (gen_random_uuid(), 'CPLE6', 'Copel PNB', true),
    (gen_random_uuid(), 'ELET3', 'Eletrobras ON', true),
    (gen_random_uuid(), 'CMIG4', 'Cemig PN', true),
    (gen_random_uuid(), 'ENEV3', 'Eneva ON', true),
    (gen_random_uuid(), 'BRFS3', 'BRF ON', true),
    (gen_random_uuid(), 'JBSS3', 'JBS ON', true),
    (gen_random_uuid(), 'MRFG3', 'Marfrig ON', true),
    (gen_random_uuid(), 'LREN3', 'Lojas Renner ON', true),
    (gen_random_uuid(), 'AZZA3', 'Azzas 2154 ON', true),
    (gen_random_uuid(), 'EQTL3', 'Equatorial Energia ON', true),
    (gen_random_uuid(), 'TOTS3', 'Totvs ON', true),
    (gen_random_uuid(), 'CASH3', 'Méliuz ON', true),
    (gen_random_uuid(), 'PRIO3', 'PetroRio ON', true),
    (gen_random_uuid(), 'CSAN3', 'Cosan ON', true);