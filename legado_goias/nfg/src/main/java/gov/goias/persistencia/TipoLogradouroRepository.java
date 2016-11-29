package gov.goias.persistencia;

import gov.goias.entidades.GENTipoLogradouro;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


public class TipoLogradouroRepository extends GenericRepository   {

	public List<GENTipoLogradouro> listar(String orderByColumn) throws Exception {
		
		List<GENTipoLogradouro> tipoLograds;
		try {
			String hql = "from GENTipoLogradouro order by "+orderByColumn+ " ASC";
			Query query = this.entityManager().createQuery(hql);
			query.setHint(QueryHints.CACHEABLE, true);
			tipoLograds= (List<GENTipoLogradouro>) query.getResultList();

		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return tipoLograds;
	}
}