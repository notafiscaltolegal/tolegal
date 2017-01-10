SELECT
  DISTINCT contrib.NUMR_INSCRICAO                                                                    AS NUMR_INSCRICAO,
  empr.NOME_EMPRESAR                                                                                 AS NOME_EMPRESA,
  pj.NUMR_CNPJ                                                                                       AS NUMR_CNPJ,
  pj.NOME_FANTASIA                                                                                   AS NOME_FANTASIA,
  to_char(MAX(emprp.DATA_EFETIVA_PARTCP) OVER (PARTITION BY contrib.NUMR_INSCRICAO), 'dd/MM/yyyy')   AS DATA_EFETIVA_PARTICIPACAO,
  emprp.INDI_RESP_ADESAO                                                                             AS INDI_RESP_ADESAO,
  emprp.DATA_CREDENC                                                                                 AS DATA_CREDENCIAMENTO,
  MAX(cnaeAuto.DATA_EXCLUSAO_CNAE) OVER (PARTITION BY contrib.NUMR_INSCRICAO)                        AS LAST_DATE,
  ccend.CODG_MUNICIPIO                                                                               AS CODG_MUNICIPIO,
  ccend.NOME_MUNICIPIO                                                                               AS NOME_MUNICIPIO,
  ccend.NOME_BAIRRO                                                                                  AS NOME_BAIRRO,
  subcnae.DESC_SUBCLASSE_CNAEF                                                                       AS DESCRICAO_CNAE,
  cnaef.ID_SUBCLASSE_CNAEF                                                                           AS ID_SUBCLASSE_CNAE
 FROM CCE_CONTRIBUINTE contrib,
  GEN_PESSOA_JURIDICA pj,
  GEN_EMPRESA empr,
  EFD_CONTRIBUINTE_OBRIGADO efd,
  CCE_CONTRIB_CNAEF cnaef,
  NFG_EMPRESA_PARTICIPANTE_NFG emprp,
  NFG_CNAE_AUTORIZADO cnaeAuto,
  CCE_ENDERECO_ESTAB_CONTRIB ccend,
  CCE_SUBCLASSE_CNAE_FISCAL subcnae
 WHERE contrib.ID_PESSOA = pj.ID_PESSOA
      AND pj.NUMR_CNPJ_BASE = empr.NUMR_CNPJ_BASE
      AND pj.ID_PESSOA = efd.ID_PESSOA
      AND contrib.NUMR_INSCRICAO = efd.NUMR_INSCRICAO
      AND contrib.NUMR_INSCRICAO = cnaef.NUMR_INSCRICAO
      AND contrib.NUMR_INSCRICAO = emprp.NUMR_INSCRICAO (+)
      AND cnaef.ID_SUBCLASSE_CNAEF = cnaeAuto.ID_SUBCLASSE_CNAEF (+)
--                "      AND cnaef.INDI_PRINCIPAL = 'S'
      AND emprp.DATA_DESCREDENC IS NULL
      AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL
      AND efd.ANO_MES_REFERENCIA = :anoMesReferencia
      AND contrib.INDI_SITUACAO_CADASTRAL = 1
      AND (efd.INDI_OBRIGAT = 'S' OR emprp.DATA_CREDENC IS NOT NULL OR CURRENT_DATE >= cnaeAuto.DATA_OBRIGAT_NFG)
      AND cnaeAuto.DATA_INCLUSAO_CNAE IS NOT NULL
      AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL
      AND ccend.NUMR_INSCRICAO = contrib.NUMR_INSCRICAO
      AND subcnae.ID_SUBCLASSE_CNAEF = cnaef.ID_SUBCLASSE_CNAEF
      ORDER by empr.NOME_EMPRESAR