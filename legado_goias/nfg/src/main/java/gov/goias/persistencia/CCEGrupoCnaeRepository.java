package gov.goias.persistencia;

import gov.goias.entidades.CCEDivisaoCnae;
import gov.goias.entidades.CCEGrupoCnae;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by thiago-mb on 23/07/2014.
 */
public class CCEGrupoCnaeRepository extends GenericRepository<Integer, CCEGrupoCnae>{
    public List<CCEGrupoCnae> findCodGrupoByIdDivisao(Integer idFKDivisao){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CCEGrupoCnae.class);
        Root<CCEGrupoCnae> grupoCnae = criteriaQuery.from(CCEGrupoCnae.class);

        criteriaQuery.where(criteriaBuilder.equal(grupoCnae.join("divisaoCnae").get("idDivisaoCnae"), idFKDivisao));

        Query query = entityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
