package gov.goias.persistencia;

import gov.goias.entidades.GENBanco;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by bruno-cff on 16/06/2015.
 */
public class GENBancoRepository extends GenericRepository<Integer, GENBanco> {

    private static final Logger logger = Logger.getLogger(GENBancoRepository.class);

    public GENBanco listarBancoPorId(Integer idBanco){
        GENBanco banco;

        try{
            String sql = "select distinct banco from GENBanco banco where banco.codigo = :idBanco";
            Query query = entityManager().createQuery(sql);
            query.setParameter("idBanco", idBanco);
            banco = (GENBanco) query.getSingleResult();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar o Banco");
        }

        return banco;
    }

    public List<GENBanco> listarBancos(){
        List<GENBanco> bancos;

        try{
            String sql = "from GENBanco banco order by banco.nome asc";
            Query query = entityManager().createQuery(sql);
            bancos = query.getResultList();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar os Bancos");
        }
        return bancos;
    }
}
