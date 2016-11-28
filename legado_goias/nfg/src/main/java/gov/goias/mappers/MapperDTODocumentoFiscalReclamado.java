package gov.goias.mappers;

import gov.goias.dtos.DTODocumentoFiscalReclamado;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bruno-cff on 15/10/2015.
 */

public class MapperDTODocumentoFiscalReclamado implements RowMapper<DTODocumentoFiscalReclamado>{

    @Override
    public DTODocumentoFiscalReclamado mapRow(ResultSet resultSet, int i) throws SQLException {
        DTODocumentoFiscalReclamado dto = new DTODocumentoFiscalReclamado();
        dto.setNumero(resultSet.getString("NUMR_DOCUMENTO_FISCAL"));
        dto.setDataEmissao(resultSet.getDate("DATA_DOCUMENTO_FISCAL"));
        dto.setDataRegistro(resultSet.getDate("DATA_RECLAMACAO"));
        dto.setInfo(resultSet.getString("INFO_SITUACAO_DOC_FISC_RECLAMA"));
        dto.setMotivo(resultSet.getString("DESC_MOTIVO_RECLAMACAO"));
        dto.setNomeReclamante(resultSet.getString("NOME_PESSOA"));
        dto.setStatus(resultSet.getString("DESC_COMPL_SITUACAO_RECLAMACAO"));
        dto.setUltimaAtualizacao(resultSet.getDate("DATA_CADASTRO_SITUACAO"));
        dto.setValor(resultSet.getLong("VALR_DOCUMENTO_FISCAL"));
        dto.setId(resultSet.getInt("ID_DOCUMENTO_FISCAL_RECLAMADO"));
        dto.setTipoExtensaoDocumento(resultSet.getInt("TIPO_EXTENSAO_ARQUIVO_DOC_FISC"));
        try{
//            dto.setImagem(IOUtils.toByteArray(resultSet.getBinaryStream("IMAG_DOCUMENTO_FISCAL_RECLAMA")));
            dto.setImagem(resultSet.getBlob("IMAG_DOCUMENTO_FISCAL_RECLAMA"));
        }catch (Exception e){

        }

        return dto;
    }
}
