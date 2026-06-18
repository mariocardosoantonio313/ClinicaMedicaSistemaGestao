-- =====================================================
-- Script de criação do banco de dados Clínica Médica
-- Banco MySQL - UTF8MB4
-- =====================================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS clinica_medica 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE clinica_medica;

-- =====================================================
-- Tabela: usuario
-- =====================================================
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    perfil ENUM('ADMINISTRADOR', 'MEDICO', 'RECEPCIONISTA') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabela: paciente
-- =====================================================
CREATE TABLE IF NOT EXISTS paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_cpf (cpf),
    INDEX idx_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabela: medico
-- =====================================================
CREATE TABLE IF NOT EXISTS medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    especialidade VARCHAR(100),
    crm VARCHAR(20) UNIQUE,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_crm (crm),
    INDEX idx_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabela: consulta
-- =====================================================
CREATE TABLE IF NOT EXISTS consulta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    data_hora DATETIME NOT NULL,
    motivo TEXT,
    status VARCHAR(50) DEFAULT 'Agendada',
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE CASCADE,
    INDEX idx_data_hora (data_hora),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabela: prontuario
-- =====================================================
CREATE TABLE IF NOT EXISTS prontuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL UNIQUE,
    descricao LONGTEXT,
    data_atualizacao DATE NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Inserir usuários de teste
-- =====================================================
INSERT INTO usuario (login, senha, perfil) VALUES 
('admin', 'admin', 'ADMINISTRADOR'),
('medico', 'medico123', 'MEDICO'),
('recepcionista', 'rec123', 'RECEPCIONISTA')
ON DUPLICATE KEY UPDATE login=login;

-- =====================================================
-- Inserir pacientes de teste
-- =====================================================
INSERT INTO paciente (nome, cpf, telefone, data_nascimento, ativo) VALUES 
('João Silva', '123.456.789-00', '11-99999-0001', '1990-05-15', TRUE),
('Maria Santos', '234.567.890-00', '11-99999-0002', '1988-10-22', TRUE),
('Pedro Costa', '345.678.901-00', '11-99999-0003', '1995-03-10', FALSE)
ON DUPLICATE KEY UPDATE nome=nome;

-- =====================================================
-- Inserir médicos de teste
-- =====================================================
INSERT INTO medico (nome, cpf, telefone, especialidade, crm, ativo) VALUES 
('Dr. Carlos Lima', '111.222.333-44', '11-98765-0001', 'Cardiologia', 'CRM123456', TRUE),
('Dra. Ana Santos', '222.333.444-55', '11-98765-0002', 'Ortopedia', 'CRM654321', TRUE),
('Dr. Rafael Costa', '333.444.555-66', '11-98765-0003', 'Neurocirurgia', 'CRM789456', TRUE)
ON DUPLICATE KEY UPDATE nome=nome;

-- =====================================================
-- Verificar criação de tabelas
-- =====================================================
SHOW TABLES;
SELECT COUNT(*) as total_usuarios FROM usuario;
SELECT COUNT(*) as total_pacientes FROM paciente;
SELECT COUNT(*) as total_medicos FROM medico;

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================
