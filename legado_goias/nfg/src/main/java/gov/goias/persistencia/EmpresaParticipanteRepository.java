package gov.goias.persistencia;

import gov.goias.dtos.DTOEmpresaParticipante;
import gov.goias.entidades.EmpresaParticipante;
import gov.goias.mappers.MapperDTOEmpresaParticipante;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: diogo
 * Date: 7/18/2014
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EmpresaParticipanteRepository extends GenericRepository<Integer, EmpresaParticipante> {

    private String anoMesReferencia = new SimpleDateFormat("yyyyMM").format(new Date());
//    private String anoMesReferencia = "201112";

    Logger logger = Logger.getLogger(EmpresaParticipante.class);

    public EmpresaParticipante findEmpresaComParticipacaoAtivaPorInscricao(Integer inscricao) {
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(EmpresaParticipante.class);
        Root<EmpresaParticipante> empresa = criteriaQuery.from(EmpresaParticipante.class);

        criteriaQuery.where(
            criteriaBuilder.and(
                criteriaBuilder.equal(empresa.join("contribuinte").get("numeroInscricao"), inscricao),
                criteriaBuilder.isNull(empresa.get("dataDescredenciamento"))
            )
        );

        TypedQuery<EmpresaParticipante> query = entityManager().createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    protected synchronized List<DTOEmpresaParticipante> getEmpresasParticipantesFromDB() {
        Date inicio = new Date();
        String sql = "SELECT * FROM NFG_CONTRIBUINTE_PARTICIPANTE";

        List<DTOEmpresaParticipante> lista = jdbcTemplate.query(sql, new MapperDTOEmpresaParticipante());
        Long tempo = new Date().getTime() - inicio.getTime();
        logger.info("Tempo consulta: " + tempo + "ms");
        return lista;
    }

    protected synchronized List<DTOEmpresaParticipante> getEmpresasParticipantesFromDBOld() {
        Date inicio = new Date();
        String sql = "SELECT " +
                "  tab_contrib.NUMR_INSCRICAO                                                                                 AS NUMR_INSCRICAO, " +
                "  empr.NOME_EMPRESAR                                                                                         AS NOME_EMPRESA, " +
                "  pj.NUMR_CNPJ                                                                                               AS NUMR_CNPJ, " +
                "  pj.NOME_FANTASIA                                                                                           AS NOME_FANTASIA, " +
                "  TO_CHAR(MAX(tab_contrib.DATA_EFETIVA_PARTCP) OVER (PARTITION BY tab_contrib.NUMR_INSCRICAO), 'dd/MM/yyyy') AS DATA_EFETIVA_PARTICIPACAO, " +
                "  tab_contrib.INDI_RESP_ADESAO                                                                               AS INDI_RESP_ADESAO, " +
                "  tab_contrib.DATA_CREDENC                                                                                   AS DATA_CREDENCIAMENTO, " +
                "  MAX(tab_contrib.DATA_EXCLUSAO_CNAE) OVER (PARTITION BY tab_contrib.NUMR_INSCRICAO)                         AS LAST_DATE, " +
                "  muni.CODG_MUNICIPIO                                                                                        AS CODG_MUNICIPIO, " +
                "  muni.NOME_MUNICIPIO                                                                                        AS NOME_MUNICIPIO, " +
                "  nvl(ccend.NOME_BAIRRO, bairro.NOME_BAIRRO)                                                                 AS NOME_BAIRRO, " +
                "  tab_contrib.DESC_SUBCLASSE_CNAEF                                                                           AS DESCRICAO_CNAE, " +
                "  tab_contrib.ID_SUBCLASSE_CNAEF                                                                             AS ID_SUBCLASSE_CNAE " +
                "FROM (WITH with_cnae AS (SELECT " +
                "                            contr.NUMR_INSCRICAO, " +
                "                            contr.id_pessoa, " +
                "                            cnaeAuto.DATA_OBRIGAT_NFG, " +
                "                            cnaeAuto.DATA_EXCLUSAO_CNAE, " +
                "                            subcnae.DESC_SUBCLASSE_CNAEF, " +
                "                            cnaef.ID_SUBCLASSE_CNAEF " +
                "                          FROM CCE_CONTRIBUINTE contr, " +
                "                            CCE_CONTRIB_CNAEF cnaef, " +
                "                            CCE_SUBCLASSE_CNAE_FISCAL subcnae, " +
                "                            NFG_CNAE_AUTORIZADO cnaeAuto " +
                "                          WHERE contr.NUMR_INSCRICAO = cnaef.NUMR_INSCRICAO " +
                "                                AND subcnae.ID_SUBCLASSE_CNAEF = cnaef.ID_SUBCLASSE_CNAEF " +
                "                                AND cnaef.ID_SUBCLASSE_CNAEF = cnaeAuto.ID_SUBCLASSE_CNAEF " +
                "                                AND contr.INDI_SITUACAO_CADASTRAL = 1 " +
                "                                AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL " +
                "                                AND cnaeAuto.DATA_INCLUSAO_CNAE IS NOT NULL), " +
                "           with_efd AS (SELECT " +
                "                          efd.NUMR_INSCRICAO, " +
                "                          WITH_CNAE.id_pessoa, " +
                "                          WITH_CNAE.DATA_OBRIGAT_NFG, " +
                "                          WITH_CNAE.DATA_EXCLUSAO_CNAE, " +
                "                          WITH_CNAE.DESC_SUBCLASSE_CNAEF, " +
                "                          WITH_CNAE.ID_SUBCLASSE_CNAEF " +
                "                        FROM " +
                "                          EFD_CONTRIBUINTE_OBRIGADO efd, " +
                "                          WITH_CNAE " +
                "                        WHERE efd.ANO_MES_REFERENCIA = ? " +
                "                              AND efd.INDI_OBRIGAT = 'S' " +
                "                              AND with_cnae.numr_inscricao = efd.numr_inscricao), " +
                "           with_emprp AS (SELECT emprp.NUMR_INSCRICAO, " +
                "                            WITH_CNAE.id_pessoa, " +
                "                            emprp.DATA_EFETIVA_PARTCP, " +
                "                            WITH_CNAE.DATA_EXCLUSAO_CNAE, " +
                "                            emprp.INDI_RESP_ADESAO, " +
                "                            emprp.DATA_CREDENC, " +
                "                            WITH_CNAE.DATA_OBRIGAT_NFG, " +
                "                            WITH_CNAE.DESC_SUBCLASSE_CNAEF, " +
                "                            WITH_CNAE.ID_SUBCLASSE_CNAEF " +
                "                          FROM " +
                "                            NFG_EMPRESA_PARTICIPANTE_NFG emprp, " +
                "                            WITH_CNAE " +
                "                          WHERE emprp.DATA_CREDENC IS NOT NULL " +
                "                                AND emprp.DATA_DESCREDENC IS NULL " +
                "                                AND with_cnae.numr_inscricao = emprp.numr_inscricao) " +
                "      SELECT NUMR_INSCRICAO, " +
                "        ID_PESSOA, " +
                "        DATA_OBRIGAT_NFG, " +
                "        DATA_EXCLUSAO_CNAE, " +
                "        DESC_SUBCLASSE_CNAEF, " +
                "        ID_SUBCLASSE_CNAEF, " +
                "        NULL DATA_EFETIVA_PARTCP, " +
                "        NULL INDI_RESP_ADESAO, " +
                "        NULL DATA_CREDENC " +
                "      FROM with_efd   efd " +
                "      UNION " +
                "      SELECT NUMR_INSCRICAO, " +
                "        ID_PESSOA, " +
                "        DATA_OBRIGAT_NFG, " +
                "        DATA_EXCLUSAO_CNAE, " +
                "        DESC_SUBCLASSE_CNAEF, " +
                "        ID_SUBCLASSE_CNAEF, " +
                "        DATA_EFETIVA_PARTCP, " +
                "        INDI_RESP_ADESAO, " +
                "        DATA_CREDENC " +
                "      FROM with_emprp emprp " +
                "      UNION " +
                "      SELECT NUMR_INSCRICAO, " +
                "        ID_PESSOA, " +
                "        DATA_OBRIGAT_NFG, " +
                "        DATA_EXCLUSAO_CNAE, " +
                "        DESC_SUBCLASSE_CNAEF, " +
                "        ID_SUBCLASSE_CNAEF, " +
                "        NULL DATA_EFETIVA_PARTCP, " +
                "        NULL INDI_RESP_ADESAO, " +
                "        NULL DATA_CREDENC " +
                "      FROM with_cnae  cnaeAuto " +
                "      WHERE NOT exists (SELECT 1 FROM with_efd efd1 WHERE efd1.numr_inscricao = cnaeAuto.numr_inscricao) " +
                "            AND NOT exists (SELECT 1 FROM with_emprp emprp1 WHERE emprp1.numr_inscricao = cnaeAuto.numr_inscricao) " +
                "            AND cnaeAuto.DATA_OBRIGAT_NFG <= trunc(sysdate) " +
                "     ) tab_contrib, " +
                "  CCE_ESTAB_CONTRIBUINTE  ccestab, " +
                "  GEN.GEN_ENDERECO        ccend, " +
                "  GEN.GEN_LOGRADOURO      ccelogr, " +
                "  GEN.GEN_MUNICIPIO       muni, " +
                "  GEN.GEN_PESSOA_JURIDICA pj, " +
                "  GEN.GEN_EMPRESA         empr, " +
                "  GEN.GEN_NOME_BAIRRO     bairro " +
                "WHERE " +
                "  ccestab.NUMR_INSCRICAO              = tab_contrib.NUMR_INSCRICAO " +
                "  AND ccestab.id_endereco             = ccend.id_endereco " +
                "  AND ccend.codg_logradouro           = ccelogr.codg_logradouro(+) " +
                "  AND muni.codg_municipio             = nvl(ccelogr.codg_municipio,ccend.codg_municipio) " +
                "  AND tab_contrib.ID_PESSOA           = pj.ID_PESSOA " +
                "  AND pj.NUMR_CNPJ_BASE               = empr.NUMR_CNPJ_BASE " +
                "  AND ccelogr.codg_bairro             = bairro.codg_bairro(+) ";


        Object[] params = {anoMesReferencia};
        List<DTOEmpresaParticipante> lista = jdbcTemplate.query(sql, new MapperDTOEmpresaParticipante(), params);
        Long tempo = new Date().getTime() - inicio.getTime();
        logger.info("Tempo consulta: " + tempo + "ms");
        return lista;
    }
}