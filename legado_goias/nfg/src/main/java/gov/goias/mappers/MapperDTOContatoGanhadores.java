package gov.goias.mappers;

import gov.goias.dtos.DTOContatoGanhadores;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bruno-cff on 02/12/2015.
 */
public class MapperDTOContatoGanhadores implements RowMapper<DTOContatoGanhadores> {

    @Override
    public DTOContatoGanhadores mapRow(ResultSet resultSet, int i) throws SQLException{
        DTOContatoGanhadores dto = new DTOContatoGanhadores();
        dto.setIdPessoa(resultSet.getInt("ID_PESSOA_PARTCT"));
        dto.setNome(resultSet.getString("NOME_PESSOA"));
        dto.setCpf(resultSet.getString("NUMR_CPF"));
        dto.setSorteio(resultSet.getString("INFO_SORTEIO"));
        dto.setNumeroBilhete(resultSet.getInt("NUMR_SEQUENCIAL_BILHETE"));
        dto.setValor(resultSet.getInt("VALR_PREMIO"));
        dto.setContaBancaria(resultSet.getInt("NUMR_CONTA_BANCARIA"));

        return dto;
    }
}
