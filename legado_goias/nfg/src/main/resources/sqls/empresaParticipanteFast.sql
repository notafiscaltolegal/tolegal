SELECT
  tab_contrib.NUMR_INSCRICAO                                                                                 AS NUMR_INSCRICAO,
  empr.NOME_EMPRESAR                                                                                         AS NOME_EMPRESA,
  pj.NUMR_CNPJ                                                                                               AS NUMR_CNPJ,
  pj.NOME_FANTASIA                                                                                           AS NOME_FANTASIA,
  TO_CHAR(MAX(tab_contrib.DATA_EFETIVA_PARTCP) OVER (PARTITION BY tab_contrib.NUMR_INSCRICAO), 'dd/MM/yyyy') AS DATA_EFETIVA_PARTICIPACAO,
  tab_contrib.INDI_RESP_ADESAO                                                                               AS INDI_RESP_ADESAO,
  tab_contrib.DATA_CREDENC                                                                                   AS DATA_CREDENCIAMENTO,
  MAX(tab_contrib.DATA_EXCLUSAO_CNAE) OVER (PARTITION BY tab_contrib.NUMR_INSCRICAO)                         AS LAST_DATE,
  muni.CODG_MUNICIPIO                                                                                        AS CODG_MUNICIPIO,
  muni.NOME_MUNICIPIO                                                                                        AS NOME_MUNICIPIO,
  nvl(ccend.NOME_BAIRRO, bairro.NOME_BAIRRO)                                                                 AS NOME_BAIRRO,
  tab_contrib.DESC_SUBCLASSE_CNAEF                                                                           AS DESCRICAO_CNAE,
  tab_contrib.ID_SUBCLASSE_CNAEF                                                                             AS ID_SUBCLASSE_CNAE
FROM (WITH with_cnae AS (SELECT
                            contr.NUMR_INSCRICAO,
                            contr.id_pessoa,
                            cnaeAuto.DATA_OBRIGAT_NFG,
                            cnaeAuto.DATA_EXCLUSAO_CNAE,
                            subcnae.DESC_SUBCLASSE_CNAEF,
                            cnaef.ID_SUBCLASSE_CNAEF
                          FROM CCE_CONTRIBUINTE contr,
                            CCE_CONTRIB_CNAEF cnaef,
                            CCE_SUBCLASSE_CNAE_FISCAL subcnae,
                            NFG_CNAE_AUTORIZADO cnaeAuto
                          WHERE contr.NUMR_INSCRICAO = cnaef.NUMR_INSCRICAO
                                AND subcnae.ID_SUBCLASSE_CNAEF = cnaef.ID_SUBCLASSE_CNAEF
                                AND cnaef.ID_SUBCLASSE_CNAEF = cnaeAuto.ID_SUBCLASSE_CNAEF
                                AND contr.INDI_SITUACAO_CADASTRAL = 1
                                AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL
                                AND cnaeAuto.DATA_INCLUSAO_CNAE IS NOT NULL),
           with_efd AS (SELECT
                          efd.NUMR_INSCRICAO,
                          WITH_CNAE.id_pessoa,
                          WITH_CNAE.DATA_OBRIGAT_NFG,
                          WITH_CNAE.DATA_EXCLUSAO_CNAE,
                          WITH_CNAE.DESC_SUBCLASSE_CNAEF,
                          WITH_CNAE.ID_SUBCLASSE_CNAEF
                        FROM
                          EFD_CONTRIBUINTE_OBRIGADO efd,
                          WITH_CNAE
                        WHERE efd.ANO_MES_REFERENCIA = ?
                              AND efd.INDI_OBRIGAT = 'S'
                              AND with_cnae.numr_inscricao = efd.numr_inscricao),
           with_emprp AS (SELECT emprp.NUMR_INSCRICAO,
                            WITH_CNAE.id_pessoa,
                            emprp.DATA_EFETIVA_PARTCP,
                            WITH_CNAE.DATA_EXCLUSAO_CNAE,
                            emprp.INDI_RESP_ADESAO,
                            emprp.DATA_CREDENC,
                            WITH_CNAE.DATA_OBRIGAT_NFG,
                            WITH_CNAE.DESC_SUBCLASSE_CNAEF,
                            WITH_CNAE.ID_SUBCLASSE_CNAEF
                          FROM
                            NFG_EMPRESA_PARTICIPANTE_NFG emprp,
                            WITH_CNAE
                          WHERE emprp.DATA_CREDENC IS NOT NULL
                                AND emprp.DATA_DESCREDENC IS NULL
                                AND with_cnae.numr_inscricao = emprp.numr_inscricao)
      SELECT NUMR_INSCRICAO,
        ID_PESSOA,
        DATA_OBRIGAT_NFG,
        DATA_EXCLUSAO_CNAE,
        DESC_SUBCLASSE_CNAEF,
        ID_SUBCLASSE_CNAEF,
        null DATA_EFETIVA_PARTCP,
        null INDI_RESP_ADESAO,
        null DATA_CREDENC
      FROM with_efd   efd
      UNION
      SELECT NUMR_INSCRICAO,
        ID_PESSOA,
        DATA_OBRIGAT_NFG,
        DATA_EXCLUSAO_CNAE,
        DESC_SUBCLASSE_CNAEF,
        ID_SUBCLASSE_CNAEF,
        DATA_EFETIVA_PARTCP,
        INDI_RESP_ADESAO,
        DATA_CREDENC
      FROM with_emprp emprp
      UNION
      SELECT NUMR_INSCRICAO,
        ID_PESSOA,
        DATA_OBRIGAT_NFG,
        DATA_EXCLUSAO_CNAE,
        DESC_SUBCLASSE_CNAEF,
        ID_SUBCLASSE_CNAEF,
        null DATA_EFETIVA_PARTCP,
        null INDI_RESP_ADESAO,
        null DATA_CREDENC
      FROM with_cnae  cnaeAuto
      WHERE NOT exists (select 1 from with_efd efd1 where efd1.numr_inscricao = cnaeAuto.numr_inscricao)
            AND NOT exists (select 1 from with_emprp emprp1 where emprp1.numr_inscricao = cnaeAuto.numr_inscricao)
            AND cnaeAuto.DATA_OBRIGAT_NFG <= trunc(sysdate)
     ) tab_contrib,
  CCE_ESTAB_CONTRIBUINTE  ccestab,
  GEN.GEN_ENDERECO        ccend,
  GEN.GEN_LOGRADOURO      ccelogr,
  GEN.GEN_MUNICIPIO       muni,
  GEN.GEN_PESSOA_JURIDICA pj,
  GEN.GEN_EMPRESA         empr,
  GEN.GEN_NOME_BAIRRO     bairro
WHERE
  ccestab.NUMR_INSCRICAO              = tab_contrib.NUMR_INSCRICAO
  and ccestab.id_endereco             = ccend.id_endereco
  and ccend.codg_logradouro           = ccelogr.codg_logradouro(+)
  and muni.codg_municipio             = nvl(ccelogr.codg_municipio,ccend.codg_municipio)
  and tab_contrib.ID_PESSOA           = pj.ID_PESSOA
  and pj.NUMR_CNPJ_BASE               = empr.NUMR_CNPJ_BASE
  and ccelogr.codg_bairro             = bairro.codg_bairro(+);


select * from CCE_CONTRIBUINTE contr WHERE rownum < 2;
select * from CCE_CONTRIB_CNAEF cnaef WHERE rownum < 2;
select * from CCE_SUBCLASSE_CNAE_FISCAL subcnae WHERE rownum < 2;
select * from NFG_CNAE_AUTORIZADO cnaeAut WHERE rownum < 2;
select * from EFD_CONTRIBUINTE_OBRIGADO efd WHERE rownum < 2;
select * from CCE_ESTAB_CONTRIBUINTE  ccestab WHERE rownum < 2;
select * from GEN.GEN_ENDERECO        ccend WHERE rownum < 2;
select * from GEN.GEN_LOGRADOURO      ccelogr WHERE rownum < 2;
select * from GEN.GEN_MUNICIPIO       muni WHERE rownum < 2;
select * from GEN.GEN_PESSOA_JURIDICA pj WHERE rownum < 2;
select * from GEN.GEN_EMPRESA         empr WHERE rownum < 2;
select * from GEN.GEN_NOME_BAIRRO     bairr WHERE rownum < 2;