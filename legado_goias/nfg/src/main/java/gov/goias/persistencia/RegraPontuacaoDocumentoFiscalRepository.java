package gov.goias.persistencia;

import gov.goias.entidades.RegraPontuacaoDocumentoFiscal;
import gov.goias.entidades.RegraSorteio;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by bruno-cff on 28/05/2015.
 */
public class RegraPontuacaoDocumentoFiscalRepository extends GenericRepository<Integer, RegraPontuacaoDocumentoFiscal> {

    private static final Logger logger = Logger.getLogger(RegraPontuacaoDocumentoFiscalRepository.class);

    public List<RegraPontuacaoDocumentoFiscal> list() {
        List<RegraPontuacaoDocumentoFiscal> regras;
        try {
            String hql = "from RegraPontuacaoDocumentoFiscal regra  order by regra.nome DESC";
            Query query = this.entityManager().createQuery(hql);
            regras= (List<RegraPontuacaoDocumentoFiscal>) query.getResultList();

        } catch (Exception e) {
            throw new NFGException("Algo de errado ocorreu ao tentar listar regras de pontuacao");
        }

        return regras;
    }


    public Date retornaDataFimRegra(Integer idSorteio) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        //TODO Mock de contingência para produção
        switch (idSorteio) {
            case 1: return format.parse("28-02-2015");
            case 3: return format.parse("31-03-2015");
            case 4: return format.parse("30-04-2015");
            case 5: return format.parse("31-05-2015");
            case 6: return format.parse("30-06-2015");
            case 7: return format.parse("30-07-2015");
            case 8: return format.parse("30-08-2015");
            case 9: return format.parse("30-09-2015");
            case 10: return format.parse("30-09-2015");
            case 11: return format.parse("31-10-2015");
            case 12: return format.parse("30-11-2015");
            case 13: return format.parse("31-12-2015");
            case 14: return format.parse("31-01-2016");
            case 15: return format.parse("29-02-2016");
            case 16: return format.parse("31-03-2016");
            case 17: return format.parse("30-04-2016");
            case 18: return format.parse("31-05-2016");
            case 19: return format.parse("30-06-2016");
            case 20: return format.parse("31-07-2016");
            case 21: return format.parse("31-08-2016");
            case 22: return format.parse("30-09-2016");
            default: return new Date();
        }
//        Date dataFimRegra;
//
//        try {
//
//            String sql = "SELECT regraPontuacao.data_fim_regra " +
//                    "       FROM nfg_regra_pontuacao_doc_fiscal regraPontuacao " +
//                    "       INNER JOIN nfg_pontuacao_doc_fiscal_pes pontuacaoDocFiscal ON regraPontuacao.id_regra_pontuacao_doc_fiscal = pontuacaoDocFiscal.id_regra_pontuacao_doc_fiscal " +
//                    "       INNER JOIN nfg_pontuacao_pessoa pontuacaoPessoa ON pontuacaoPessoa.id_pontuacao_pessoa = pontuacaoDocFiscal.id_pontuacao_pessoa " +
//                    "       INNER JOIN nfg_sorteio_pontuacao sorteioPontuacao ON pontuacaoPessoa.id_pontuacao_pessoa = sorteioPontuacao.id_pontuacao_pessoa " +
//                    "       WHERE sorteioPontuacao.id_regra_sorteio = :idSorteio" +
//                    "       AND ROWNUM = 1";
//
//            Query query = entityManager().createNativeQuery(sql);
//            query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("");
//            query.setParameter("idSorteio", idSorteio);
//
//            dataFimRegra = (Date) query.getSingleResult();
//        } catch (NoResultException e){
//            return null;
//        } catch (Exception e){
//            throw new NFGException("Algo de errado ocorreu ao tentar retornar a data fim da regra");
//        }
//
//        return dataFimRegra;
    }
}
