DROP TABLE IF EXISTS transazione CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS utente CASCADE;

CREATE TABLE utente (
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL PRIMARY KEY,
                        saldo DOUBLE NOT NULL
);

CREATE TABLE categoria (
                           nome VARCHAR(255) NOT NULL,
                           descrizione VARCHAR(255) NULL,
                           fk_utente VARCHAR(255),
                           UNIQUE (nome, fk_utente), -- Vincolo di unicità per nome e utente
                           FOREIGN KEY (fk_utente) REFERENCES utente (username)
);

CREATE TABLE transazione (
                             id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                             causale VARCHAR(255) NOT NULL,
                             data DATE NOT NULL,
                             importo DOUBLE NOT NULL,
                             nome_categoria VARCHAR(255) NOT NULL,
                             fk_categoria BIGINT,
                             fk_utente VARCHAR(255) NOT NULL,
                             FOREIGN KEY (fk_categoria) REFERENCES categoria (id),
                             FOREIGN KEY (fk_utente) REFERENCES utente (username)
);

-- Inserimento dati
INSERT INTO utente (email, password, username, saldo)
VALUES ('test', 'test', 'test', 100.0);

INSERT INTO categoria (nome, fk_utente, descrizione)
VALUES ('categoria', 'test', 'Categoria Test'),
       ('fgrsg', 'test', 'Altro Test'),
       ('dbfdb', 'test', 'Ancora Test');

INSERT INTO transazione (causale, data, importo, nome_categoria, fk_categoria, fk_utente)
VALUES ('Test causale', '2025-01-06', 200.0, 'categoria', 1, 'test');
