package gov.goias.mappers;

import gov.goias.dtos.DTOGENEmailPessoa;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bruno-cff on 07/12/2015.
 */
public class MapperDTOGENEmailPessoa implements RowMapper<DTOGENEmailPessoa> {

    @Override
    public DTOGENEmailPessoa mapRow(ResultSet resultSet, int i) throws SQLException{
        DTOGENEmailPessoa dto = new DTOGENEmailPessoa();
        dto.setTipo(resultSet.getInt("TIPO_EMAIL_PESSOA"));
        dto.setDescricao(resultSet.getString("DESC_EMAIL_PESSOA"));

        return dto;
    }
}
