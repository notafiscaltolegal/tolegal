SELECT
  DISTINCT contrib.NUMR_INSCRICAO                                                                                   AS NUMR_INSCRICAO,
  empr.NOME_EMPRESAR                                                                                                AS NOME_EMPRESA,
  pj.NUMR_CNPJ                                                                                                      AS NUMR_CNPJ,
  CASE WHEN emprp.DATA_CREDENC IS NOT NULL THEN 'S' ELSE 'N' END                                                    AS IS_EMPRESA_PARTICIPANTE,
  to_char(MAX(emprp.DATA_EFETIVA_PARTCP) OVER (PARTITION BY contrib.NUMR_INSCRICAO), 'dd/MM/yyyy')                  AS DATA_EFETIVA_PARTICIPACAO,
  CASE WHEN (cnaeAuto.DATA_INCLUSAO_CNAE IS NOT NULL AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL) THEN 'S' ELSE 'N' END AS IS_CNAE_AUTORIZADO,
  CASE WHEN CURRENT_DATE >= cnaeuto.DATA_OBRIGAT_NFG AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL THEN 'S' ELSE 'N' END AS IS_CNAE_OBRIGATORIO,
  to_char(MAX(cnaeAuto.DATA_OBRIGAT_NFG) OVER (PARTITION BY contrib.NUMR_INSCRICAO), 'dd/MM/yyyy')                  AS DATA_OBRIGATORIEDADE_CNAE,
  efd.INDI_OBRIGAT                                                                                                  AS IS_EMISSOR_EFD,
  emprp.INDI_RESP_ADESAO                                                                                            AS INDI_RESP_ADESAO,
  emprp.DATA_CREDENC                                                                                                AS DATA_CREDENCIAMENTO,
  MAX(cnaeAuto.DATA_EXCLUSAO_CNAE) OVER (PARTITION BY contrib.NUMR_INSCRICAO)                                       AS LAST_DATE
FROM CCE_CONTRIBUINTE contrib,
  GEN_PESSOA_JURIDICA pj,
  GEN_EMPRESA empr,
  EFD_CONTRIBUINTE_OBRIGADO efd,
  CCE_CONTRIB_CNAEF cnaef,
  NFG_EMPRESA_PARTICIPANTE_NFG emprp,
  NFG_CNAE_AUTORIZADO cnaeAuto
WHERE
  contrib.ID_PESSOA = pj.ID_PESSOA AND
  pj.NUMR_CNPJ_BASE = empr.NUMR_CNPJ_BASE AND
  pj.ID_PESSOA = efd.ID_PESSOA AND
  contrib.NUMR_INSCRICAO = efd.NUMR_INSCRICAO AND
  contrib.NUMR_INSCRICAO = cnaef.NUMR_INSCRICAO AND
  contrib.NUMR_INSCRICAO = emprp.NUMR_INSCRICAO (+) AND
  cnaef.ID_SUBCLASSE_CNAEF = cnaeAuto.ID_SUBCLASSE_CNAEF (+) AND
  cnaef.INDI_PRINCIPAL = 'S' AND
  emprp.DATA_DESCREDENC IS NULL AND
  (
    cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL OR
      (cnaeAuto.DATA_EXCLUSAO_CNAE IS NOT NULL AND
       NOT exists(SELECT * FROM NFG_CNAE_AUTORIZADO WHERE ID_SUBCLASSE_CNAEF = cnaef.ID_SUBCLASSE_CNAEF AND DATA_EXCLUSAO_CNAE IS NULL)
      )
  ) AND
  efd.ANO_MES_REFERENCIA = '201412' AND
  pj.NUMR_CNPJ_BASE = :cnpjBase AND
  contrib.INDI_SITUACAO_CADASTRAL = 1