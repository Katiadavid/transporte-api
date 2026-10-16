USE transporte;

INSERT INTO modelo_veiculo (modelo, marca, tipo_veiculo, capacidade_max) VALUES ('ZK6122', 'Yutong', 'AUTOCARRO', 50);
INSERT INTO veiculo (matricula, IdModelo, cor, ano_fabrico, quilometragem) VALUES ('AAA-001-MC', 1, 'Branco', 2022, 45000);

INSERT INTO motorista (data_contratacao, nome, carta_conducao, data_validade_carta, telefone)
VALUES ('2022-03-01', 'Carlos Macamo', 'MZ-123456', '2030-12-31', '841234567');
INSERT INTO motorista_endereco (IdM, endereco) VALUES (1, 'Av. 25 de Setembro, Maputo');

INSERT INTO rota (origem, destino, duracao_estimada, preco_base, distancia_km) VALUES ('Maputo', 'Xai-Xai', 180, 450.00, 224);
INSERT INTO rota_dias_operacao (IdR, dia_operacao) VALUES
                                                       (1,'SEG'),(1,'TER'),(1,'QUA'),(1,'QUI'),(1,'SEX'),(1,'SAB'),(1,'DOM');

INSERT INTO rota_veiculo (IdR, matricula)       VALUES (1, 'AAA-001-MC');
INSERT INTO rota_motorista (IdR, IdM)           VALUES (1, 1);
INSERT INTO motorista_veiculo (IdM, matricula)  VALUES (1, 'AAA-001-MC');

INSERT INTO passageiro (nuit, nome, telefone, tipo_passageiro, data_nascimento)
VALUES ('123456789', 'Ana Chissano', '840000001', 'ADULTO', '1998-05-12');
INSERT INTO passageiro_endereco (IdP, endereco) VALUES (1, 'Bairro Central, Maputo');