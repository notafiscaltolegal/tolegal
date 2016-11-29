package gov.goias.persistencia;

import gov.goias.dtos.DTOBilhetePessoa;
import gov.goias.entidades.BilhetePessoa;
import gov.goias.entidades.PremioBilhete;
import gov.goias.entidades.PremioSorteio;
import gov.goias.exceptions.NFGException;
import gov.goias.mappers.MapperDTOBilhetePessoa;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.*;

/**
 * Created by bruno-cff on 07/05/2015.
 */
public class BilhetePessoaRepository extends GenericRepository<Integer, BilhetePessoa> {

    private static final Logger logger = Logger.getLogger(BilhetePessoaRepository.class);

    public Map findBilhete(Long numeroBilhete, Integer idRegra) {
        return getBilhetes(numeroBilhete, idRegra);
    }

    private Map getBilhetes(Long numeroBilhete, Integer idRegra) {
        String sql = "SELECT " +
                "   bp.NUMR_SEQUENCIAL_BILHETE AS NUM_BILHETE," +
                "   bp.ID_BILHETE_PESSOA AS ID_BILHETE," +
                "   rs.INFO_SORTEIO AS NUM_SORTEIO," +
                "   pf.NUMR_CPF AS CPF, " +
                "   pf.NOME_PESSOA AS NOME_PESSOA," +
                "   ecp.NOME_MUNICIPIO AS MUNICIPIO, " +
                "   ecp.NOME_UF AS UF, " +
                "   ecp.NOME_BAIRRO AS BAIRRO" +
                " FROM " +
                "   GEN_PESSOA_FISICA pf, " +
                "   NFG_PESSOA_PARTICIPANTE pp, " +
                "   NFG_BILHETE_PESSOA bp, " +
                "   GEN_ENDERECO_COMPLETO_PESSOA ecp, " +
                "   NFG_REGRA_SORTEIO rs " +
                " WHERE pp.ID_PESSOA = pf.ID_PESSOA" +
                "      AND bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT" +
                "      AND ecp.ID_PESSOA (+) = pf.ID_PESSOA" +
                "      AND rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO" +
                "      AND bp.NUMR_SEQUENCIAL_BILHETE = ? AND bp.ID_REGRA_SORTEIO = ? and rownum < 2";
        Object[] params = {numeroBilhete, idRegra};
        try {
            return jdbcTemplate.queryForMap(sql, params);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Map<String, Object>> listBilhetes(Integer idRegra) {
        return listBilhetes(idRegra, null);
    }

    public Map<String, Object> getBilhete(Integer idRegra, Long numeroBilhete) {
        List<Map<String, Object>> bilhetes = listBilhetes(idRegra, numeroBilhete);
        if (bilhetes.isEmpty()) {
            return null;
        } else {
            return bilhetes.get(0);
        }
    }

    private List<Map<String, Object>> listBilhetes(Integer idRegra, Long numeroBilhete) {
        //language=SQL
        String sql = "SELECT " +
                "   bp.NUMR_SEQUENCIAL_BILHETE AS NUM_BILHETE," +
                "   pb.ID_PREMIO_BILHETE AS ID_PREMIO_BILHETE," +
                "   rs.INFO_SORTEIO AS NUM_SORTEIO," +
                "   pf.NUMR_CPF AS CPF, " +
                "   pf.NOME_PESSOA AS NOME_PESSOA," +
                "   ecp.NOME_MUNICIPIO AS MUNICIPIO, " +
                "   ecp.NOME_UF AS UF, " +
                "   ecp.NOME_BAIRRO AS BAIRRO," +
                "   pb.NUMR_PREMIO_PROGRAMA_SORTEIO AS NUMERO_PREMIO," +
                "   ps.VALR_PREMIO AS VALOR_PREMIO" +
                " FROM " +
                "   GEN_PESSOA_FISICA pf, " +
                "   NFG_PESSOA_PARTICIPANTE pp, " +
                "   NFG_BILHETE_PESSOA bp, " +
                "   GEN_ENDERECO_COMPLETO_PESSOA ecp, " +
                "   NFG_REGRA_SORTEIO rs," +
                "   NFG_PREMIO_BILHETE pb," +
                "   NFG_PREMIO_SORTEIO ps " +
                " WHERE pp.ID_PESSOA = pf.ID_PESSOA" +
                "      AND bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT" +
                "      AND ecp.ID_PESSOA = pf.ID_PESSOA" +
                "      AND ecp.ID_ENDERECO = (SELECT ID_ENDERECO from GEN_PESSOA_ENDERECO WHERE ID_PESSOA = pf.ID_PESSOA AND rownum < 2)" +
                "      AND rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO" +
                "      AND bp.ID_BILHETE_PESSOA= pb.ID_BILHETE_PESSOA" +
                "      AND ps.ID_PREMIO_SORTEIO = pb.ID_PREMIO_SORTEIO" +
                (numeroBilhete != null ? " AND bp.NUMR_SEQUENCIAL_BILHETE = ?" : "") +
                "      AND bp.ID_REGRA_SORTEIO = ? ORDER BY pb.NUMR_PREMIO_PROGRAMA_SORTEIO DESC";

        Object[] params;
        if (numeroBilhete != null ) {
            params = new Object[]{numeroBilhete, idRegra};
        } else {
            params = new Object[]{idRegra};
        };
        return jdbcTemplate.queryForList(sql, params);
    }

    public List<BilhetePessoa> listBilhetes(Integer idCidadao, Integer idSorteio, Integer max, Integer page){
        List<BilhetePessoa> bilhetes ;
        try{
            String hql = "from BilhetePessoa b where b.pessoaParticipante.id = :idCidadao and b.regraSorteio.id = :idSorteio";
            Query query = this.entityManager().createQuery(hql);
            query.setParameter("idCidadao", idCidadao);
            query.setParameter("idSorteio", idSorteio);

            if (max!=null && page!=null){
                query.setMaxResults(max);
                query.setFirstResult(page * max);
            }

            bilhetes = (List<BilhetePessoa>) query.getResultList();
        }catch (Exception e){
            throw new NFGException("Algo de errado ocorreu ao tentar listar os bilhetes");
        }
        return bilhetes;
    }

    public List<DTOBilhetePessoa> listBilheteMap(Integer idCidadao, Integer idSorteio, Integer max, Integer page, int nrColunas){
        try{
            List listaDeMapBilhetes = new ArrayList();

            List<DTOBilhetePessoa> bilhetesPremiados = listarBilhetesPremiados(max, page, nrColunas, idSorteio, idCidadao);

            logger.info("NR. DE BILHETES ENCONTRADOS: " + bilhetesPremiados.size() + " serao mostrados em " + nrColunas + " colunas.");

            for (int i=0; i<bilhetesPremiados.size() ; i+=nrColunas){
                Map<String,DTOBilhetePessoa> mapDeBilhetes = new HashMap<String, DTOBilhetePessoa>();
                if (i <= bilhetesPremiados.size()-1)
                    mapDeBilhetes.put("bilhete1col",(DTOBilhetePessoa) bilhetesPremiados.get(i));

                if (i+1 <= bilhetesPremiados.size()-1)
                    mapDeBilhetes.put("bilhete2col",(DTOBilhetePessoa) bilhetesPremiados.get(i+1));

                if (i+2 <= bilhetesPremiados.size()-1)
                    mapDeBilhetes.put("bilhete3col",(DTOBilhetePessoa) bilhetesPremiados.get(i+2));

//                if (i+3 <= bilhetesPremiados.size()-1)
//                    mapDeBilhetes.put("bilhete4col",(DTOBilhetePessoa) bilhetesPremiados.get(i+3));
//
//                if (i+4 <= bilhetesPremiados.size()-1)
//                    mapDeBilhetes.put("bilhete5col",(DTOBilhetePessoa) bilhetesPremiados.get(i+4));

                listaDeMapBilhetes.add( mapDeBilhetes);
            }

            return  listaDeMapBilhetes;
        }catch (Exception e){
            throw new NFGException("Algo de errado ocorreu ao tentar listar os bilhetes");
        }
    }

    private List<DTOBilhetePessoa> listarBilhetesPremiados(Integer max, Integer page, int nrColunas, Integer idSorteio, Integer idCidadao){
        logger.info("LISTAR BILHETES PARTCT."+idCidadao+" Id. Sorteio:"+idSorteio);
        try{
            String sql =" SELECT * FROM ( " +
                    "   SELECT PREMIADO,ID_BILHETE_PESSOA,NUMR_SEQUENCIAL_BILHETE,ID_REGRA_SORTEIO,ID_PESSOA_PARTCT,INDI_NUMERO_BILHETE_DEFINITIVO,rownum as rn  FROM ( " +
                    "     select case when exists " +
                    "     (select ID_BILHETE_PESSOA from NFG_PREMIO_BILHETE pb where pb.ID_BILHETE_PESSOA= bp.ID_BILHETE_PESSOA) " +
                    "       then 'S'   else 'N' end as Premiado , bp.* " +
                    "     from nfg_bilhete_pessoa bp " +
                    "     where bp.id_regra_sorteio = ? " +
                    "     and bp.ID_PESSOA_PARTCT = ?  " +
                    "     order by 1 desc " +
                    "   ) " +
                    "   WHERE rownum <= ? " +
                    " ) " +
                    " WHERE rn > ?";
            Object[] params = {idSorteio,idCidadao,max*(page+1),max*page};
            return jdbcTemplate.query(sql, new MapperDTOBilhetePessoa(), params);
        }catch (Exception e){
            throw new NFGException("Algo de errado ocorreu ao tentar listar os Bilhetes Premiados");
        }
    }

    public Long retornaTotalDeBilhetes(Integer idCidadao, Integer idSorteio){
        Long count;
        try{
            String hql = "Select count(*) from BilhetePessoa b where b.pessoaParticipante.id = :idCidadao and b.regraSorteio.id = :idSorteio";
            Query query = this.entityManager().createQuery(hql);
            query.setParameter("idCidadao", idCidadao);
            query.setParameter("idSorteio", idSorteio);
            count = (Long) query.getSingleResult();
        }catch (Exception e){
            throw new NFGException("Algo de errado ocorreu ao tentar listar o total de bilhetes");
        }

        return count;
    }

    public Integer novoBilhete(Integer idBilhete, Integer idPremio, Long numeroPremio) {
        PremioBilhete premioBilhete = new PremioBilhete();
        premioBilhete.setBilhetePessoa(new BilhetePessoa());
        premioBilhete.getBilhetePessoa().setId(idBilhete);
        premioBilhete.setPremioSorteio(new PremioSorteio());
        premioBilhete.getPremioSorteio().setId(idPremio);
        premioBilhete.setNumeroPremio(numeroPremio);
        premioBilhete.setDataInclusao(new Date());
        Calendar calDataLimite = Calendar.getInstance();
        calDataLimite.add(Calendar.DAY_OF_YEAR, 90);
        premioBilhete.setDataLimiteResgate(calDataLimite.getTime());
        premioBilhete.save();
        return premioBilhete.getId();
    }
}