CREATE SEQUENCE public.abastecimento_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE public.abastecimento (
                                      id                 BIGINT         NOT NULL,
                                      data_abastecimento DATE           NOT NULL,
                                      litragem           DECIMAL(10, 3) NOT NULL,
                                      valor_total        DECIMAL(10, 2) NOT NULL,
                                      status             VARCHAR(20)    NOT NULL,
                                      bomba_id           BIGINT         NOT NULL,
                                      cliente_id         BIGINT,        -- Coluna adicionada (Nullable = Anônimo permitido)
                                      CONSTRAINT pk_abastecimento PRIMARY KEY (id)
);

ALTER TABLE public.abastecimento
    ADD CONSTRAINT FK_ABASTECIMENTO_ON_BOMBA
        FOREIGN KEY (bomba_id) REFERENCES public.bomba_combustivel (id);

ALTER TABLE public.abastecimento
    ADD CONSTRAINT FK_ABASTECIMENTO_ON_CLIENTE
        FOREIGN KEY (cliente_id) REFERENCES public.cliente (id);