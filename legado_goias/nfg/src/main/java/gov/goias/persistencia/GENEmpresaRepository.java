package gov.goias.persistencia;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.GENEmpresa;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * @author henrique-rh
 * @since 06/01/15.
 */
public abstract class GENEmpresaRepository extends GenericRepository<Integer, GENEmpresa> {

    public GENEmpresa findByCnpj(String cnpj) {
        cnpj = cnpj.substring(0,8);
        String hql = "from GENEmpresa where numeroCnpjBase = :cnpjBase";
        Query query = entityManager().createQuery(hql);
        query.setParameter("cnpjBase", cnpj);
        List empresas = query.getResultList();
        if (empresas.isEmpty()) {
            throw new NoResultException();
        } else {
            return (GENEmpresa) empresas.get(0);
        }
    }
}
