package gov.goias.mappers;

import gov.goias.dtos.DTOPremiacao;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bruno-cff on 11/06/2015.
 */
public class MapperDTOPremiacao implements RowMapper<DTOPremiacao> {

    @Override
    public DTOPremiacao mapRow(ResultSet resultSet, int i) throws SQLException{
        DTOPremiacao dtoPremiacao = new DTOPremiacao();
        dtoPremiacao.setSorteio(resultSet.getString("INFO_SORTEIO"));
        dtoPremiacao.setNumeroBilhete(resultSet.getInt("NUMR_SEQUENCIAL_BILHETE"));
        dtoPremiacao.setValor(resultSet.getInt("VALR_PREMIO"));
        dtoPremiacao.setCodigoAgencia(resultSet.getString("CODG_AGENCIA"));
        dtoPremiacao.setNomeBanco(resultSet.getString("NOME_BANCO"));
        dtoPremiacao.setNumeroConta(resultSet.getInt("NUMR_CONTA_BANCARIA"));
        dtoPremiacao.setCodigoBanco(resultSet.getInt("CODG_BANCO"));
        dtoPremiacao.setTipoConta(resultSet.getInt("TIPO_CONTA_BANCARIA"));
        dtoPremiacao.setDigito(resultSet.getString("NUMR_DIGITO_CONTA_BANCARIA"));
        dtoPremiacao.setIdBilhetePremiado(resultSet.getInt("ID_PREMIO_BILHETE"));

        dtoPremiacao.setDataLimiteResgate(resultSet.getTimestamp("data_limite_resgate"));
        dtoPremiacao.setDataResgate(resultSet.getTimestamp("data_resgate_premio"));
        dtoPremiacao.setDataSolicitacaoResgate(resultSet.getTimestamp("data_solict_resgate_premio"));

        if (dtoPremiacao.getDataLimiteResgate()!=null){
            if ((new Date()).after(dtoPremiacao.getDataLimiteResgate() )){
                dtoPremiacao.setEstahNoPrazoParaResgate(false);
            }else {
                dtoPremiacao.setEstahNoPrazoParaResgate(true);
            }
        }


        double valrTaxaBancaria = resultSet.getDouble("valr_taxa_bancaria");

        dtoPremiacao.setValorTaxasBancarias(resultSet.wasNull()?null:valrTaxaBancaria);

        double valrTributo = resultSet.getDouble("valr_tributo");
        dtoPremiacao.setValorTributo(resultSet.wasNull()?null:valrTributo);

        String tipoResgatePremio = resultSet.getString("tipo_resgate_premio");
        dtoPremiacao.setTipoResgatePremio((tipoResgatePremio==null||resultSet.wasNull())?null:tipoResgatePremio.charAt(0));

        return dtoPremiacao;
    }
}
