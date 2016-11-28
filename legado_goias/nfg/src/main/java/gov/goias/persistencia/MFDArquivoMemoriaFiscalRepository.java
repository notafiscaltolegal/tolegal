package gov.goias.persistencia;

import gov.goias.entidades.MFDArquivoMemoriaFiscal;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno-cff on 01/07/2015.
 */
public class MFDArquivoMemoriaFiscalRepository extends GenericRepository<Integer, MFDArquivoMemoriaFiscal> {

    private static final Logger logger = Logger.getLogger(MFDArquivoMemoriaFiscalRepository.class);

    public List listarFiltro(Integer inscricao, Integer referenciaInicio, Integer referenciaFim, Integer max, Integer page, Boolean count){
        List<MFDArquivoMemoriaFiscal> list;

        try{
            String sql = "select mfd from MFDArquivoMemoriaFiscal mfd where mfd.numeroInscricao = :inscricao" +
                    "   and mfd.referencia between :referenciaInicio and :referenciaFim order by mfd.referencia";

            Query query = entityManager().createQuery(sql);
            query.setParameter("inscricao", inscricao);

            query.setParameter("referenciaInicio", referenciaInicio);
            query.setParameter("referenciaFim", referenciaFim);

            if (count){
                List countList = new ArrayList();
                countList.add(new Integer(query.getResultList().size()));
                return countList;
            }

            query.setFirstResult(page * max);
            query.setMaxResults(max);

            list = query.getResultList();

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar filtros MFDArquivoMemoriaFiscal");
        }

        return list;
    }

    public List listarTodos(Integer inscricao, Integer max, Integer page, Boolean count){
        List list;

        try{
            String sql = "select mfd from MFDArquivoMemoriaFiscal mfd where mfd.numeroInscricao = :inscricao order by mfd.referencia DESC, mfd.dataInicioApuracao desc ";

            Query query = entityManager().createQuery(sql);
            query.setParameter("inscricao", inscricao);

            if (count){
                List countList = new ArrayList();
                countList.add(new Integer(query.getResultList().size()));
                return countList;
            }

            query.setFirstResult(page * max);
            query.setMaxResults(max);

            list = query.getResultList();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar MFDArquivoMemoriaFiscal.");
        }

        return list;
    }
}
