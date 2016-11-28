package gov.goias.persistencia;

import gov.goias.entidades.GENMunicipio;
import gov.goias.entidades.GENTipoLogradouro;
import org.hibernate.annotations.QueryHints;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;


public class MunicipioRepository extends GenericRepository<Integer, GENMunicipio>   {

    //TODO REFATORAR
    @Override
	public <T> List<T> list(Map<String, Object> data, String orderBy) {
        String hql = "from GENMunicipio where uf = :uf order by " + orderBy + " ASC";
        Query query = this.entityManager().createQuery(hql);
        query.setParameter("uf", data.get("uf"));
        query.setHint(QueryHints.CACHEABLE, true);
        return query.getResultList();
	}
}
