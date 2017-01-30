CREATE TABLE tb_bilhete (id_bilhete BIGINT NOT NULL, cpf VARCHAR(255), num_seq_bilhete INTEGER, premiado SMALLINT DEFAULT 0, st_bilhete VARCHAR(255), SORTEIOTOLEGAL_id_sorteio BIGINT, PRIMARY KEY (id_bilhete))
CREATE TABLE tb_endereco (id_endereco BIGINT NOT NULL, bairro VARCHAR(255), cep INTEGER, complemento VARCHAR(255), logradouro VARCHAR(255), numero VARCHAR(255), MUNICIPIO_munibge BIGINT, PRIMARY KEY (id_endereco))
CREATE TABLE tb_nota_legal (id_nota_legal BIGINT NOT NULL, cnpj_emitente VARCHAR(255), cpf_cidadao VARCHAR(255), data_emissao TIMESTAMP, nome_fantasia VARCHAR(255), num_nota VARCHAR(255), razao_social VARCHAR(255), situacao_pontuacao VARCHAR(255), valor FLOAT, PRIMARY KEY (id_nota_legal))
CREATE TABLE tb_pes_fisica (id_pes_fisica BIGINT NOT NULL, cpf VARCHAR(255), data_nasc DATE, nome VARCHAR(255), nome_mae VARCHAR(255), ENDERECO_id_endereco BIGINT, PRIMARY KEY (id_pes_fisica))
CREATE TABLE tb_pont_bonus (id_pont_bonus BIGINT NOT NULL, cpf VARCHAR(255), data_limite_validade DATE, data_pontuacao DATE, descricao VARCHAR(255), qnt_ponto INTEGER, situacao VARCHAR(255), PRIMARY KEY (id_pont_bonus))
CREATE TABLE tb_pont_nota (id_pont_to_legl BIGINT NOT NULL, qnt_ponto INTEGER, NOTAFISCALTOLEGAL_id_nota_legal BIGINT, SORTEIOTOLEGAL_id_sorteio BIGINT, PRIMARY KEY (id_pont_to_legl))
CREATE TABLE tb_sort_cidadao (id_sort_cidadao BIGINT NOT NULL, cpf VARCHAR(255), total_bilhetes INTEGER, total_notas INTEGER, total_pontos INTEGER, SORTEIOTOLEGAL_id_sorteio BIGINT, PRIMARY KEY (id_sort_cidadao))
CREATE TABLE tb_sorteio (id_sorteio BIGINT NOT NULL, data_extracao DATE, data_sorteio DATE, num_extracao INTEGER, num_sorteio INTEGER, PRIMARY KEY (id_sorteio))
CREATE TABLE tb_usr_to_legl (id_usr_to_legl BIGINT NOT NULL, data_ativacao TIMESTAMP, data_cadastro TIMESTAMP, email VARCHAR(255), hash_ativacao VARCHAR(255), participa_sorteio SMALLINT DEFAULT 0, recebe_email SMALLINT DEFAULT 0, senha VARCHAR(255), situacao VARCHAR(255), telefone VARCHAR(255), PESSOAFISICA_id_pes_fisica BIGINT, PRIMARY KEY (id_usr_to_legl))
CREATE TABLE TBCDMN (munibge BIGINT NOT NULL, ufcdg VARCHAR(255), munnom VARCHAR(255), PRIMARY KEY (munibge))
CREATE TABLE tb_reclamacao (id_reclamacao BIGINT NOT NULL, anexo_nota BLOB(2147483647), data_cadastro TIMESTAMP, data_emissao_doc_fiscal TIMESTAMP, inscricao_estadual VARCHAR(255), motivo_reclamacao VARCHAR(255), numero_doc_fiscal VARCHAR(255), problema_empresa VARCHAR(255), tipo_doc_fiscal VARCHAR(255), valor_doc_fiscal FLOAT, id_usuario BIGINT, PRIMARY KEY (id_reclamacao))
CREATE TABLE tb_reclamacao_log (id_reclamacao_log BIGINT NOT NULL, data_reclamacao TIMESTAMP, perfil VARCHAR(255), status_reclamacao VARCHAR(255), id_reclamacao BIGINT, PRIMARY KEY (id_reclamacao_log))
CREATE TABLE tb_bloqueio_cpf (id_bloqueio_cpf BIGINT NOT NULL, cpf VARCHAR(255), cpf_adm_logado VARCHAR(255), dt_hora_bloqueio TIMESTAMP, motivo_bloqueio VARCHAR(255), nome VARCHAR(255), nome_adm_logado VARCHAR(255), situacao VARCHAR(255), PRIMARY KEY (id_bloqueio_cpf))
CREATE TABLE tb_msg_empresa (id_msg_empresa BIGINT NOT NULL, cpf_adm_logado VARCHAR(255), data_envio TIMESTAMP, mensagem VARCHAR(255), nome_adm_logado VARCHAR(255), situacao VARCHAR(255), titulo VARCHAR(255), PRIMARY KEY (id_msg_empresa))
CREATE TABLE tb_msg_visu_empresa (id_msg_visu_empresa BIGINT NOT NULL, data_leitura TIMESTAMP, inscricao_estadual VARCHAR(255), mensagem VARCHAR(255), titulo VARCHAR(255), PRIMARY KEY (id_msg_visu_empresa))
CREATE TABLE tb_embaralhamento (id_embaralhamento BIGINT NOT NULL, cpf VARCHAR(255), data_embaralhamento DATE, num_bilhete INTEGER, num_sorteio INTEGER, posicao INTEGER, PRIMARY KEY (id_embaralhamento))
CREATE TABLE tb_ganhador_sorteio (id_ganhador BIGINT NOT NULL, cpf VARCHAR(255), nome VARCHAR(255), num_bilhete INTEGER, num_sorteio INTEGER, PRIMARY KEY (id_ganhador))
ALTER TABLE tb_bilhete ADD CONSTRAINT tbblhtSRTTLGLdsrto FOREIGN KEY (SORTEIOTOLEGAL_id_sorteio) REFERENCES tb_sorteio (id_sorteio)
ALTER TABLE tb_endereco ADD CONSTRAINT tbndrcoMNCPmunibge FOREIGN KEY (MUNICIPIO_munibge) REFERENCES TBCDMN (munibge)
ALTER TABLE tb_pes_fisica ADD CONSTRAINT tbpsfscNDRCdndreco FOREIGN KEY (ENDERECO_id_endereco) REFERENCES tb_endereco (id_endereco)
ALTER TABLE tb_pont_nota ADD CONSTRAINT tbNTFSCLTLGLdntlgl FOREIGN KEY (NOTAFISCALTOLEGAL_id_nota_legal) REFERENCES tb_nota_legal (id_nota_legal)
ALTER TABLE tb_pont_nota ADD CONSTRAINT tbpntntSRTTLGLdsrt FOREIGN KEY (SORTEIOTOLEGAL_id_sorteio) REFERENCES tb_sorteio (id_sorteio)
ALTER TABLE tb_sort_cidadao ADD CONSTRAINT tbsrtcdSRTTLGLdsrt FOREIGN KEY (SORTEIOTOLEGAL_id_sorteio) REFERENCES tb_sorteio (id_sorteio)
ALTER TABLE tb_usr_to_legl ADD CONSTRAINT tbsrtlPSSFSCdpsfsc FOREIGN KEY (PESSOAFISICA_id_pes_fisica) REFERENCES tb_pes_fisica (id_pes_fisica)
ALTER TABLE tb_reclamacao ADD CONSTRAINT tbrclamacaodsuario FOREIGN KEY (id_usuario) REFERENCES tb_usr_to_legl (id_usr_to_legl)
ALTER TABLE tb_reclamacao_log ADD CONSTRAINT tbrclmcologdrclmco FOREIGN KEY (id_reclamacao) REFERENCES tb_reclamacao (id_reclamacao)
CREATE SEQUENCE seq_usr_to_legl START WITH 1
CREATE SEQUENCE seq_sorteio START WITH 1
CREATE SEQUENCE seq_pont_nota START WITH 1
CREATE SEQUENCE seq_bloqueio_cpf START WITH 1
CREATE SEQUENCE seq_msg_visu_empresa START WITH 1
CREATE SEQUENCE seq_bilhete START WITH 1
CREATE SEQUENCE seq_reclamacao START WITH 1
CREATE SEQUENCE seq_endereco START WITH 1
CREATE SEQUENCE seq_pont_bonus START WITH 1
CREATE SEQUENCE seq_pes_fisica START WITH 1
CREATE SEQUENCE seq_embaralhamento START WITH 1
CREATE SEQUENCE seq_reclamacao_log START WITH 1
CREATE SEQUENCE seq_ganhador_sorteio START WITH 1
CREATE SEQUENCE seq_sort_cidadao START WITH 1
CREATE SEQUENCE seq_msg_empresa START WITH 1
