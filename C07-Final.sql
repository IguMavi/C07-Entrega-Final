DROP DATABASE IF EXISTS DB_Supermercado_Novo;
CREATE DATABASE DB_Supermercado_Novo;
USE DB_Supermercado_Novo;

CREATE TABLE Fornecedores (
    id INT NOT NULL AUTO_INCREMENT,
    nome_fantasia VARCHAR(100) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE Clientes (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(15) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE Produtos (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    estoque INT NOT NULL CHECK (estoque >= 0),
    id_fornecedor INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_fornecedor) REFERENCES Fornecedores(id) ON DELETE CASCADE
);

CREATE TABLE Vendas (
    id INT NOT NULL AUTO_INCREMENT,
    data_venda DATETIME NOT NULL,
    id_cliente INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id) ON DELETE CASCADE
);

CREATE TABLE Itens_Venda (
    id_venda INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    PRIMARY KEY (id_venda, id_produto),
    FOREIGN KEY (id_venda) REFERENCES Vendas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES Produtos(id) ON DELETE CASCADE
);

INSERT INTO Fornecedores (nome_fantasia, cnpj) VALUES 
('Laticínios Abençoado', '11.111.111/0001-11'),
('Ambev', '22.222.222/0001-22'),
('Mansão Maromba', '33.333.333/0001-33'),
('Copper Rita', '44.444.444/0001-44'),
('Padaria Dois Irmãos', '55.555.555/0001-55');

INSERT INTO Clientes (nome, cpf) VALUES 
('Ana Clara', '123.456.789-01'),
('Vinicius Souza', '234.567.890-12'),
('João Pedro', '345.678.901-23'),
('Cauã Souza', '456.789.012-34'),
('Felipe TAGAWA', '567.890.123-45');

INSERT INTO Produtos (nome, preco, estoque, id_fornecedor) VALUES 
('Queijo Mussarela 1kg', 45.90, 50, 1),
('Refrigerante Guaraná 2L', 8.50, 200, 2),
('Tomate 500g', 12.00, 30, 3),
('Peito de Frango 1kg', 22.90, 80, 4),
('Pão de Forma', 9.50, 40, 5);

INSERT INTO Vendas (data_venda, id_cliente) VALUES 
('2023-11-20 09:30:00', 1),
('2023-11-20 14:15:00', 2),
('2023-11-21 10:45:00', 3),
('2023-11-22 16:20:00', 4),
('2023-11-22 18:00:00', 5);

INSERT INTO Itens_Venda (id_venda, id_produto, quantidade, preco_unitario) VALUES 
(1, 1, 2, 45.90),
(2, 2, 5, 8.50),
(3, 3, 1, 12.00),
(4, 4, 3, 22.90),
(5, 5, 2, 9.50);

-- =============================================
-- USUÁRIOS E PERMISSÕES
-- =============================================

DROP USER IF EXISTS 'OperadorCaixa1'@'%';
CREATE USER 'OperadorCaixa1'@'%' IDENTIFIED BY 'senhaCaixa01';

DROP USER IF EXISTS 'OperadorCaixa2'@'%';
CREATE USER 'OperadorCaixa2'@'%' IDENTIFIED BY 'senhaCaixa02';

DROP USER IF EXISTS 'Admin'@'localhost';
CREATE USER 'Admin'@'localhost' IDENTIFIED BY 'admin123';

DROP ROLE IF EXISTS 'Perfil_Atendimento';
CREATE ROLE 'Perfil_Atendimento';

GRANT SELECT, INSERT ON DB_Supermercado_Novo.* TO 'Perfil_Atendimento';
GRANT ALL PRIVILEGES ON DB_Supermercado_Novo.* TO 'Admin'@'localhost';

GRANT 'Perfil_Atendimento' TO 'OperadorCaixa1'@'%', 'OperadorCaixa2'@'%';
SET DEFAULT ROLE 'Perfil_Atendimento' TO 'OperadorCaixa1'@'%', 'OperadorCaixa2'@'%';

FLUSH PRIVILEGES;

-- =============================================
-- PROCEDURES, TRIGGERS E FUNCTIONS
-- =============================================

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_cadastrar_cliente $$

CREATE PROCEDURE sp_cadastrar_cliente(
    p_nome VARCHAR(100),
    p_cpf VARCHAR(15)
)
BEGIN
    INSERT INTO Clientes (nome, cpf)
    VALUES (p_nome, p_cpf);
END $$

DROP TRIGGER IF EXISTS trg_atualiza_estoque $$

CREATE TRIGGER trg_atualiza_estoque
AFTER INSERT ON Itens_Venda
FOR EACH ROW
BEGIN
    DECLARE estoque_atual INT;

    SELECT estoque
    INTO estoque_atual
    FROM Produtos
    WHERE id = NEW.id_produto;

    IF estoque_atual >= NEW.quantidade THEN
        UPDATE Produtos
        SET estoque = estoque - NEW.quantidade
        WHERE id = NEW.id_produto;
    END IF;
END $$

DROP FUNCTION IF EXISTS fn_total_item $$

CREATE FUNCTION fn_total_item(
    p_quantidade INT,
    p_preco DECIMAL(10,2)
)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    RETURN p_quantidade * p_preco;
END $$

DELIMITER ;

-- =============================================
-- PERMISSÕES PARA PROCEDURES E FUNCTIONS
-- =============================================

GRANT EXECUTE ON PROCEDURE DB_Supermercado_Novo.sp_cadastrar_cliente TO 'Perfil_Atendimento';
GRANT EXECUTE ON FUNCTION DB_Supermercado_Novo.fn_total_item TO 'Perfil_Atendimento';

-- =============================================
-- VIEW PARA RELATÓRIO
-- =============================================

DROP VIEW IF EXISTS vw_relatorio_vendas;

CREATE VIEW vw_relatorio_vendas AS
SELECT 
    v.id AS numero_venda,
    c.nome AS cliente,
    p.nome AS produto,
    i.quantidade,
    fn_total_item(i.quantidade, i.preco_unitario) AS total_item
FROM Vendas v
INNER JOIN Clientes c
    ON v.id_cliente = c.id
INNER JOIN Itens_Venda i
    ON v.id = i.id_venda
INNER JOIN Produtos p
    ON i.id_produto = p.id;

-- =============================================
-- VERIFICAR DADOS
-- =============================================

SELECT '=== FORNECEDORES ===' AS '';
SELECT * FROM Fornecedores;

SELECT '=== CLIENTES ===' AS '';
SELECT * FROM Clientes;

SELECT '=== PRODUTOS ===' AS '';
SELECT * FROM Produtos;

SELECT '=== VENDAS ===' AS '';
SELECT * FROM Vendas;

SELECT '=== ITENS_VENDA ===' AS '';
SELECT * FROM Itens_Venda;

SELECT '=== RELATÓRIO DE VENDAS ===' AS '';
SELECT * FROM vw_relatorio_vendas;