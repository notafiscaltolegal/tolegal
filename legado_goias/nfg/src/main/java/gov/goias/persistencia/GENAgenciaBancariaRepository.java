package gov.goias.persistencia;

import gov.goias.entidades.GENAgenciaBancaria;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by bruno-cff on 16/06/2015.
 */
public abstract class GENAgenciaBancariaRepository extends GenericRepository<Integer, GENAgenciaBancaria>{

    private static final Logger logger = Logger.getLogger(GENAgenciaBancariaRepository.class);

    public GENAgenciaBancaria listarAgenciaPorIdAgencia(Integer idBanco, String idAgencia){
        GENAgenciaBancaria agenciaBancaria;

        try{
            String sql = "select distinct agencia from GENAgenciaBancaria agencia join agencia.banco banco" +
                    " where banco.codigo = :idBanco and agencia.codigoAgencia = :idAgencia";
            Query query = entityManager().createQuery(sql);
            query.setParameter("idBanco", idBanco);
            query.setParameter("idAgencia", idAgencia.trim());
            agenciaBancaria = (GENAgenciaBancaria) query.getSingleResult();
        } catch (NoResultException e){
            throw new NFGException("Agência não encontrada", true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar a Agência");
        }

        return agenciaBancaria;
    }

    public List<GENAgenciaBancaria> listarAgenciaPorIdBanco(Integer idBanco){
        List<GENAgenciaBancaria> agencias;

        try{
            String sql = "select agencia from GENAgenciaBancaria agencia  join agencia.banco banco " +
                    "where banco.codigo = :idBanco";
            Query query = this.entityManager().createQuery(sql);
            query.setParameter("idBanco", idBanco);
            agencias = query.getResultList();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar as Agências");
        }
        return agencias;
    }

    public String listarNomeDaAgencia(Integer idBanco, String numeroAgencia){
        String agencia;

        String sql = "select distinct agencia.nomeAgencia from GENAgenciaBancaria agencia  join agencia.banco banco " +
                "where banco.codigo = :idBanco and agencia.codigoAgencia = :numeroAgencia";
        Query query = this.entityManager().createQuery(sql);
        query.setParameter("idBanco", idBanco);
        query.setParameter("numeroAgencia", numeroAgencia.trim());
        agencia = (String) query.getSingleResult();

        return agencia;
    }
}
