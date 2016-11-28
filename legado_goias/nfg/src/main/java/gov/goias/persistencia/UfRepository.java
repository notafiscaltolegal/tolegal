package gov.goias.persistencia;

import gov.goias.entidades.GENUf;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


public class UfRepository extends GenericRepository {

	@SuppressWarnings("unchecked")
	public List<GENUf> listar(String orderByColumn) throws Exception {
		
		List<GENUf> ufs;
		
		try {
			String hql = "from GENUf where codgUf != 'EX' order by " + orderByColumn + " ASC";
			Query query = this.entityManager().createQuery(hql);
			query.setHint(QueryHints.CACHEABLE, true);
			ufs= (List<GENUf>) query.getResultList();
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return ufs;
	}
}
