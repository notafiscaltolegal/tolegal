package gov.goias.persistencia;

import gov.goias.dtos.DTOGENEmailPessoa;
import gov.goias.dtos.DTOGENTelefonePessoa;
import gov.goias.entidades.GENTelefonePessoa;
import gov.goias.exceptions.NFGException;
import gov.goias.mappers.MapperDTOGENEmailPessoa;
import gov.goias.mappers.MapperDTOGENTelefonePessoa;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno-cff on 07/12/2015.
 */
public class GENTelefonePessoaRepository extends GenericRepository<Integer, GENTelefonePessoa>{

    private static final Logger logger = Logger.getLogger(GENTelefonePessoaRepository.class);


    public List<DTOGENTelefonePessoa> listarTelefoneGanhador(Integer idPessoa){

        try{
            String sql =
                    "           SELECT tp.TIPO_TELEFONE, tp.NUMR_DDD, tp.NUMR_TELEFONE " +
                    "               FROM NFG_PESSOA_PARTICIPANTE pp," +
                    "                    NFG_BILHETE_PESSOA bp," +
                    "                    NFG_PREMIO_BILHETE pb, " +
                    "                    GEN_PESSOA_FISICA pf," +
                    "                    NFG_REGRA_SORTEIO rs," +
                    "                    GEN_TELEFONE_PESSOA tp," +
                    "                    NFG_PREMIO_SORTEIO pss " +
                    "               where bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT " +
                    "               and pb.ID_BILHETE_PESSOA = bp.ID_BILHETE_PESSOA " +
                    "               and rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO " +
                    "               and tp.ID_PESSOA = pf.ID_PESSOA " +
                    "               and pss.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO " +
                    "               and pss.ID_PREMIO_SORTEIO = pb.ID_PREMIO_SORTEIO " +
                    "               and pp.ID_PESSOA = pf.ID_PESSOA " +
                    "               and pp.Id_Pessoa_Partct = ? " +
                    "           order by tp.NUMR_DDD desc ";

            Object[] params = {idPessoa};
            return jdbcTemplate.query(sql, new MapperDTOGENTelefonePessoa(), params);

        }catch (Exception e){
            logger.error("Erro ao listar telefones " + e);
            throw new NFGException("Algo de errado ocorreu ao tentar listar os telefones");
        }
    }
}
