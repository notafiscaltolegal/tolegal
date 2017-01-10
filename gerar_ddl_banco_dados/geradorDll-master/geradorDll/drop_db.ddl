ALTER TABLE tb_bilhete DROP CONSTRAINT tbblhtSRTTLGLdsrto
ALTER TABLE tb_endereco DROP CONSTRAINT tbndrcoMNCPmunibge
ALTER TABLE tb_pes_fisica DROP CONSTRAINT tbpsfscNDRCdndreco
ALTER TABLE tb_pont_nota DROP CONSTRAINT tbNTFSCLTLGLdntlgl
ALTER TABLE tb_pont_nota DROP CONSTRAINT tbpntntSRTTLGLdsrt
ALTER TABLE tb_sort_cidadao DROP CONSTRAINT tbsrtcdSRTTLGLdsrt
ALTER TABLE tb_usr_to_legl DROP CONSTRAINT tbsrtlPSSFSCdpsfsc
ALTER TABLE tb_reclamacao DROP CONSTRAINT tbrclamacaodsuario
ALTER TABLE tb_reclamacao_log DROP CONSTRAINT tbrclmcologdrclmco
DROP TABLE tb_bilhete
DROP TABLE tb_endereco
DROP TABLE tb_nota_legal
DROP TABLE tb_pes_fisica
DROP TABLE tb_pont_bonus
DROP TABLE tb_pont_nota
DROP TABLE tb_sort_cidadao
DROP TABLE tb_sorteio
DROP TABLE tb_usr_to_legl
DROP TABLE TBCDMN
DROP TABLE tb_reclamacao
DROP TABLE tb_reclamacao_log
DROP TABLE tb_bloqueio_cpf
DROP TABLE tb_msg_empresa
DROP TABLE tb_msg_visu_empresa
DROP SEQUENCE seq_reclamacao RESTRICT
DROP SEQUENCE seq_sorteio RESTRICT
DROP SEQUENCE seq_endereco RESTRICT
DROP SEQUENCE seq_pont_bonus RESTRICT
DROP SEQUENCE seq_msg_empresa RESTRICT
DROP SEQUENCE seq_bilhete RESTRICT
DROP SEQUENCE seq_msg_visu_empresa RESTRICT
DROP SEQUENCE seq_pes_fisica RESTRICT
DROP SEQUENCE seq_bloqueio_cpf RESTRICT
DROP SEQUENCE seq_reclamacao_log RESTRICT
DROP SEQUENCE seq_pont_nota RESTRICT
DROP SEQUENCE seq_usr_to_legl RESTRICT
DROP SEQUENCE seq_sort_cidadao RESTRICT
