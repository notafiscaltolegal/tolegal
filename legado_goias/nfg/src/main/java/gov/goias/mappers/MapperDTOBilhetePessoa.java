package gov.goias.mappers;

import gov.goias.dtos.DTOBilhetePessoa;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bruno-cff on 15/05/2015.
 */
public class MapperDTOBilhetePessoa implements RowMapper<DTOBilhetePessoa> {

    @Override
    public DTOBilhetePessoa mapRow(ResultSet resultSet, int i) throws SQLException{
        DTOBilhetePessoa dto = new DTOBilhetePessoa();
        dto.setPremiado(resultSet.getString("PREMIADO"));
        dto.setIdBilhete(resultSet.getInt("ID_BILHETE_PESSOA"));
        dto.setNumero(resultSet.getInt("NUMR_SEQUENCIAL_BILHETE"));
        dto.setIdSorteio(resultSet.getInt("ID_REGRA_SORTEIO"));
        dto.setIdPessoa(resultSet.getInt("ID_PESSOA_PARTCT"));
        dto.setBilheteDefinitivo(resultSet.getString("INDI_NUMERO_BILHETE_DEFINITIVO"));

        return dto;
    }
}
