CREATE SCHEMA IF NOT EXISTS currencyExchange;

CREATE TABLE IF NOT EXISTS currencyExchange.Currencies(
    id serial PRIMARY KEY,
    code varchar(5) UNIQUE NOT NULL,
    full_name varchar(30) UNIQUE NOT NULL,
    sign varchar(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS currencyExchange.ExchangeRates(
    id serial PRIMARY KEY,
    base_currency_id int NOT NULL REFERENCES currencyExchange.Currencies(ID) ON DELETE CASCADE,
    target_currency_id int UNIQUE NOT NULL REFERENCES currencyExchange.Currencies(ID) ON DELETE CASCADE,
    rate decimal(10,2) NOT NULL
);