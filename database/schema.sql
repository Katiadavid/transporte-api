
CREATE DATABASE transporte CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE transporte;

CREATE TABLE passageiro (
                            IdP              INT AUTO_INCREMENT PRIMARY KEY,
                            nuit             VARCHAR(9)   NOT NULL,
                            nome             VARCHAR(120) NOT NULL,
                            telefone         VARCHAR(20)  NOT NULL,
                            tipo_passageiro  ENUM('ADULTO','CRIANCA','ESTUDANTE','IDOSO') NOT NULL,
                            data_nascimento  DATE         NOT NULL,
                            CONSTRAINT uq_passageiro_nuit UNIQUE (nuit),
                            CONSTRAINT ck_passageiro_nuit CHECK (nuit REGEXP '^[0-9]{9}$')
    ) ENGINE=InnoDB;

CREATE TABLE passageiro_endereco (
                                     IdP       INT          NOT NULL,
                                     endereco  VARCHAR(200) NOT NULL,
                                     PRIMARY KEY (IdP, endereco),
                                     CONSTRAINT fk_pend_passageiro FOREIGN KEY (IdP)
                                         REFERENCES passageiro (IdP) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE mercadoria (
                            IdMercadoria  INT AUTO_INCREMENT PRIMARY KEY,
                            peso          DOUBLE       NOT NULL,
                            tipo          VARCHAR(60)  NOT NULL,
                            IdP           INT          NOT NULL,
                            CONSTRAINT ck_mercadoria_peso CHECK (peso > 0),
                            CONSTRAINT fk_mercadoria_passageiro FOREIGN KEY (IdP)
                                REFERENCES passageiro (IdP) ON DELETE RESTRICT ON UPDATE CASCADE,
                            INDEX idx_mercadoria_idp (IdP)
) ENGINE=InnoDB;

CREATE TABLE modelo_veiculo (
                                IdModelo        INT AUTO_INCREMENT PRIMARY KEY,
                                modelo          VARCHAR(60) NOT NULL,
                                marca           VARCHAR(60) NOT NULL,
                                tipo_veiculo    ENUM('AUTOCARRO','MINIBUS','CAMIAO') NOT NULL,
                                capacidade_max  INT         NOT NULL,
                                CONSTRAINT uq_modelo_marca UNIQUE (marca, modelo),
                                CONSTRAINT ck_modelo_capacidade CHECK (capacidade_max > 0)
) ENGINE=InnoDB;

CREATE TABLE veiculo (
                         matricula       VARCHAR(15) PRIMARY KEY,
                         IdModelo        INT         NOT NULL,
                         cor             VARCHAR(30) NOT NULL,
                         ano_fabrico     INT         NOT NULL,
                         status_veiculo  ENUM('ATIVO','MANUTENCAO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                         quilometragem   INT         NOT NULL DEFAULT 0,
                         CONSTRAINT ck_veiculo_ano CHECK (ano_fabrico >= 1980),
                         CONSTRAINT ck_veiculo_km  CHECK (quilometragem >= 0),
                         CONSTRAINT fk_veiculo_modelo FOREIGN KEY (IdModelo)
                             REFERENCES modelo_veiculo (IdModelo) ON DELETE RESTRICT ON UPDATE CASCADE,
                         INDEX idx_veiculo_modelo (IdModelo)
) ENGINE=InnoDB;

CREATE TABLE motorista (
                           IdM                  INT AUTO_INCREMENT PRIMARY KEY,
                           data_contratacao     DATE         NOT NULL,
                           nome                 VARCHAR(120) NOT NULL,
                           carta_conducao       VARCHAR(30)  NOT NULL,
                           data_validade_carta  DATE         NOT NULL,
                           telefone             VARCHAR(20)  NOT NULL,
                           status_motorista     ENUM('ATIVO','FERIAS','SUSPENSO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                           CONSTRAINT uq_motorista_carta UNIQUE (carta_conducao)
) ENGINE=InnoDB;

CREATE TABLE motorista_endereco (
                                    IdM       INT          NOT NULL,
                                    endereco  VARCHAR(200) NOT NULL,
                                    PRIMARY KEY (IdM, endereco),
                                    CONSTRAINT fk_mend_motorista FOREIGN KEY (IdM)
                                        REFERENCES motorista (IdM) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE rota (
                      IdR               INT AUTO_INCREMENT PRIMARY KEY,
                      origem            VARCHAR(80)   NOT NULL,
                      destino           VARCHAR(80)   NOT NULL,
                      duracao_estimada  INT           NOT NULL COMMENT 'minutos',
                      preco_base        DECIMAL(10,2) NOT NULL,
                      distancia_km      DOUBLE        NOT NULL,
                      CONSTRAINT ck_rota_duracao  CHECK (duracao_estimada > 0),
                      CONSTRAINT ck_rota_preco    CHECK (preco_base > 0),
                      CONSTRAINT ck_rota_dist     CHECK (distancia_km > 0),
                      CONSTRAINT ck_rota_origem_destino CHECK (origem <> destino),
                      INDEX idx_rota_origem_destino (origem, destino)
) ENGINE=InnoDB;

CREATE TABLE rota_dias_operacao (
                                    IdR           INT NOT NULL,
                                    dia_operacao  ENUM('SEG','TER','QUA','QUI','SEX','SAB','DOM') NOT NULL,
                                    PRIMARY KEY (IdR, dia_operacao),
                                    CONSTRAINT fk_rdo_rota FOREIGN KEY (IdR)
                                        REFERENCES rota (IdR) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE rota_motorista (
                                IdR  INT NOT NULL,
                                IdM  INT NOT NULL,
                                PRIMARY KEY (IdR, IdM),
                                CONSTRAINT fk_rm_rota      FOREIGN KEY (IdR) REFERENCES rota (IdR)      ON DELETE CASCADE ON UPDATE CASCADE,
                                CONSTRAINT fk_rm_motorista FOREIGN KEY (IdM) REFERENCES motorista (IdM) ON DELETE CASCADE ON UPDATE CASCADE,
                                INDEX idx_rm_motorista (IdM)
) ENGINE=InnoDB;

CREATE TABLE motorista_veiculo (
                                   IdM        INT         NOT NULL,
                                   matricula  VARCHAR(15) NOT NULL,
                                   PRIMARY KEY (IdM, matricula),
                                   CONSTRAINT fk_mv_motorista FOREIGN KEY (IdM)       REFERENCES motorista (IdM)     ON DELETE CASCADE ON UPDATE CASCADE,
                                   CONSTRAINT fk_mv_veiculo   FOREIGN KEY (matricula) REFERENCES veiculo (matricula) ON DELETE CASCADE ON UPDATE CASCADE,
                                   INDEX idx_mv_veiculo (matricula)
) ENGINE=InnoDB;

CREATE TABLE rota_veiculo (
                              IdR        INT         NOT NULL,
                              matricula  VARCHAR(15) NOT NULL,
                              PRIMARY KEY (IdR, matricula),
                              CONSTRAINT fk_rv_rota    FOREIGN KEY (IdR)       REFERENCES rota (IdR)          ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT fk_rv_veiculo FOREIGN KEY (matricula) REFERENCES veiculo (matricula) ON DELETE CASCADE ON UPDATE CASCADE,
                              INDEX idx_rv_veiculo (matricula)
) ENGINE=InnoDB;

CREATE TABLE viagem (
                        IdV            INT AUTO_INCREMENT PRIMARY KEY,
                        status_viagem  ENUM('AGENDADA','EM_CURSO','CONCLUIDA','CANCELADA') NOT NULL DEFAULT 'AGENDADA',
                        data_partida   DATE        NOT NULL,
                        hora_partida   VARCHAR(5)  NOT NULL COMMENT 'HH:mm',
                        hora_chegada   VARCHAR(5)  NOT NULL COMMENT 'HH:mm',
                        observacoes    TEXT        NULL,
                        IdR            INT         NOT NULL,
                        matricula      VARCHAR(15) NOT NULL,
                        CONSTRAINT ck_viagem_horas CHECK (hora_chegada > hora_partida),
                        CONSTRAINT fk_viagem_rota    FOREIGN KEY (IdR)       REFERENCES rota (IdR)          ON DELETE RESTRICT ON UPDATE CASCADE,
                        CONSTRAINT fk_viagem_veiculo FOREIGN KEY (matricula) REFERENCES veiculo (matricula) ON DELETE RESTRICT ON UPDATE CASCADE,
                        INDEX idx_viagem_rota_data (IdR, data_partida),
                        INDEX idx_viagem_veiculo_data (matricula, data_partida)
) ENGINE=InnoDB;

CREATE TABLE bilhete (
                         IdB               INT AUTO_INCREMENT PRIMARY KEY,
                         hora_compra       VARCHAR(8)    NOT NULL COMMENT 'HH:mm:ss',
                         metodo_pagamento  ENUM('DINHEIRO','MPESA','EMOLA','MKESH','CARTAO') NOT NULL,
                         preco_pago        DECIMAL(10,2) NOT NULL,
                         numero_assento    INT           NOT NULL,
                         status_bilhete    ENUM('ATIVO','USADO','CANCELADO','EXPIRADO') NOT NULL DEFAULT 'ATIVO',
                         data_validade     DATE          NOT NULL,
                         data_compra       DATE          NOT NULL,
                         IdP               INT           NOT NULL,
                         IdV               INT           NOT NULL,
                         CONSTRAINT ck_bilhete_preco   CHECK (preco_pago >= 0),
                         CONSTRAINT ck_bilhete_assento CHECK (numero_assento > 0),
                         CONSTRAINT ck_bilhete_datas   CHECK (data_validade >= data_compra),
                         CONSTRAINT fk_bilhete_passageiro FOREIGN KEY (IdP) REFERENCES passageiro (IdP) ON DELETE RESTRICT ON UPDATE CASCADE,
                         CONSTRAINT fk_bilhete_viagem     FOREIGN KEY (IdV) REFERENCES viagem (IdV)     ON DELETE RESTRICT ON UPDATE CASCADE,
                         INDEX idx_bilhete_viagem_assento (IdV, numero_assento),
                         INDEX idx_bilhete_passageiro (IdP)
) ENGINE=InnoDB;

CREATE VIEW v_ocupacao_viagem AS
SELECT v.IdV, r.origem, r.destino, v.data_partida, v.hora_partida, v.matricula,
       m.capacidade_max,
       COUNT(b.IdB)                    AS assentos_ocupados,
       m.capacidade_max - COUNT(b.IdB) AS assentos_livres
FROM viagem v
         JOIN rota r            ON r.IdR = v.IdR
         JOIN veiculo ve        ON ve.matricula = v.matricula
         JOIN modelo_veiculo m  ON m.IdModelo = ve.IdModelo
         LEFT JOIN bilhete b    ON b.IdV = v.IdV AND b.status_bilhete IN ('ATIVO','USADO')
GROUP BY v.IdV, r.origem, r.destino, v.data_partida, v.hora_partida, v.matricula, m.capacidade_max;