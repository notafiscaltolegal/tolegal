package gov.goias.service;

import java.util.List;

import javax.ejb.Local;

import gov.goias.entidades.GENMunicipio;
import gov.goias.entidades.GENTipoLogradouro;
import gov.goias.entidades.GENUf;

@Local
public interface EnderecoService {

	List<GENUf> listUF();

	List<GENTipoLogradouro> listTipoLogradouro();

	List<GENMunicipio> listMunicipioPorUf(String uf);

	EnderecoDTO consularEnderecoPorCEP(String cep);
}