package gov.goias.persistencia;

import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.RFEPessoaFisica;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created with IntelliJ IDEA.
 * User: lucas-mp
 * Date: 23/10/14
 * To change this template use File | Settings | File Templates.
 */
public abstract class RFEPessoaFisicaRepository extends GenericRepository<Long, RFEPessoaFisica> implements IRepositorioPessoaFisica{

    public RFEPessoaFisica findByCpf(String cpf){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<RFEPessoaFisica> pessoa = criteriaQuery.from(RFEPessoaFisica.class);
        criteriaQuery.where(criteriaBuilder.equal(pessoa.get("cpf"),  cpf ));

        TypedQuery<RFEPessoaFisica> query = entityManager().createQuery(criteriaQuery);

        try{
            return query.getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

}