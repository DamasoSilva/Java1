-- SQL Schema para o banco de dados salao_beleza (PostgreSQL)

-- Tabela de Usuários (para autenticação de clientes, profissionais e administradores)
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    sobrenome VARCHAR(255),
    telefone VARCHAR(20),
    tipo_usuario VARCHAR(50) NOT NULL -- 'CLIENTE', 'PROFISSIONAL', 'ADMIN'
);

-- Tabela de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    usuario_id INT UNIQUE NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Serviços
CREATE TABLE IF NOT EXISTS servicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) UNIQUE NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    duracao_minutos INT NOT NULL -- Duração em minutos
);

-- Tabela de Profissionais
CREATE TABLE IF NOT EXISTS profissionais (
    id SERIAL PRIMARY KEY,
    usuario_id INT UNIQUE NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    especialidade VARCHAR(255),
    data_contratacao DATE DEFAULT CURRENT_DATE
);

-- Tabela de Profissional_Servico (para indicar quais serviços cada profissional oferece)
CREATE TABLE IF NOT EXISTS profissional_servico (
    profissional_id INT NOT NULL REFERENCES profissionais(id) ON DELETE CASCADE,
    servico_id INT NOT NULL REFERENCES servicos(id) ON DELETE CASCADE,
    PRIMARY KEY (profissional_id, servico_id)
);

-- Tabela de Horários Disponíveis dos Profissionais
CREATE TABLE IF NOT EXISTS horarios_disponiveis (
    id SERIAL PRIMARY KEY,
    profissional_id INT NOT NULL REFERENCES profissionais(id) ON DELETE CASCADE,
    data DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    UNIQUE (profissional_id, data, hora_inicio)
);

-- Tabela de Agendamentos
CREATE TABLE IF NOT EXISTS agendamentos (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
    profissional_id INT NOT NULL REFERENCES profissionais(id) ON DELETE CASCADE,
    servico_id INT NOT NULL REFERENCES servicos(id) ON DELETE CASCADE,
    data_agendamento DATE NOT NULL,
    hora_agendamento TIME NOT NULL,
    duracao_minutos INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE', -- 'PENDENTE', 'CONFIRMADO', 'CANCELADO', 'CONCLUIDO'
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT,
    UNIQUE (profissional_id, data_agendamento, hora_agendamento)
);

-- Índices para otimização de consultas
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_agendamentos_cliente_id ON agendamentos(cliente_id);
CREATE INDEX IF NOT EXISTS idx_agendamentos_profissional_id ON agendamentos(profissional_id);
CREATE INDEX IF NOT EXISTS idx_agendamentos_data_hora ON agendamentos(data_agendamento, hora_agendamento);
CREATE INDEX IF NOT EXISTS idx_horarios_disponiveis_profissional_data ON horarios_disponiveis(profissional_id, data);




-- Dados de exemplo para teste da aplicação

-- Inserir usuários de exemplo
INSERT INTO usuarios (email, senha, nome, sobrenome, telefone, tipo_usuario) VALUES
('admin@salaobeleza.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSaZWOvPqHnvl/1l1Z.jrIu', 'Administrador', 'Sistema', '(11) 99999-9999', 'ADMIN'),
('maria.silva@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSaZWOvPqHnvl/1l1Z.jrIu', 'Maria', 'Silva', '(11) 98888-8888', 'PROFISSIONAL'),
('ana.santos@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSaZWOvPqHnvl/1l1Z.jrIu', 'Ana', 'Santos', '(11) 97777-7777', 'PROFISSIONAL'),
('joao.cliente@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSaZWOvPqHnvl/1l1Z.jrIu', 'João', 'Cliente', '(11) 96666-6666', 'CLIENTE'),
('carla.cliente@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSaZWOvPqHnvl/1l1Z.jrIu', 'Carla', 'Cliente', '(11) 95555-5555', 'CLIENTE');

-- Inserir serviços
INSERT INTO servicos (nome, descricao, preco, duracao_minutos) VALUES
('Corte Feminino', 'Corte de cabelo feminino com lavagem e finalização', 50.00, 60),
('Corte Masculino', 'Corte de cabelo masculino tradicional', 30.00, 30),
('Escova', 'Escova modeladora com produtos profissionais', 40.00, 45),
('Coloração', 'Coloração completa do cabelo', 120.00, 120),
('Mechas', 'Aplicação de mechas ou luzes', 80.00, 90),
('Hidratação', 'Tratamento hidratante para cabelos', 60.00, 60),
('Manicure', 'Cuidados completos para as unhas das mãos', 25.00, 45),
('Pedicure', 'Cuidados completos para as unhas dos pés', 30.00, 60),
('Sobrancelha', 'Design e modelagem de sobrancelhas', 20.00, 30),
('Maquiagem', 'Maquiagem profissional para eventos', 80.00, 60);

-- Inserir profissionais
INSERT INTO profissionais (usuario_id, especialidade, data_contratacao) VALUES
(2, 'Cabelereiro e Colorista', '2023-01-15'),
(3, 'Manicure e Designer de Sobrancelhas', '2023-03-01');

-- Inserir clientes
INSERT INTO clientes (usuario_id, data_cadastro) VALUES
(4, '2023-06-01 10:00:00'),
(5, '2023-07-15 14:30:00');

-- Associar serviços aos profissionais
INSERT INTO profissional_servico (profissional_id, servico_id) VALUES
-- Maria Silva (cabelereira)
(1, 1), -- Corte Feminino
(1, 2), -- Corte Masculino
(1, 3), -- Escova
(1, 4), -- Coloração
(1, 5), -- Mechas
(1, 6), -- Hidratação
-- Ana Santos (manicure)
(2, 7), -- Manicure
(2, 8), -- Pedicure
(2, 9), -- Sobrancelha
(2, 10); -- Maquiagem

-- Inserir horários disponíveis para os próximos 30 dias (exemplo para Maria Silva)
INSERT INTO horarios_disponiveis (profissional_id, data, hora_inicio, hora_fim, disponivel) VALUES
-- Segunda-feira
(1, CURRENT_DATE + INTERVAL '1 day', '08:00', '09:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '09:00', '10:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '10:00', '11:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '11:00', '12:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '14:00', '15:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '15:00', '16:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '16:00', '17:00', true),
(1, CURRENT_DATE + INTERVAL '1 day', '17:00', '18:00', true),
-- Terça-feira
(1, CURRENT_DATE + INTERVAL '2 days', '08:00', '09:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '09:00', '10:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '10:00', '11:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '11:00', '12:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '14:00', '15:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '15:00', '16:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '16:00', '17:00', true),
(1, CURRENT_DATE + INTERVAL '2 days', '17:00', '18:00', true);

-- Inserir horários disponíveis para Ana Santos
INSERT INTO horarios_disponiveis (profissional_id, data, hora_inicio, hora_fim, disponivel) VALUES
-- Segunda-feira
(2, CURRENT_DATE + INTERVAL '1 day', '08:00', '09:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '09:00', '10:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '10:00', '11:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '11:00', '12:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '14:00', '15:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '15:00', '16:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '16:00', '17:00', true),
(2, CURRENT_DATE + INTERVAL '1 day', '17:00', '18:00', true),
-- Terça-feira
(2, CURRENT_DATE + INTERVAL '2 days', '08:00', '09:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '09:00', '10:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '10:00', '11:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '11:00', '12:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '14:00', '15:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '15:00', '16:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '16:00', '17:00', true),
(2, CURRENT_DATE + INTERVAL '2 days', '17:00', '18:00', true);

-- Inserir alguns agendamentos de exemplo
INSERT INTO agendamentos (cliente_id, profissional_id, servico_id, data_agendamento, hora_agendamento, duracao_minutos, status, observacoes) VALUES
(1, 1, 1, CURRENT_DATE + INTERVAL '1 day', '09:00', 60, 'CONFIRMADO', 'Cliente preferencial'),
(2, 2, 7, CURRENT_DATE + INTERVAL '2 days', '10:00', 45, 'PENDENTE', 'Primeira vez no salão');

-- Comentários sobre as senhas:
-- Todas as senhas de exemplo são: 'senha123'
-- Hash gerado com BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSaZWOvPqHnvl/1l1Z.jrIu

