package gov.goias.mappers;

import gov.goias.dtos.DTOGENTelefonePessoa;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bruno-cff on 08/12/2015.
 */
public class MapperDTOGENTelefonePessoa implements RowMapper<DTOGENTelefonePessoa> {

    @Override
    public DTOGENTelefonePessoa mapRow(ResultSet resultSet, int i) throws SQLException {
        DTOGENTelefonePessoa dto = new DTOGENTelefonePessoa();
        dto.setTipo(resultSet.getInt("TIPO_TELEFONE"));
        dto.setDdd(resultSet.getInt("NUMR_DDD"));
        dto.setNumero(resultSet.getInt("NUMR_TELEFONE"));

        return dto;
    }
}
