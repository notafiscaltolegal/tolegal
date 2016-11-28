package gov.goias.persistencia;

import gov.goias.entidades.ContaBancariaPremio;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.PremioBilhete;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by bruno-cff on 16/06/2015.
 */
public abstract class ContaBancariaPremioRepository extends GenericRepository<Integer, ContaBancariaPremio>{

    public  ContaBancariaPremio consultarContaPorBilhetePremiado(PremioBilhete premioBilhete){

        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<ContaBancariaPremio> contaBancariaPremio = criteriaQuery.from(ContaBancariaPremio.class);
        criteriaQuery.where(criteriaBuilder.equal(contaBancariaPremio.join("premioBilhete").get("id"),  premioBilhete.getId()));
        TypedQuery<ContaBancariaPremio> query = entityManager().createQuery(criteriaQuery);

        try{
            return query.getSingleResult();
        }catch (NoResultException nre){
            return null;
        }

    }

}
