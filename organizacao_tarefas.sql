DROP DATABASE IF EXISTS organizacao_tarefas;
-- Criação do banco de dados, se não existir
CREATE DATABASE IF NOT EXISTS organizacao_tarefas;

-- Seleção do banco de dados
USE organizacao_tarefas;

-- Criação da tabela de usuários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,  -- Para armazenar a senha (sempre criptografada!)
    perfil ENUM('Comum', 'Administrador') NOT NULL DEFAULT 'Comum'
);

-- Criação da tabela de tarefas
CREATE TABLE tarefas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'A Fazer',
    data_entrega DATE NOT NULL,
    prioridade VARCHAR(10) NOT NULL DEFAULT 'Média',
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
); 

-- Inserindo um usuário comum
INSERT INTO usuarios (nome, email, senha, perfil) 
VALUES 
    ('Usuário Comum', 'comum@exemplo.com', 'senha_comum', 'Comum');

-- Inserindo um usuário administrador
INSERT INTO usuarios (nome, email, senha, perfil) 
VALUES 
    ('Administrador', 'admin@exemplo.com', 'senha_admin', 'Administrador');

-- Exibindo todas as tarefas
SELECT * FROM tarefas;
SELECT * FROM Usuarios;
