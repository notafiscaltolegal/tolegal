package gov.goias.mappers;

import gov.goias.dtos.DTOPremiacaoPortal;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bruno-cff on 23/06/2015.
 */
public class MapperDTOPremiacaoPortal implements RowMapper<DTOPremiacaoPortal> {

    @Override
    public DTOPremiacaoPortal mapRow(ResultSet resultSet, int i) throws SQLException {
        DTOPremiacaoPortal dtoPremiacaoPortal = new DTOPremiacaoPortal();
        dtoPremiacaoPortal.setNome(resultSet.getString("NOME_PESSOA"));
        dtoPremiacaoPortal.setCpf(resultSet.getString("NUMR_CPF"));
        dtoPremiacaoPortal.setSorteio(resultSet.getString("INFO_SORTEIO"));
        dtoPremiacaoPortal.setNumeroBilhete(resultSet.getInt("NUMR_SEQUENCIAL_BILHETE"));
        dtoPremiacaoPortal.setValor(resultSet.getInt("VALR_PREMIO"));
        dtoPremiacaoPortal.setCodigoAgencia(resultSet.getString("CODG_AGENCIA"));
        dtoPremiacaoPortal.setNomeAgencia(resultSet.getString("NOME_AGENCIA"));
        dtoPremiacaoPortal.setNomeBanco(resultSet.getString("NOME_BANCO"));
        dtoPremiacaoPortal.setCodigoBanco(resultSet.getInt("CODG_BANCO"));
        dtoPremiacaoPortal.setDigito("DIGITO_CONTA_BANCARIA");
        dtoPremiacaoPortal.setNumeroConta(resultSet.getInt("NUMR_CONTA_BANCARIA"));
        dtoPremiacaoPortal.setDigito(resultSet.getString("NUMR_DIGITO_CONTA_BANCARIA"));
        dtoPremiacaoPortal.setIdBilhetePremiado(resultSet.getInt("ID_PREMIO_BILHETE"));

        return dtoPremiacaoPortal;
    }
}
