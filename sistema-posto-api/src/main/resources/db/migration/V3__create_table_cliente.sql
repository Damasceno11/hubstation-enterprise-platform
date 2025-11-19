CREATE SEQUENCE public.cliente_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE public.cliente (
                                id     BIGINT       NOT NULL,
                                nome   VARCHAR(255) NOT NULL,
                                cpf    VARCHAR(11)  NOT NULL,
                                email  VARCHAR(255),
                                CONSTRAINT pk_cliente PRIMARY KEY (id)
);

ALTER TABLE public.cliente ADD CONSTRAINT uc_cliente_cpf UNIQUE (cpf);
ALTER TABLE public.cliente ADD CONSTRAINT uc_cliente_email UNIQUE (email);