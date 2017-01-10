package gov.goias.mappers;

import gov.goias.dtos.DTOEmpresaParticipante;
import gov.goias.dtos.DTOSubclasseCnae;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author henrique-rh
 * @since 29/01/15.
 */
public class MapperDTOEmpresaParticipante implements ResultSetExtractor<List<DTOEmpresaParticipante>> {

    @Override
    public List<DTOEmpresaParticipante> extractData(ResultSet rs) throws SQLException, DataAccessException {
        HashMap<Integer, DTOEmpresaParticipante> map = new HashMap<>();

        while (rs.next()) {
            Integer key = rs.getInt("NUMR_INSCRICAO");
            DTOEmpresaParticipante dto = map.get(key);
            if (dto == null) {
                dto = new DTOEmpresaParticipante();
                dto.setNumeroInscricao(rs.getInt("NUMR_INSCRICAO"));
                dto.setNomeEmpresa(rs.getString("NOME_EMPRESAR"));
                dto.setNumeroCnpj(rs.getString("NUMR_CNPJ"));
                dto.setNomeFantasia(rs.getString("NOME_FANTASIA"));
                dto.setDataEfetivaParticipacao(rs.getString("DATA_EFETIVA_PARTCP"));
                dto.setIndiResponsavelString(rs.getString("INDI_RESP_ADESAO"));
                dto.setDataCredenciamento(rs.getString("DATA_CREDENC"));
                dto.setCodgMunicipio(rs.getString("CODG_MUNICIPIO"));
                dto.setNomeMunicipio(rs.getString("NOME_MUNICIPIO"));
                dto.setNomeBairro(rs.getString("NOME_BAIRRO"));
                dto.setListaSubclasseCnae(new ArrayList<DTOSubclasseCnae>());
            }
            DTOSubclasseCnae dtoSubclasseCnae = new DTOSubclasseCnae();
            dtoSubclasseCnae.setId(rs.getLong("ID_SUBCLASSE_CNAEF"));
            dtoSubclasseCnae.setDescricao(rs.getString("DESC_SUBCLASSE_CNAEF"));
            dto.getListaSubclasseCnae().add(dtoSubclasseCnae);
            map.put(key, dto);
        }

        return new ArrayList<>(map.values());
    }
}
