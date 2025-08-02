CREATE TABLE currency_exchange_log (
                                       id BIGSERIAL PRIMARY KEY,
                                       external_id VARCHAR(50) NOT NULL,
                                       from_currency VARCHAR(3) NOT NULL,
                                       to_currency VARCHAR(3) NOT NULL,
                                       amount NUMERIC(18, 4) NOT NULL,
                                       quote NUMERIC(18, 6) NOT NULL,
                                       converted_amount NUMERIC(18, 4) NOT NULL,
                                       created_at TIMESTAMP DEFAULT now()
);

CREATE UNIQUE INDEX idx_currency_exchange_log_external_id
    ON currency_exchange_log (external_id);