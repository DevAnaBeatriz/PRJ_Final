CREATE TABLE IF NOT EXISTS pessoa (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  dt_nascimento DATE,
  ativo BOOLEAN
);

INSERT INTO pessoa (nome, dt_nascimento, ativo) VALUES
('Ana Beatriz', '2005-03-05', true),
('Rian Arcanjo', '2002-04-15', true),
('Carlos Alberto', '1975-01-01', false);
