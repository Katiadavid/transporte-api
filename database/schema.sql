DROP DATABASE IF EXISTS transrapida;
CREATE DATABASE transrapida CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE transrapida;

-- 1. TABELAS
CREATE TABLE Passageiro (
                            IdP INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                            NUIT VARCHAR(20) NOT NULL UNIQUE,
                            nome VARCHAR(120) NOT NULL,
                            tipo_passageiro ENUM('Estudante','Idoso','Regular','VIP') NOT NULL DEFAULT 'Regular',
                            data_nascimento DATE NULL,
                            rua VARCHAR(100) NULL, numero VARCHAR(15) NULL,
                            bairro VARCHAR(80) NULL, cidade VARCHAR(80) NOT NULL,
                            criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE PassageiroTelefone (
                                    IdP INT UNSIGNED NOT NULL, telefone VARCHAR(25) NOT NULL,
                                    PRIMARY KEY (IdP, telefone),
                                    CONSTRAINT fk_pt_passageiro FOREIGN KEY (IdP) REFERENCES Passageiro(IdP)
                                        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Motorista (
                           IdM INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(120) NOT NULL,
                           cartadeconducao VARCHAR(40) NOT NULL UNIQUE,
                           categoria_carta VARCHAR(40) NOT NULL DEFAULT 'Profissional',
                           data_validade_carta DATE NOT NULL, data_contratacao DATE NOT NULL,
                           Status_Motorista ENUM('Activo','Em férias','Afastado') NOT NULL DEFAULT 'Activo',
                           telefoneM VARCHAR(25) NOT NULL,
                           rua VARCHAR(100) NULL, numero VARCHAR(15) NULL,
                           bairro VARCHAR(80) NULL, cidade VARCHAR(80) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE Rota (
                      IdR INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                      origem VARCHAR(100) NOT NULL, destino VARCHAR(100) NOT NULL,
                      Duracao_Estimada TIME NOT NULL, Preco_Base DECIMAL(10,2) NOT NULL,
                      Distancia_KM DECIMAL(8,2) NOT NULL,
                      CONSTRAINT chk_rota_od CHECK (origem <> destino),
                      CONSTRAINT chk_rota_duracao CHECK (Duracao_Estimada > '00:00:00'),
                      CONSTRAINT chk_rota_preco CHECK (Preco_Base >= 0),
                      CONSTRAINT chk_rota_distancia CHECK (Distancia_KM > 0)
) ENGINE=InnoDB;

CREATE TABLE RotaDiasOperacao (
                                  IdR INT UNSIGNED NOT NULL,
                                  DiaSemana ENUM('Segunda','Terca','Quarta','Quinta','Sexta','Sabado','Domingo') NOT NULL,
                                  PRIMARY KEY (IdR, DiaSemana),
                                  CONSTRAINT fk_rdo_rota FOREIGN KEY (IdR) REFERENCES Rota(IdR)
                                      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ModeloVeiculo (
                               IdModelo INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                               modelo VARCHAR(80) NOT NULL, marca VARCHAR(80) NOT NULL,
                               Tipo_Veiculo ENUM('Machimbombo','Mini-machimbombo','Executivo') NOT NULL,
                               capacidademax SMALLINT UNSIGNED NOT NULL,
                               UNIQUE KEY uq_modelo (marca, modelo, Tipo_Veiculo),
                               CONSTRAINT chk_modelo_capacidade CHECK (capacidademax > 0)
) ENGINE=InnoDB;

CREATE TABLE Veiculo (
                         matricula VARCHAR(20) PRIMARY KEY,
                         IdModelo INT UNSIGNED NOT NULL, cor VARCHAR(40) NOT NULL,
                         AnoFabrico YEAR NOT NULL,
                         Status_Veiculo ENUM('Disponivel','Em manutencao','Em viagem','Reservado') NOT NULL DEFAULT 'Disponivel',
                         Quilometragem DECIMAL(10,1) NOT NULL DEFAULT 0,
                         CONSTRAINT fk_veiculo_modelo FOREIGN KEY (IdModelo) REFERENCES ModeloVeiculo(IdModelo)
                             ON UPDATE CASCADE ON DELETE RESTRICT,
                         CONSTRAINT chk_veiculo_km CHECK (Quilometragem >= 0)
) ENGINE=InnoDB;

CREATE TABLE Viagem (
                        IdV INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                        IdR INT UNSIGNED NOT NULL, matricula VARCHAR(20) NOT NULL,
                        Status_Viagem ENUM('Programada','Em andamento','Concluida','Cancelada') NOT NULL DEFAULT 'Programada',
                        Data_Partida DATE NOT NULL, Hora_Partida TIME NOT NULL,
                        Data_Chegada DATE NULL, Hora_Chegada TIME NULL,
                        observacoes TEXT NULL, origem VARCHAR(100) NOT NULL, destino VARCHAR(100) NOT NULL,
                        criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_viagem_rota FOREIGN KEY (IdR) REFERENCES Rota(IdR)
                            ON UPDATE CASCADE ON DELETE RESTRICT,
                        CONSTRAINT fk_viagem_veiculo FOREIGN KEY (matricula) REFERENCES Veiculo(matricula)
                            ON UPDATE CASCADE ON DELETE RESTRICT,
                        CONSTRAINT chk_viagem_od CHECK (origem <> destino),
                        CONSTRAINT chk_viagem_chegada CHECK (Data_Chegada IS NULL OR Data_Chegada >= Data_Partida),
                        INDEX idx_viagem_data_rota (Data_Partida, IdR), INDEX idx_viagem_status (Status_Viagem)
) ENGINE=InnoDB;

CREATE TABLE EscalaMotorista (
                                 idEscalaMotorista INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                 IdV INT UNSIGNED NOT NULL UNIQUE, IdM INT UNSIGNED NOT NULL,
                                 estadoConfirmacao ENUM('Pendente','Confirmada','Recusada','Cancelada') NOT NULL DEFAULT 'Pendente',
                                 dataAtribuicao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_escala_viagem FOREIGN KEY (IdV) REFERENCES Viagem(IdV)
                                     ON UPDATE CASCADE ON DELETE CASCADE,
                                 CONSTRAINT fk_escala_motorista FOREIGN KEY (IdM) REFERENCES Motorista(IdM)
                                     ON UPDATE CASCADE ON DELETE RESTRICT,
                                 INDEX idx_escala_motorista (IdM)
) ENGINE=InnoDB;

CREATE TABLE Bilhete (
                         IdB INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         IdP INT UNSIGNED NOT NULL, IdV INT UNSIGNED NOT NULL,
                         Hora_Compra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         Metodo_Pagamento ENUM('Numerario','M-Pesa','e-Mola','Transferencia','POS','Outro') NOT NULL,
                         Preco_Pago DECIMAL(10,2) NOT NULL,
                         Numero_Assento SMALLINT UNSIGNED NOT NULL,
                         Status_Bilhete ENUM('Valido','Utilizado','Cancelado','Reembolsado') NOT NULL DEFAULT 'Valido',
                         Data_Validade DATE NOT NULL,
                         CONSTRAINT fk_bilhete_passageiro FOREIGN KEY (IdP) REFERENCES Passageiro(IdP)
                             ON UPDATE CASCADE ON DELETE RESTRICT,
                         CONSTRAINT fk_bilhete_viagem FOREIGN KEY (IdV) REFERENCES Viagem(IdV)
                             ON UPDATE CASCADE ON DELETE RESTRICT,
                         CONSTRAINT chk_bilhete_preco CHECK (Preco_Pago >= 0),
                         CONSTRAINT chk_bilhete_assento CHECK (Numero_Assento > 0),
                         CONSTRAINT uq_bilhete_assento_viagem UNIQUE (IdV, Numero_Assento),
                         INDEX idx_bilhete_passageiro (IdP), INDEX idx_bilhete_status (Status_Bilhete)
) ENGINE=InnoDB;

CREATE TABLE Bagagem (
                         IdBagagem INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         IdB INT UNSIGNED NOT NULL, peso DECIMAL(7,2) NOT NULL,
                         descricao VARCHAR(255) NOT NULL,
                         estadoBagagem ENUM('Registada','Recebida','Em transporte','Entregue','Extraviada','Danificada','Cancelada') NOT NULL DEFAULT 'Registada',
                         CONSTRAINT fk_bagagem_bilhete FOREIGN KEY (IdB) REFERENCES Bilhete(IdB)
                             ON UPDATE CASCADE ON DELETE RESTRICT,
                         CONSTRAINT chk_bagagem_peso CHECK (peso > 0), INDEX idx_bagagem_bilhete (IdB)
) ENGINE=InnoDB;

CREATE TABLE Encomenda (
                           idEncomenda INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                           IdV INT UNSIGNED NOT NULL,
                           nomeRemetente VARCHAR(120) NOT NULL,
                           telefoneRemetente VARCHAR(25) NOT NULL,
                           nomeDestinatario VARCHAR(120) NOT NULL,
                           telefoneDestinatario VARCHAR(25) NOT NULL,
                           destinoEntrega VARCHAR(150) NOT NULL,
                           descricao VARCHAR(255) NOT NULL,
                           peso DECIMAL(7,2) NOT NULL,
                           precoEncomenda DECIMAL(10,2) NOT NULL,
                           estadoEncomenda ENUM('Registada','Recebida','Em transporte','Entregue','Extraviada','Danificada','Cancelada') NOT NULL DEFAULT 'Registada',
                           dataRegisto DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           Metodo_Pagamento ENUM('Numerario','M-Pesa','e-Mola','Transferencia','POS','Outro') NOT NULL,
                           estadoPagamento ENUM('Pendente','Pago','Reembolsado') NOT NULL DEFAULT 'Pendente',
                           CONSTRAINT fk_encomenda_viagem FOREIGN KEY (IdV) REFERENCES Viagem(IdV)
                               ON UPDATE CASCADE ON DELETE RESTRICT,
                           CONSTRAINT chk_encomenda_peso CHECK (peso > 0),
                           CONSTRAINT chk_encomenda_preco CHECK (precoEncomenda >= 0),
                           INDEX idx_encomenda_viagem (IdV), INDEX idx_encomenda_estado (estadoEncomenda, estadoPagamento)
) ENGINE=InnoDB;

-- 2. TRIGGERS
DELIMITER $$
CREATE TRIGGER trg_bilhete_bi BEFORE INSERT ON Bilhete FOR EACH ROW
BEGIN
    DECLARE v_status VARCHAR(20);
    DECLARE v_capacidade INT;
    SELECT v.Status_Viagem, mv.capacidademax INTO v_status, v_capacidade
    FROM Viagem v JOIN Veiculo ve ON ve.matricula=v.matricula
                  JOIN ModeloVeiculo mv ON mv.IdModelo=ve.IdModelo WHERE v.IdV=NEW.IdV;
    IF v_status IN ('Cancelada','Concluida') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Nao e possivel emitir bilhete para viagem cancelada ou concluida.';
    END IF;
    IF NEW.Numero_Assento > v_capacidade THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='O numero do assento excede a capacidade do veiculo.';
    END IF;
    SET NEW.Hora_Compra = COALESCE(NEW.Hora_Compra, CURRENT_TIMESTAMP);
    SET NEW.Data_Validade = DATE_ADD(DATE(NEW.Hora_Compra), INTERVAL 7 DAY);
END$$

CREATE TRIGGER trg_bilhete_bu BEFORE UPDATE ON Bilhete FOR EACH ROW
BEGIN
    IF NEW.Hora_Compra <> OLD.Hora_Compra THEN
        SET NEW.Data_Validade = DATE_ADD(DATE(NEW.Hora_Compra), INTERVAL 7 DAY);
    END IF;
END$$

CREATE TRIGGER trg_escala_bi BEFORE INSERT ON EscalaMotorista FOR EACH ROW
BEGIN
    DECLARE v_status VARCHAR(20); DECLARE v_validade DATE;
    DECLARE v_data DATE; DECLARE v_status_viagem VARCHAR(20);
    SELECT Status_Motorista, data_validade_carta INTO v_status, v_validade
    FROM Motorista WHERE IdM=NEW.IdM;
    SELECT Data_Partida, Status_Viagem INTO v_data, v_status_viagem
    FROM Viagem WHERE IdV=NEW.IdV;
    IF v_status <> 'Activo' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='O motorista nao esta activo.';
    END IF;
    IF v_validade < v_data THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='A carta de conducao estara expirada na data da viagem.';
    END IF;
    IF v_status_viagem = 'Cancelada' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Nao se pode atribuir motorista a uma viagem cancelada.';
    END IF;
END$$

CREATE TRIGGER trg_escala_bu BEFORE UPDATE ON EscalaMotorista FOR EACH ROW
BEGIN
    DECLARE v_status VARCHAR(20); DECLARE v_validade DATE;
    DECLARE v_data DATE; DECLARE v_status_viagem VARCHAR(20);
    SELECT Status_Motorista, data_validade_carta INTO v_status, v_validade
    FROM Motorista WHERE IdM=NEW.IdM;
    SELECT Data_Partida, Status_Viagem INTO v_data, v_status_viagem
    FROM Viagem WHERE IdV=NEW.IdV;
    IF v_status <> 'Activo' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='O motorista nao esta activo.';
    END IF;
    IF v_validade < v_data THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='A carta de conducao estara expirada na data da viagem.';
    END IF;
    IF v_status_viagem = 'Cancelada' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Nao se pode atribuir motorista a uma viagem cancelada.';
    END IF;
END$$

-- 3. PROCEDIMENTOS ARMAZENADOS
CREATE PROCEDURE sp_EmitirBilhete(
    IN p_IdP INT UNSIGNED, IN p_IdV INT UNSIGNED,
    IN p_NumeroAssento SMALLINT UNSIGNED, IN p_PrecoPago DECIMAL(10,2),
    IN p_MetodoPagamento VARCHAR(20)
)
BEGIN
    IF p_PrecoPago < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='O preco pago nao pode ser negativo.';
    END IF;
    INSERT INTO Bilhete (IdP,IdV,Numero_Assento,Preco_Pago,Metodo_Pagamento)
    VALUES (p_IdP,p_IdV,p_NumeroAssento,p_PrecoPago,p_MetodoPagamento);
    SELECT LAST_INSERT_ID() AS IdBilhete, 'Bilhete emitido com sucesso' AS Mensagem;
END$$

CREATE PROCEDURE sp_PesquisarViagens(
    IN p_Origem VARCHAR(100), IN p_Destino VARCHAR(100), IN p_Data DATE
)
BEGIN
    SELECT v.IdV,v.origem,v.destino,v.Data_Partida,v.Hora_Partida,v.Status_Viagem,
           r.Preco_Base,r.Duracao_Estimada,ve.matricula,mv.marca,mv.modelo,mv.capacidademax,
           (SELECT COUNT(*) FROM Bilhete b WHERE b.IdV=v.IdV
                                             AND b.Status_Bilhete IN ('Valido','Utilizado')) AS AssentosOcupados
    FROM Viagem v JOIN Rota r ON r.IdR=v.IdR
                  JOIN Veiculo ve ON ve.matricula=v.matricula
                  JOIN ModeloVeiculo mv ON mv.IdModelo=ve.IdModelo
    WHERE v.origem=p_Origem AND v.destino=p_Destino AND v.Data_Partida=p_Data
      AND v.Status_Viagem='Programada' ORDER BY v.Hora_Partida;
END$$

CREATE PROCEDURE sp_RelatorioReceita(IN p_DataInicio DATE, IN p_DataFim DATE)
BEGIN
    IF p_DataInicio > p_DataFim THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='A data inicial nao pode ser posterior a data final.';
    END IF;
    SELECT DATE(Hora_Compra) AS Dia, COUNT(*) AS TotalBilhetes, SUM(Preco_Pago) AS Receita
    FROM Bilhete WHERE Status_Bilhete IN ('Valido','Utilizado')
                   AND DATE(Hora_Compra) BETWEEN p_DataInicio AND p_DataFim
    GROUP BY DATE(Hora_Compra) ORDER BY Dia;
END$$

CREATE PROCEDURE sp_AtualizarEncomenda(
    IN p_IdEncomenda INT UNSIGNED,
    IN p_EstadoEncomenda VARCHAR(20), IN p_EstadoPagamento VARCHAR(20)
)
BEGIN
    UPDATE Encomenda SET estadoEncomenda=p_EstadoEncomenda, estadoPagamento=p_EstadoPagamento
    WHERE idEncomenda=p_IdEncomenda;
    IF ROW_COUNT()=0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Encomenda nao encontrada ou sem alteracoes.';
    END IF;
    SELECT idEncomenda,estadoEncomenda,estadoPagamento FROM Encomenda WHERE idEncomenda=p_IdEncomenda;
END$$
DELIMITER ;

-- 4. VISTAS
CREATE VIEW vw_ViagensDetalhadas AS
SELECT v.IdV,v.origem,v.destino,v.Data_Partida,v.Hora_Partida,v.Data_Chegada,v.Hora_Chegada,
       v.Status_Viagem,r.Distancia_KM,r.Duracao_Estimada,r.Preco_Base,ve.matricula,
       mv.marca,mv.modelo,mv.Tipo_Veiculo,mv.capacidademax
FROM Viagem v JOIN Rota r ON r.IdR=v.IdR
              JOIN Veiculo ve ON ve.matricula=v.matricula JOIN ModeloVeiculo mv ON mv.IdModelo=ve.IdModelo;

CREATE VIEW vw_EscalasMotoristas AS
SELECT e.idEscalaMotorista,e.estadoConfirmacao,e.dataAtribuicao,m.IdM,m.nome AS Motorista,
       m.telefoneM,m.cartadeconducao,v.IdV,v.origem,v.destino,v.Data_Partida,v.Hora_Partida,v.Status_Viagem
FROM EscalaMotorista e JOIN Motorista m ON m.IdM=e.IdM JOIN Viagem v ON v.IdV=e.IdV;

CREATE VIEW vw_BilhetesDetalhados AS
SELECT b.IdB,p.IdP,p.nome AS Passageiro,b.IdV,v.origem,v.destino,v.Data_Partida,v.Hora_Partida,
       b.Numero_Assento,b.Preco_Pago,b.Metodo_Pagamento,b.Status_Bilhete,b.Hora_Compra,b.Data_Validade
FROM Bilhete b JOIN Passageiro p ON p.IdP=b.IdP JOIN Viagem v ON v.IdV=b.IdV;

CREATE VIEW vw_EncomendasDetalhadas AS
SELECT e.idEncomenda,e.nomeRemetente,e.telefoneRemetente,e.nomeDestinatario,e.telefoneDestinatario,
       e.destinoEntrega,e.descricao,e.peso,e.precoEncomenda,e.estadoEncomenda,e.estadoPagamento,e.dataRegisto,
       v.IdV,v.origem AS OrigemViagem,v.destino AS DestinoViagem,v.Data_Partida
FROM Encomenda e JOIN Viagem v ON v.IdV=e.IdV;

CREATE VIEW vw_ReceitaPorViagem AS
SELECT v.IdV,v.origem,v.destino,v.Data_Partida,
       COALESCE(bx.QuantidadeBilhetes,0) AS QuantidadeBilhetes,
       COALESCE(bx.ReceitaBilhetes,0) AS ReceitaBilhetes,
       COALESCE(ex.ReceitaEncomendas,0) AS ReceitaEncomendas
FROM Viagem v
         LEFT JOIN (
    SELECT IdV, COUNT(*) AS QuantidadeBilhetes,
           SUM(CASE WHEN Status_Bilhete IN ('Valido','Utilizado') THEN Preco_Pago ELSE 0 END) AS ReceitaBilhetes
    FROM Bilhete GROUP BY IdV
) bx ON bx.IdV=v.IdV
         LEFT JOIN (
    SELECT IdV, SUM(precoEncomenda) AS ReceitaEncomendas
    FROM Encomenda
    WHERE estadoPagamento='Pago' AND estadoEncomenda<>'Cancelada'
    GROUP BY IdV
) ex ON ex.IdV=v.IdV;