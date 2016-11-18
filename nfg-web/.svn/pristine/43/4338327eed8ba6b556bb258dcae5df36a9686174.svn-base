package gov.goias.persistencia;

import gov.goias.entidades.CCEContribuinteCnae;

import javax.persistence.Query;
import java.io.Serializable;

/**
 * @author henrique-rh
 * @since 06/08/2014
 */
public class CCEContribuinteCnaeRepository extends GenericRepository<Integer, CCEContribuinteCnae> implements Serializable {

    public CCEContribuinteCnae findCnaePrincipal(Integer numeroInscricao) {
        String hql = "from CCEContribuinteCnae" +
                " where contribuinte.numeroInscricao = :numeroInscricao" +
                " and isPrincipal = true";

        Query query = entityManager().createQuery(hql);
        query.setParameter("numeroInscricao", numeroInscricao);
        return (CCEContribuinteCnae) query.getSingleResult();
    }
}
