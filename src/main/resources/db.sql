CREATE DATABASE finance_tracker;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- TABLE: users
CREATE TABLE users (
                       id          UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
                       email       VARCHAR(255)  NOT NULL UNIQUE,
                       password    VARCHAR(255)  NOT NULL,
                       full_name   VARCHAR(255)  NOT NULL,
                       created_at  TIMESTAMP     NOT NULL DEFAULT now()
);

-- TABLE: refresh_tokens
CREATE TABLE refresh_tokens (
                                id          BIGSERIAL     PRIMARY KEY,
                                token       TEXT          NOT NULL UNIQUE,
                                user_id     UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                expires_at  TIMESTAMP     NOT NULL,
                                revoked     BOOLEAN       NOT NULL DEFAULT false
);

-- TABLE: categories
CREATE TABLE categories (
                            id       BIGSERIAL    PRIMARY KEY,
                            name     VARCHAR(100) NOT NULL,
                            type     VARCHAR(10)  NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
                            user_id  UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- TABLE: transactions
CREATE TABLE transactions (
                              id            UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
                              amount        DECIMAL(15,2)  NOT NULL CHECK (amount > 0),
                              note          TEXT,
                              type          VARCHAR(10)    NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
                              date          DATE           NOT NULL,
                              category_id   BIGINT         REFERENCES categories(id) ON DELETE SET NULL,
                              user_id       UUID           NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              receipt_path  VARCHAR(500),
                              created_at    TIMESTAMP      NOT NULL DEFAULT now()
);


-- Speed up login lookup by email
CREATE INDEX idx_users_email ON users(email);

-- Speed up token lookup during refresh/logout
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

-- Speed up finding all categories for a user
CREATE INDEX idx_categories_user_id ON categories(user_id);

-- Speed up transaction queries (most common filters)
CREATE INDEX idx_transactions_user_id   ON transactions(user_id);
CREATE INDEX idx_transactions_date      ON transactions(date);
CREATE INDEX idx_transactions_type      ON transactions(type);
CREATE INDEX idx_transactions_category  ON transactions(category_id);

