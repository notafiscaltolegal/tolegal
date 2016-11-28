package gov.goias.persistencia;

import gov.goias.entidades.CCEClasseCnae;
import gov.goias.entidades.CCEGrupoCnae;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by thiago-mb on 23/07/2014.
 */
public class CCEClasseCnaeRepository extends GenericRepository<Integer, CCEClasseCnae> {
    public List<CCEClasseCnae> findCodClasseByIdGrupo(Integer idFKGrupo){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CCEClasseCnae.class);
        Root<CCEClasseCnae> classeCnae = criteriaQuery.from(CCEClasseCnae.class);

        criteriaQuery.where(criteriaBuilder.equal(classeCnae.join("grupoCnae").get("idGrupoCnae"), idFKGrupo));

        Query query = entityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
