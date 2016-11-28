package gov.goias.persistencia;

import gov.goias.entidades.CCEDivisaoCnae;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by thiago-mb on 23/07/2014.
 */
public class CCEDivisaoCnaeRepository extends GenericRepository<Integer, CCEDivisaoCnae> {
    public List<CCEDivisaoCnae> findCodDivisaoByIdSecao(Integer idFKSecao){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CCEDivisaoCnae.class);
        Root<CCEDivisaoCnae> divisaoCnae = criteriaQuery.from(CCEDivisaoCnae.class);

        criteriaQuery.where(criteriaBuilder.equal(divisaoCnae.join("secaoCnae").get("idSecaoCnae"), idFKSecao));

        Query query = entityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }

}
