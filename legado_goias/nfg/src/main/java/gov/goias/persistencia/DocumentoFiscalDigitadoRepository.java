package gov.goias.persistencia;

import gov.goias.entidades.DocumentoFiscalDigitado;
import gov.goias.persistencia.util.HqlTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Remisson Silva on 30/09/2014.
 */
public abstract class DocumentoFiscalDigitadoRepository extends GenericRepository<Integer, DocumentoFiscalDigitadoRepository>
{
    @SuppressWarnings("unchecked")


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public List<DocumentoFiscalDigitado> list(Integer inscricaoEstadual,Integer tipoDocumentoFiscal,
                                              Integer numeroDocumentoFiscal,String dataEmissao,
                                              Integer serieNotaFiscal,
                                              String subSerieNotaFiscal,
                                              String cpf,
                                              Integer offset,
                                              Integer max,
                                              Boolean ordenaPorUltimoInserido
    ){
        logger.info("NOTA DIGITADA => ENTROU NO METODO DE LISTAGEM" );
        HqlTools hqlTools = new HqlTools();

        hqlTools.appendHql(" from DocumentoFiscalDigitado doc ");
        hqlTools.appendHql(" where ");

        hqlTools.appendHqlIfNotNull(inscricaoEstadual, " doc.contribuinte.numeroInscricao = " + inscricaoEstadual);
        hqlTools.appendHqlIfNotNull(cpf," doc.cpf = '" + cpf+"'");
        hqlTools.appendHqlIfNotNull(tipoDocumentoFiscal," doc.tipoDocumentoFiscal = " + tipoDocumentoFiscal);
        hqlTools.appendHqlIfNotNull(numeroDocumentoFiscal," doc.numeroDocumentoFiscal = " + numeroDocumentoFiscal);
        hqlTools.appendHqlIfNotNullAndNotEmpty(subSerieNotaFiscal," doc.subSerieNotaFiscal like '" + subSerieNotaFiscal +"'");
        hqlTools.appendHqlIfNotNull(serieNotaFiscal," doc.serieNotaFiscal = " + serieNotaFiscal);
        hqlTools.appendHqlIfNotNullAndNotEmpty(dataEmissao, " doc.dataEmissao = " + "TO_DATE('" + dataEmissao + "','dd/MM/yyyy')");


        logger.info("NOTA DIGITADA => FILTRO CONSULTA " +
                "IE{" + inscricaoEstadual + "} TIPO{" + tipoDocumentoFiscal + "} NUMERO{" + numeroDocumentoFiscal + "} " +
                "EMISSAO{" + dataEmissao + "} SERIE{" + serieNotaFiscal + "} SUBSERIE{" + subSerieNotaFiscal + "} ");

        if (ordenaPorUltimoInserido){
            hqlTools.appendHqlIfNotNull(inscricaoEstadual, " doc.dataCancelDocumentoFiscal = null ");
            hqlTools.appendHql(" order by doc.dataInclucaoDoctoFiscal desc ");
        }else{
            if(dataEmissao != null){
                hqlTools.appendHql(" order by doc.dataEmissao ");
            }
        }

        List<DocumentoFiscalDigitado> result = new ArrayList<DocumentoFiscalDigitado>();
        if(numeroDocumentoFiscal != null || (dataEmissao != null && dataEmissao.length()>0) || serieNotaFiscal != null || subSerieNotaFiscal != null
                || cpf !=null || inscricaoEstadual != null){
            logger.info("ENRTEI AQUI DURANTE A CONSULTA DE DOC");
            Query query = entityManager().createQuery(hqlTools.getHql());
            if (offset!=null){
                query.setFirstResult(offset);
            }
            if (max!=null){
                query.setMaxResults(max);
            }


            final List resultList = query.getResultList();
            logger.info("CONSULTA DE DOCS "+ resultList.size()+" RESULTADOS");
            result = resultList;

        }
        return result;
    }


    public Long countUltimosInseridos(Integer inscricaoEstadual,
                                      Integer numeroDocumentoFiscal,String dataEmissao,

                                      String cpf
    ){
        HqlTools hqlTools = new HqlTools();

        hqlTools.appendHql(" select count(*) ");
        hqlTools.appendHql(" from DocumentoFiscalDigitado doc ");
        hqlTools.appendHql(" where ");

        hqlTools.appendHqlIfNotNull(inscricaoEstadual," doc.contribuinte.numeroInscricao = " + inscricaoEstadual);
        hqlTools.appendHqlIfNotNull(cpf," doc.cpf = '" + cpf+"'");
        hqlTools.appendHqlIfNotNull(numeroDocumentoFiscal," doc.numeroDocumentoFiscal = " + numeroDocumentoFiscal);
        hqlTools.appendHqlIfNotNullAndNotEmpty(dataEmissao, " doc.dataEmissao = " + "TO_DATE('" + dataEmissao + "','dd/MM/yyyy')");

        hqlTools.appendHqlIfNotNull(inscricaoEstadual, " doc.dataCancelDocumentoFiscal = null ");
        hqlTools.appendHql(" order by doc.dataInclucaoDoctoFiscal desc ");

        List<DocumentoFiscalDigitado> result = new ArrayList<DocumentoFiscalDigitado>();
        if(numeroDocumentoFiscal != null || (dataEmissao != null && dataEmissao.length()>0)
                || cpf !=null || inscricaoEstadual != null){
            Query query = entityManager().createQuery(hqlTools.getHql());

            return (Long) query.getSingleResult();

        }else{
            return new Long(0);
        }
    }
}
