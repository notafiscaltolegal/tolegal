package gov.goias.persistencia;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.mappers.MapperDTOContribuinte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: diogo
 * Date: 8/27/13
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CCEContribuinteRepository extends GenericRepository<Integer, CCEContribuinte> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //private String anoMesReferencia = new SimpleDateFormat("yyyyMM").format(new Date());
    private String anoMesReferencia = "201312";


    public CCEContribuinte findByCnpj(String cnpj) {
        String hql = "select" +
                " new CCEContribuinte(cc.numeroInscricao, cc.indicacaoSituacaoCadastral, cc.dataInicioContratoContador, cc.dataFinalContratoContador, cc.infoContribuinte, cc.tipoContratoContador, cc.numeroExtratoCadastral, cc.dataEmissaoExtratoCadastral, cc.dataCadastramento, cc.numeroValidadorExtratoCadastro, cc.dataPrazoCerto, cc.dataInicioPrecariedade, cc.idContador, pj)" +
                " from CCEContribuinte cc, GENPessoaJuridica pj" +
                " left join cc.pessoa p" +
                " where pj.idPessoa = p.idPessoa" +
                " and pj.numeroCnpj = :cnpj";
        Query query = entityManager().createQuery(hql);
        query.setParameter("cnpj", cnpj);
        List contribuintes = query.getResultList();
        if (contribuintes.isEmpty()) {
            throw new NoResultException();
        } else {
            return (CCEContribuinte) contribuintes.get(0);
        }
    }

    public String getFodendoCnpjPelaInscricao(Integer inscricao){
        String sql = " select pj.NUMR_CNPJ from GEN_PESSOA_JURIDICA pj, CCE_CONTRIBUINTE con" +
                " where con.ID_PESSOA = pj.ID_PESSOA" +
                " and con.NUMR_INSCRICAO = ? ";
        try{
            String cnpj  = jdbcTemplate.queryForObject(sql, String.class, inscricao);
            return cnpj;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public String getFodendoNomeFantasiaPelaInscricao(Integer inscricao){
        String sql = " select pj.NOME_FANTASIA from GEN_PESSOA_JURIDICA pj, CCE_CONTRIBUINTE con" +
                " where con.ID_PESSOA = pj.ID_PESSOA" +
                " and con.NUMR_INSCRICAO = ? ";
        try{
            String nome  = jdbcTemplate.queryForObject(sql, String.class, inscricao);
            return nome;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public String getFodendoNomeFantasiaPeloCnpj(String cnpj){
        String sql = " select pj.NOME_FANTASIA from GEN_PESSOA_JURIDICA pj " +
                " where pj.NUMR_CNPJ = ? ";
        try{
            String nome  = jdbcTemplate.queryForObject(sql, String.class, cnpj);
            return nome;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    public boolean isParticipante(Integer inscricao) {
        Query query = entityManager().createQuery("select case when (count(ep.id) > 0) then true else false end" +
                " from EmpresaParticipante ep where ep.contribuinte.numeroInscricao = :inscricao" +
                " and ep.dataDescredenciamento is null");
        query.setParameter("inscricao", inscricao);

        return (Boolean) query.getSingleResult();
    }

    public Boolean contribuinteAssociadoAoContador(Integer inscricao, GENPessoaFisica contador){
        String sql = "SELECT count(contrib.NUMR_INSCRICAO) FROM " +
                "  CCE_SOCIO_CONTABILISTA cscont," +
                "  CCE_CONTRIBUINTE contrib" +
                " WHERE " +
                "  contrib.ID_PESSOA_CONTADOR = cscont.ID_PESSOA_CONTADOR (+)" +
                "  AND contrib.NUMR_INSCRICAO = ?" +
                "  AND (contrib.ID_PESSOA_CONTADOR = ? OR cscont.ID_PESSOA = ?)";
        Object[] params = {inscricao, contador.getIdPessoa(), contador.getIdPessoa()};

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    protected List<DTOContribuinte> getFromDBForContribuinte(String cnpjBase) {
        String sql = "SELECT" +
                "  DISTINCT contrib.NUMR_INSCRICAO            AS NUMR_INSCRICAO," +
                "  empr.NOME_EMPRESAR                         AS NOME_EMPRESA," +
                "  pj.NUMR_CNPJ                               AS NUMR_CNPJ," +
                "  contrib.ID_PESSOA                          AS ID_PESSOA,"+
                "  CASE WHEN emprp.DATA_CREDENC IS NOT NULL THEN 'S'" +
                "  ELSE 'N' END                               AS IS_EMPRESA_PARTICIPANTE," +
                "  MAX(emprp.DATA_EFETIVA_PARTCP) OVER (PARTITION BY contrib.NUMR_INSCRICAO)   AS DATA_EFETIVA_PARTICIPACAO," +
                "  CASE WHEN (cnaeAuto.DATA_INCLUSAO_CNAE IS NOT NULL AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL) THEN 'S'" +
                "  ELSE 'N' END                               AS IS_CNAE_AUTORIZADO," +
                "  CASE WHEN CURRENT_DATE >= cnaeAuto.DATA_OBRIGAT_NFG AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL THEN 'S'" +
                "  ELSE 'N' END                               AS IS_CNAE_OBRIGATORIO," +
                "  MAX(cnaeAuto.DATA_OBRIGAT_NFG) OVER (PARTITION BY contrib.NUMR_INSCRICAO)   AS DATA_OBRIGATORIEDADE_CNAE," +
                "  cnaeAuto.ID_CNAE_AUTORIZADO                AS ID_CNAE_AUTORIZADO," +
                "  cnaeAuto.ID_SUBCLASSE_CNAEF                AS ID_SUBCLASSE_CNAEF," +
                "  efd.INDI_OBRIGAT                           AS IS_EMISSOR_EFD," +
                "  emprp.INDI_RESP_ADESAO                     AS INDI_RESP_ADESAO," +
                "  emprp.DATA_CREDENC                         AS DATA_CREDENCIAMENTO," +
                "  MAX(cnaeAuto.DATA_EXCLUSAO_CNAE) OVER (PARTITION BY contrib.NUMR_INSCRICAO) AS LAST_DATE" +
                " FROM CCE_CONTRIBUINTE contrib," +
                "  GEN_PESSOA_JURIDICA pj," +
                "  GEN_EMPRESA empr," +
                "  EFD_CONTRIBUINTE_OBRIGADO efd," +
                "  CCE_CONTRIB_CNAEF cnaef," +
                "  NFG_EMPRESA_PARTICIPANTE_NFG emprp," +
                "  NFG_CNAE_AUTORIZADO cnaeAuto" +
                " WHERE contrib.ID_PESSOA = pj.ID_PESSOA" +
                "      AND pj.NUMR_CNPJ_BASE = empr.NUMR_CNPJ_BASE" +
                "      AND pj.ID_PESSOA = efd.ID_PESSOA" +
                "      AND contrib.NUMR_INSCRICAO = efd.NUMR_INSCRICAO" +
                "      AND contrib.NUMR_INSCRICAO = cnaef.NUMR_INSCRICAO" +
                "      AND contrib.NUMR_INSCRICAO = emprp.NUMR_INSCRICAO (+)" +
                "      AND cnaef.ID_SUBCLASSE_CNAEF = cnaeAuto.ID_SUBCLASSE_CNAEF (+)" +
//                "      AND cnaef.INDI_PRINCIPAL = 'S'" +
                "      AND emprp.DATA_DESCREDENC IS NULL" +
                // Quando o cnae nunca foi cadastrado, no left join ele virá null. Quando exclui o cnae, ele nao virá no left join, pois ele existe, mas a data de exclusao nao e null.
                "      AND (cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL OR (cnaeAuto.DATA_EXCLUSAO_CNAE IS NOT NULL AND NOT exists(SELECT ID_CNAE_AUTORIZADO FROM NFG_CNAE_AUTORIZADO WHERE ID_SUBCLASSE_CNAEF = cnaef.ID_SUBCLASSE_CNAEF AND DATA_EXCLUSAO_CNAE IS NULL)))" +
//mock bruno
//                "      AND efd.ANO_MES_REFERENCIA = 201110" +
//                "      AND pj.NUMR_CNPJ_BASE = '02347193'" +

//                "      AND pj.NUMR_CNPJ_BASE = '01081108'" +
                "      AND efd.ANO_MES_REFERENCIA = ?" +
                "      AND pj.NUMR_CNPJ_BASE = ?" +
                "      AND contrib.INDI_SITUACAO_CADASTRAL = 1";
        //TODO: comentado para teste
        Object[] params = {anoMesReferencia, cnpjBase};
        return jdbcTemplate.query(sql, new MapperDTOContribuinte(), params);
//        return jdbcTemplate.query(sql, new MapperDTOContribuinte());
    }

    protected List<DTOContribuinte> getFromDBForContador(GENPessoaFisica contador) {
        String sql = "SELECT" +
                "  DISTINCT contrib.NUMR_INSCRICAO            AS NUMR_INSCRICAO," +
                "  empr.NOME_EMPRESAR                         AS NOME_EMPRESA," +
                "  pj.NUMR_CNPJ                               AS NUMR_CNPJ," +
                "  contrib.ID_PESSOA                          AS ID_PESSOA,"+
                "  CASE WHEN emprp.DATA_CREDENC IS NOT NULL THEN 'S'" +
                "  ELSE 'N' END                               AS IS_EMPRESA_PARTICIPANTE," +
                "  MAX(emprp.DATA_EFETIVA_PARTCP) OVER (PARTITION BY contrib.NUMR_INSCRICAO) AS DATA_EFETIVA_PARTICIPACAO," +
                "  CASE WHEN (cnaeAuto.DATA_INCLUSAO_CNAE IS NOT NULL AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL) THEN 'S'" +
                "  ELSE 'N' END                               AS IS_CNAE_AUTORIZADO," +
                "  CASE WHEN CURRENT_DATE >= cnaeAuto.DATA_OBRIGAT_NFG AND cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL THEN 'S'" +
                "  ELSE 'N' END                               AS IS_CNAE_OBRIGATORIO," +
                "  MAX(cnaeAuto.DATA_OBRIGAT_NFG) OVER (PARTITION BY contrib.NUMR_INSCRICAO) AS DATA_OBRIGATORIEDADE_CNAE," +
                "  cnaeAuto.ID_CNAE_AUTORIZADO                AS ID_CNAE_AUTORIZADO," +
                "  cnaeAuto.ID_SUBCLASSE_CNAEF                AS ID_SUBCLASSE_CNAEF," +
                "  efd.INDI_OBRIGAT                           AS IS_EMISSOR_EFD," +
                "  emprp.INDI_RESP_ADESAO                     AS INDI_RESP_ADESAO," +
                "  emprp.DATA_CREDENC                         AS DATA_CREDENCIAMENTO," +
                "  MAX(cnaeAuto.DATA_EXCLUSAO_CNAE) OVER (PARTITION BY contrib.NUMR_INSCRICAO) AS LAST_DATE" +
                " FROM CCE_CONTRIBUINTE contrib," +
                "  GEN_PESSOA_JURIDICA pj," +
                "  GEN_EMPRESA empr," +
                "  EFD_CONTRIBUINTE_OBRIGADO efd," +
                "  CCE_CONTRIB_CNAEF cnaef," +
                "  NFG_EMPRESA_PARTICIPANTE_NFG emprp," +
                "  NFG_CNAE_AUTORIZADO cnaeAuto," +
                "  CCE_SOCIO_CONTABILISTA cscont" +
                " WHERE contrib.ID_PESSOA = pj.ID_PESSOA" +
                "      AND pj.NUMR_CNPJ_BASE = empr.NUMR_CNPJ_BASE" +
                "      AND pj.ID_PESSOA = efd.ID_PESSOA" +
                "      AND contrib.NUMR_INSCRICAO = efd.NUMR_INSCRICAO" +
                "      AND contrib.NUMR_INSCRICAO = cnaef.NUMR_INSCRICAO" +
                "      AND contrib.NUMR_INSCRICAO = emprp.NUMR_INSCRICAO (+)" +
                "      AND cnaef.ID_SUBCLASSE_CNAEF = cnaeAuto.ID_SUBCLASSE_CNAEF (+)" +
                "      AND contrib.ID_PESSOA_CONTADOR = cscont.ID_PESSOA_CONTADOR (+)" +
//                "      AND cnaef.INDI_PRINCIPAL = 'S'" +
                "      AND emprp.DATA_DESCREDENC IS NULL" +
                "      AND (cnaeAuto.DATA_EXCLUSAO_CNAE IS NULL OR (cnaeAuto.DATA_EXCLUSAO_CNAE IS NOT NULL AND NOT exists(SELECT ID_CNAE_AUTORIZADO FROM NFG_CNAE_AUTORIZADO WHERE ID_SUBCLASSE_CNAEF = cnaef.ID_SUBCLASSE_CNAEF AND DATA_EXCLUSAO_CNAE IS NULL)))" +
                "      AND efd.ANO_MES_REFERENCIA = ?" +
//                "      AND efd.ANO_MES_REFERENCIA = 201110" +
                "      AND contrib.INDI_SITUACAO_CADASTRAL = 1"+
                "      AND (contrib.ID_PESSOA_CONTADOR = ? OR cscont.ID_PESSOA = ?)";
//                "      AND (contrib.ID_PESSOA_CONTADOR = '728993' OR cscont.ID_PESSOA = '728993')";
//                "      AND (contrib.ID_PESSOA_CONTADOR = '477883' OR cscont.ID_PESSOA = '477883')";
        //TODO: comentado para teste
        Object[] params = {anoMesReferencia, contador.getIdPessoa(), contador.getIdPessoa()};
//        return jdbcTemplate.query(sql, new MapperDTOContribuinte(), params);
        return jdbcTemplate.query(sql, new MapperDTOContribuinte());
    }

    public Boolean contribuintesPossuiCNAEValido(Integer inscricao){
        Query query = entityManager().createQuery(
                "select case when (count(contribuinteCnae) > 0) then true else false end from CnaeAutorizado cnaeFiscal, CCEContribuinteCnae contribuinteCnae " +
                "join cnaeFiscal.subClasseCnae cnae " +
                "where contribuinteCnae.subClasseCnae.idSubClasseCnae = cnaeFiscal.subClasseCnae.idSubClasseCnae and " +
                "contribuinteCnae.contribuinte.numeroInscricao = :inscricao ");

        query.setParameter("inscricao", inscricao);

        return (Boolean) query.getSingleResult();
    }


}