package gov.goias.persistencia;

import gov.goias.entidades.CCEClasseCnae;
import gov.goias.entidades.CCESubClasseCnae;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by thiago-mb on 23/07/2014.
 */
public class CCESubClasseCnaeRepository extends GenericRepository<Integer, CCESubClasseCnae>{
    public List<CCESubClasseCnae> findCodSubClasseByIdClasse(Integer idFKClasse){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CCESubClasseCnae.class);
        Root<CCESubClasseCnae> subClasseCnae = criteriaQuery.from(CCESubClasseCnae.class);

        criteriaQuery.where(criteriaBuilder.equal(subClasseCnae.join("classeCnae").get("idClasseCnae"), idFKClasse));

        Query query = entityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
