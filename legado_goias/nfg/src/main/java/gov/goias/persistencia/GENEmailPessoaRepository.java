package gov.goias.persistencia;

import gov.goias.dtos.DTOGENEmailPessoa;
import gov.goias.entidades.GENEmailPessoa;
import gov.goias.exceptions.NFGException;
import gov.goias.mappers.MapperDTOGENEmailPessoa;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno-cff on 04/12/2015.
 */
public class GENEmailPessoaRepository extends GenericRepository<Integer, GENEmailPessoa>{

    private static final Logger logger = Logger.getLogger(GENEmailPessoaRepository.class);

    public List<DTOGENEmailPessoa> listarEmailsPorGanhador(Integer idPessoa){

        try{
        String sql =
                    "       SELECT ep.DESC_EMAIL_PESSOA, ep.TIPO_EMAIL_PESSOA  " +
                    "           FROM NFG_PESSOA_PARTICIPANTE pp, " +
                    "                NFG_BILHETE_PESSOA bp, " +
                    "                NFG_PREMIO_BILHETE pb, " +
                    "                GEN_PESSOA_FISICA pf," +
                    "                GEN_EMAIL_PESSOA ep," +
                    "                NFG_REGRA_SORTEIO rs, " +
                    "                NFG_PREMIO_SORTEIO pss " +
                    "           where bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT " +
                    "               and pb.ID_BILHETE_PESSOA = bp.ID_BILHETE_PESSOA " +
                    "               and pf.ID_PESSOA = ep.ID_PESSOA " +
                    "               and rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO " +
                    "               and pss.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO " +
                    "               and pss.ID_PREMIO_SORTEIO = pb.ID_PREMIO_SORTEIO " +
                    "               and pp.ID_PESSOA = pf.ID_PESSOA " +
                    "               and pp.Id_Pessoa_Partct = ? " +
                    "           order by ep.DESC_EMAIL_PESSOA desc ";

            Object[] params = {idPessoa};
            return jdbcTemplate.query(sql, new MapperDTOGENEmailPessoa(), params);

        }catch (Exception e){
            logger.error("Erro ao listar emails " + e);
            throw new NFGException("Algo de errado ocorreu ao tentar listar os emails");
        }
    }
}
