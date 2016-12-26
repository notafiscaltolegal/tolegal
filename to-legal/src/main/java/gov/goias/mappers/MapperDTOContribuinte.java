package gov.goias.mappers;

import gov.goias.dtos.DTOCnaeAutorizado;
import gov.goias.dtos.DTOContribuinte;
import gov.goias.dtos.DTOEmpresaParticipante;
import gov.goias.dtos.DTOSubclasseCnae;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author henrique-rh
 * @since 27/01/15.
 */
public class MapperDTOContribuinte implements ResultSetExtractor<List<DTOContribuinte>> {

    @Override
    public List<DTOContribuinte> extractData(ResultSet rs) throws SQLException, DataAccessException {
        HashMap<Integer, DTOContribuinte> map = new HashMap<>();

        while (rs.next()) {
            Integer key = rs.getInt("NUMR_INSCRICAO");
            DTOContribuinte dto = map.get(key);
            if (dto == null) {
                dto = new DTOContribuinte();
                dto.setNomeEmpresa(rs.getString("NOME_EMPRESA"));
                dto.setNumeroCnpj(rs.getString("NUMR_CNPJ"));
                dto.setDataObrigatoriedadeCnae(rs.getTimestamp("DATA_OBRIGATORIEDADE_CNAE"));
                dto.setDataCredenciamento(rs.getTimestamp("DATA_CREDENCIAMENTO"));
                dto.setDataEfetivaParticipacao(rs.getTimestamp("DATA_EFETIVA_PARTICIPACAO"));
                dto.setIndiResponsavelAdesaoString(rs.getString("INDI_RESP_ADESAO"));
                dto.setIsEmpresaParticipante(rs.getString("IS_EMPRESA_PARTICIPANTE").equals("S"));
                dto.setNumeroInscricao(rs.getInt("NUMR_INSCRICAO"));
                dto.setIsEmissorEFD(rs.getString("IS_EMISSOR_EFD").equals("S"));
                dto.setListaCnaeAutorizado(new ArrayList<DTOCnaeAutorizado>());
                dto.setIdPessoa(rs.getInt("ID_PESSOA"));
            }
            DTOCnaeAutorizado dtoCnaeAutorizado = new DTOCnaeAutorizado();
            dtoCnaeAutorizado.setId(rs.getLong("ID_CNAE_AUTORIZADO"));
            dtoCnaeAutorizado.setIdSubClasseCnae(rs.getLong("ID_SUBCLASSE_CNAEF"));
            dtoCnaeAutorizado.setIsCnaeAutorizado(rs.getString("IS_CNAE_AUTORIZADO").equals("S"));
            dtoCnaeAutorizado.setIsCnaeObrigatorio(rs.getString("IS_CNAE_OBRIGATORIO").equals("S"));
            dto.getListaCnaeAutorizado().add(dtoCnaeAutorizado);
            map.put(key, dto);
        }

        return new ArrayList<>(map.values());
    }

//    @Override
//    public DTOContribuinte mapRow(ResultSet resultSet, int i) throws SQLException {
//        DTOContribuinte dto = new DTOContribuinte();
//        dto.setNomeEmpresa(resultSet.getString("NOME_EMPRESA"));
//        dto.setNumeroCnpj(resultSet.getString("NUMR_CNPJ"));
//        dto.setDataObrigatoriedadeCnae(resultSet.getDate("DATA_OBRIGATORIEDADE_CNAE"));
//        dto.setDataCredenciamento(resultSet.getDate("DATA_CREDENCIAMENTO"));
//        dto.setDataEfetivaParticipacao(resultSet.getDate("DATA_EFETIVA_PARTICIPACAO"));
//        dto.setIndiResponsavelAdesaoString(resultSet.getString("INDI_RESP_ADESAO"));
//        dto.setIsEmpresaParticipante(resultSet.getString("IS_EMPRESA_PARTICIPANTE").equals("S"));
//        dto.setNumeroInscricao(resultSet.getInt("NUMR_INSCRICAO"));
//        dto.setIsCnaeAutorizado(resultSet.getString("IS_CNAE_AUTORIZADO").equals("S"));
//        dto.setIsCnaeObrigatorio(resultSet.getString("IS_CNAE_OBRIGATORIO").equals("S"));
//        dto.setIsEmissorEFD(resultSet.getString("IS_EMISSOR_EFD").equals("S"));
//        return dto;
//    }
}
