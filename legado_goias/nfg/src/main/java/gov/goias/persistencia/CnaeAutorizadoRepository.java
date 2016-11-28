package gov.goias.persistencia;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.CCESubClasseCnae;
import gov.goias.entidades.CnaeAutorizado;
import org.hibernate.annotations.QueryHints;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by thiago-mb on 29/07/2014.
 */
public class CnaeAutorizadoRepository extends GenericRepository<Integer, CnaeAutorizado> implements Serializable {

    public List<CnaeAutorizado> listCnaesAutorizadosNaoExcluidos() {
        return listCnaesAutorizadosNaoExcluidos(null);
    }

    public List<CnaeAutorizado> listCnaesAutorizadosNaoExcluidos(Map<String, Object> params){
        String hql = "from CnaeAutorizado cnae " +
                "join fetch cnae.subClasseCnae " +
                "join fetch cnae.subClasseCnae.classeCnae " +
                "join fetch cnae.subClasseCnae.classeCnae.grupoCnae " +
                "join fetch cnae.subClasseCnae.classeCnae.grupoCnae.divisaoCnae " +
                "join fetch cnae.subClasseCnae.classeCnae.grupoCnae.divisaoCnae.secaoCnae " +
                "where cnae.dataExclusaoCnae is null";

        Query query = entityManager().createQuery(hql);
        if (params != null) {
            if (!StringUtils.isEmpty(params.get("start"))) {
                query.setFirstResult(Integer.valueOf(params.get("start").toString()));
            }

            if (!StringUtils.isEmpty(params.get("max"))) {
                query.setMaxResults(Integer.valueOf(params.get("max").toString()));
            }
        }
        query.setHint(QueryHints.CACHEABLE, true);

        return query.getResultList();
    }

    public List<CnaeAutorizado> listCnaesAutorizadosNaoExcluidos(String codigoSubclasse, Map<String, Object> params){
        String hql = "from CnaeAutorizado cnae " +
                "join fetch cnae.subClasseCnae " +
                "join fetch cnae.subClasseCnae.classeCnae " +
                "join fetch cnae.subClasseCnae.classeCnae.grupoCnae " +
                "join fetch cnae.subClasseCnae.classeCnae.grupoCnae.divisaoCnae " +
                "join fetch cnae.subClasseCnae.classeCnae.grupoCnae.divisaoCnae.secaoCnae " +
                "where cnae.dataExclusaoCnae is null " +
                "and cnae.subClasseCnae.codSubClasseCnae like :codigoSubclasse";

        Query query = entityManager().createQuery(hql);
        query.setParameter("codigoSubclasse", codigoSubclasse.concat("%"));

        if(!StringUtils.isEmpty(params.get("start"))){
            query.setFirstResult(Integer.valueOf(params.get("start").toString()));
        }

        if(!StringUtils.isEmpty(params.get("max"))){
            query.setMaxResults(Integer.valueOf(params.get("max").toString()));
        }

        return query.getResultList();
    }

    public Long countCnaesAutorizadosNaoExcluidos(){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CnaeAutorizado.class);
        Root<CnaeAutorizado> cnaeAutorizado = criteriaQuery.from(CnaeAutorizado.class);
        criteriaQuery.select(criteriaBuilder.count(cnaeAutorizado));

        criteriaQuery.where(cnaeAutorizado.get("dataExclusaoCnae").isNull());

        TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    public Long countCnaesAutorizadosNaoExcluidos(String codigoSubclasse){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CnaeAutorizado.class);
        Root<CnaeAutorizado> cnaeAutorizado = criteriaQuery.from(CnaeAutorizado.class);
        criteriaQuery.select(criteriaBuilder.count(cnaeAutorizado));

        criteriaQuery.where(criteriaBuilder.and(
                cnaeAutorizado.get("dataExclusaoCnae").isNull(),
                criteriaBuilder.like(cnaeAutorizado.get("subClasseCnae").<String>get("codSubClasseCnae"), codigoSubclasse+"%")
        ));

        TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    public CnaeAutorizado findCnaeAutorizadoByIdCnaeAutorizado(Integer idCnaeAutorizado){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CnaeAutorizado.class);
        Root<CnaeAutorizado> cnaeAutorizado = criteriaQuery.from(CnaeAutorizado.class);

        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(cnaeAutorizado.get("idCnaeAutorizado"), idCnaeAutorizado),
                        criteriaBuilder.isNull(cnaeAutorizado.get("dataExclusaoCnae"))
                )
        );
        TypedQuery<CnaeAutorizado> query = entityManager().createQuery(criteriaQuery);

        return (CnaeAutorizado) query.getSingleResult();
    }

    public Boolean verifyIfExist(Long idSubClasseCnae, CnaeAutorizado cnaeAutorizado){
        Query query = entityManager().createQuery("select case when (count(cf) > 0) then true else false end" +
                " from CnaeAutorizado cf where cf.subClasseCnae.idSubClasseCnae = :idsubclassecnae" +
                " and cf.dataExclusaoCnae is null");
        query.setParameter("idsubclassecnae", idSubClasseCnae);

        return (Boolean) query.getSingleResult();
    }

    public void createFromDivisao(Integer divisao, Date dataObrigatoriedade){
        String sql = "insert into NFG_CNAE_AUTORIZADO (DATA_INCLUSAO_CNAE, DATA_OBRIGAT_NFG, ID_SUBCLASSE_CNAEF) ";
        sql +="select :dataInclusao, :dataObrigatoriedade, subclasse.ID_SUBCLASSE_CNAEF from CCE_SUBCLASSE_CNAE_FISCAL subclasse left join CCE_CLASSE_CNAE_FISCAL classe on subclasse.ID_CLASSE_CNAEF = classe.ID_CLASSE_CNAEF left join CCE_GRUPO_CNAE_FISCAL grupo on classe.ID_GRUPO_CNAEF = grupo.ID_GRUPO_CNAEF left join CCE_DIVISAO_CNAE_FISCAL divisao on grupo.ID_DIVISAO_CNAEF = divisao.ID_DIVISAO_CNAEF";
        sql +=" where divisao.ID_DIVISAO_CNAEF = :divisao ";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("NFG_CNAE_AUTORIZADO");

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        String hoje = format.format(new Date());
        query.setParameter("dataInclusao", hoje);
        query.setParameter("dataObrigatoriedade", format.format(dataObrigatoriedade));

        query.setParameter("divisao", divisao);

        query.executeUpdate();
    }

    public void createFromGrupo(Integer grupo, Date dataObrigatoriedade){
        String sql = "insert into NFG_CNAE_AUTORIZADO (DATA_INCLUSAO_CNAE, DATA_OBRIGAT_NFG, ID_SUBCLASSE_CNAEF) ";
        sql +="select :dataInclusao, :dataObrigatoriedade, subclasse.ID_SUBCLASSE_CNAEF from CCE_SUBCLASSE_CNAE_FISCAL subclasse left join CCE_CLASSE_CNAE_FISCAL classe on subclasse.ID_CLASSE_CNAEF = classe.ID_CLASSE_CNAEF left join CCE_GRUPO_CNAE_FISCAL grupo on classe.ID_GRUPO_CNAEF = grupo.ID_GRUPO_CNAEF";
        sql +=" where grupo.ID_GRUPO_CNAEF = :grupo ";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("NFG_CNAE_AUTORIZADO");

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        String hoje = format.format(new Date());
        query.setParameter("dataInclusao", hoje);
        query.setParameter("dataObrigatoriedade", format.format(dataObrigatoriedade));

        query.setParameter("grupo", grupo);

        query.executeUpdate();
    }

    public void createFromClasse(Integer classe, Date dataObrigatoriedade){
        String sql = "insert into NFG_CNAE_AUTORIZADO (DATA_INCLUSAO_CNAE, DATA_OBRIGAT_NFG, ID_SUBCLASSE_CNAEF) ";
        sql +="select :dataInclusao, :dataObrigatoriedade, subclasse.ID_SUBCLASSE_CNAEF from CCE_SUBCLASSE_CNAE_FISCAL subclasse left join CCE_CLASSE_CNAE_FISCAL classe on subclasse.ID_CLASSE_CNAEF = classe.ID_CLASSE_CNAEF";
        sql +=" where classe.ID_CLASSE_CNAEF = :classe ";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("NFG_CNAE_AUTORIZADO");

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        String hoje = format.format(new Date());
        query.setParameter("dataInclusao", hoje);
        query.setParameter("dataObrigatoriedade", format.format(dataObrigatoriedade));

        query.setParameter("classe", classe);

        query.executeUpdate();
    }

    public void insertCnaeAutorizado(Long idSubclassCnae, Date dataObrigatoriedade) {
        String sql = "insert into NFG_CNAE_AUTORIZADO (DATA_INCLUSAO_CNAE, DATA_OBRIGAT_NFG, ID_SUBCLASSE_CNAEF) values (:dataInclusao, :dataObrigatoriedade, :classe) ";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("NFG_CNAE_AUTORIZADO");

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
        String hoje = format.format(new Date());
        query.setParameter("dataInclusao", hoje);
        query.setParameter("dataObrigatoriedade", format.format(dataObrigatoriedade));

        query.setParameter("classe", idSubclassCnae);

        query.executeUpdate();
    }

    public Boolean possuiContribuintesCadastrados(Long idCnaeAutorizado) {
        Query query = entityManager().createQuery(
                "select count(cnaeAutorizado) from CnaeAutorizado cnaeAutorizado " +
                "where cnaeAutorizado.subClasseCnae.idSubClasseCnae in " +
                "(select cnae.subClasseCnae.idSubClasseCnae from EmpresaParticipante empresaParticipante " +
                "join empresaParticipante.contribuinte contribuinte, CCEContribuinteCnae cnae where cnae.contribuinte = contribuinte and cnae.subClasseCnae.idSubClasseCnae = :id) ");

        query.setParameter("id", idCnaeAutorizado);

        Long quantidade = (Long) query.getSingleResult();
        return quantidade!=null && quantidade >0;
    }

    public CnaeAutorizado findForContribuinte(CCEContribuinte contribuinte) {
        Query query = entityManager().createQuery("" +
                "select CnaeAutorizado from CnaeAutorizado cnaeAutorizado, CCEContribuinteCnae contribuinteCnae " +
                "join cnaeAutorizado.subClasseCnae cnae " +
                "where cnaeAutorizado.dataExclusaoCnae is null and " +
                "contribuinteCnae.subClasseCnae = cnaeAutorizado.subClasseCnae and " +
                "contribuinteCnae.contribuinte.numeroInscricao = :inscricao " +
                "and contribuinteCnae.indiPrincipal = 'S'");

        query.setParameter("inscricao", contribuinte.getNumeroInscricao());
        return (CnaeAutorizado) query.getSingleResult();
    }

     public Boolean isAutorizado(String cnae) {
        Query query = entityManager().createQuery(
                "select case when (count(cnaeAutorizado) > 0) then true else false end from CnaeAutorizado cnaeAutorizado " +
                        "join cnaeAutorizado.subClasseCnae cnae " +
                        "where cnae.codSubClasseCnae = :cnae ");

        query.setParameter("cnae", cnae);

        return (Boolean) query.getSingleResult();
    }
}
