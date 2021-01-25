DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
    id   INTEGER      NOT NULL AUTO_INCREMENT,
    accountNumber INTEGER NOT NULL,
    amount NUMERIC NOT NULL,
    PRIMARY KEY (id)
);
