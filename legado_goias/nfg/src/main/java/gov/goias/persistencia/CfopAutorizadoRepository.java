package gov.goias.persistencia;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.CfopAutorizado;
import gov.goias.entidades.CnaeAutorizado;
import gov.goias.entidades.GENCfop;
import gov.goias.persistencia.util.HqlTools;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author henrique-rh
 * @since 23/09/2014
 */
public class CfopAutorizadoRepository extends GenericRepository<Integer, CnaeAutorizado> implements Serializable {

    public List<CfopAutorizado> listCfopsAutorizadosNaoExcluidos(Map<String, Object> params){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CnaeAutorizado.class);
        Root<CfopAutorizado> cfopAutorizado = criteriaQuery.from(CfopAutorizado.class);

        criteriaQuery.where(cfopAutorizado.get("dataExclusaoCfop").isNull());

        Query query = entityManager().createQuery(criteriaQuery);

        if(!StringUtils.isEmpty(params.get("start"))){
            query.setFirstResult(Integer.valueOf(params.get("start").toString()));
        }

        if(!StringUtils.isEmpty(params.get("max"))){
            query.setMaxResults(Integer.valueOf(params.get("max").toString()));
        }

        return query.getResultList();
    }

    public List<CfopAutorizado> list(String codigoCFOP,String descricaoCFOP)
    {
        HqlTools hqlTools = new HqlTools();

        hqlTools.appendHql(" from CfopAutorizado cfop ");
        hqlTools.appendHql(" where ");

        hqlTools.appendHqlIfNotNull(""," cfop.dataExclusaoCfop = null ");
        hqlTools.appendHqlIfNotNull(codigoCFOP," cfop.genCfop.codigoCFOP like '"+codigoCFOP+"'");
        hqlTools.appendHqlIfNotNull(descricaoCFOP," cfop.genCfop.descricaoCFOP like '%"+descricaoCFOP+"%'");

        hqlTools.appendHql(" order by cfop.genCfop.codigoCFOP ");

        Query query = entityManager().createQuery(hqlTools.getHql());

        return query.getResultList();
    }

    public Long countCfopsAutorizadosNaoExcluidos(){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CfopAutorizado.class);
        Root<CfopAutorizado> cfopAutorizado = criteriaQuery.from(CfopAutorizado.class);
        criteriaQuery.select(criteriaBuilder.count(cfopAutorizado));

        criteriaQuery.where(cfopAutorizado.get("dataExclusaoCfop").isNull());

        TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
