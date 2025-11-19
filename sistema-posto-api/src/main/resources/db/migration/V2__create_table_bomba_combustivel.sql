CREATE SEQUENCE public.bomba_combustivel_id_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE public.bomba_combustivel
(
    id                  BIGINT       NOT NULL,
    nome                VARCHAR(100) NOT NULL,
    tipo_combustivel_id BIGINT       NOT NULL,
    CONSTRAINT pk_bomba_combustivel PRIMARY KEY (id)
);

ALTER TABLE public.bomba_combustivel
    ADD CONSTRAINT FK_BOMBA_COMBUSTIVEL_ON_TIPO_COMBUSTIVEL
        FOREIGN KEY (tipo_combustivel_id) REFERENCES public.tipo_combustivel (id);