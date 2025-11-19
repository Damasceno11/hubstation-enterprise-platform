CREATE SEQUENCE public.tipo_combustivel_id_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE public.tipo_combustivel
(
    id          BIGINT                      NOT NULL,
    nome        VARCHAR(100)                NOT NULL,
    preco_litro DECIMAL(10, 2)              NOT NULL,
    CONSTRAINT pk_tipo_combustivel PRIMARY KEY (id)
);

-- Adiciona uma constraint de unicidade para o nome
ALTER TABLE public.tipo_combustivel
    ADD CONSTRAINT uc_tipo_combustivel_nome UNIQUE (nome);