INSERT INTO currencyExchange.Currencies (code, full_name, sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('CZK', 'Czech Koruna', 'Kč'),
       ('CNY', 'Yuan Renminbi', '¥'),
       ('HUF', 'Forint', 'Ft'),
       ('GBP', 'Pound Sterling', '£');

INSERT INTO currencyExchange.ExchangeRates (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.92),
       (1, 3, 23.40),
       (1, 4, 7.23),
       (1, 5, 365.05),
       (1, 6, 0.78);