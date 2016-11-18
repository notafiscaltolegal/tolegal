package gov.goias.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import gov.goias.entidades.GENMunicipio;
import gov.goias.entidades.GENTipoLogradouro;
import gov.goias.entidades.GENUf;
import gov.to.dominio.Estado;
import gov.to.dominio.SituacaoUsuario;
import gov.to.dto.EnderecoWsDTO;
import gov.to.entidade.MunicipioToLegal;
import gov.to.service.MunicipioService;

@Service
public class EnderecoServiceImpl implements EnderecoService{
	
	@Autowired(required=true)
	private MunicipioService municipioService;
	
	@Override
	public List<GENUf> listUF() {
		return getListUF();
	}

	private List<GENUf> getListUF() {
		
		List<Estado> estadoList = Estado.list();
		List<GENUf> list = new ArrayList<>();
		
		for (Estado es : estadoList){
			
			GENUf genUf = new GENUf();
			genUf.setCodgIbge(es.ordinal());
			genUf.setCodgUf(es.name());
			genUf.setNomeUf(es.getLabel());
			
			list.add(genUf);
		}
		
		return list;
	}

	@Override
	public List<GENTipoLogradouro> listTipoLogradouro() {
		
		List<SituacaoUsuario> tpLogradouroList = SituacaoUsuario.list();
		List<GENTipoLogradouro> list = new ArrayList<>();
		
		for (SituacaoUsuario tp : tpLogradouroList){
			
			GENTipoLogradouro tipoLogradouro = new GENTipoLogradouro();
			
			tipoLogradouro.setDescTipoLogradouro(tp.getLabel());
			tipoLogradouro.setSiglTipoLogradouro(tp.getLabel());
			tipoLogradouro.setTipoLogradouro(tp.getCodigo());
			
			list.add(tipoLogradouro);
		}
		
		return list;
	}

	@Override
	public List<GENMunicipio> listMunicipioPorUf(String uf) {

		List<GENMunicipio> list = new ArrayList<>();
		List<MunicipioToLegal> listUf = municipioService.municipiosPorCodigoUF(uf);
		
		for (MunicipioToLegal municipio : listUf){
			
			GENMunicipio gen = new GENMunicipio();
			gen.setCodigoIbge(municipio.getId().toString());
			gen.setCodigoMunicipio(municipio.getId());
			gen.setNome(municipio.getMunNome());
			
			list.add(gen);
		}
		
		return list;
	}

	@Override
	public EnderecoDTO consularEnderecoPorCEP(String cep) {
		
		String url = "https://viacep.com.br/ws/"+cep+"/json/";
		
		String json = getJSON(url);
		
		EnderecoWsDTO endereco = new Gson().fromJson(json, EnderecoWsDTO.class);
		
		EnderecoDTO enderecodto = new EnderecoDTO();
		
		if (endereco == null){
			enderecodto.setCepInvalido(Boolean.TRUE);
		}else{
			enderecodto = converteParaModeloLogicoNFG(endereco);
			enderecodto.setCepInvalido(Boolean.FALSE);
		}

		return enderecodto;
	}
	
	public String getJSON(String url) {
		
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != HttpStatus.OK.value()) {
				return null;
			}

			String output = response.getEntity(String.class);
			
			return output;

		  } catch (Exception e) {
			e.printStackTrace();
		  }
		
		return null;
	}


	private EnderecoDTO converteParaModeloLogicoNFG(EnderecoWsDTO endereco) {
		
		EnderecoDTO enderecoDTO = new EnderecoDTO();
		
		String cepSemFormat = endereco.getCep();
		
		if (cepSemFormat != null){
			
			cepSemFormat = cepSemFormat.replace("-", "");
			
			enderecoDTO.setCep(Integer.valueOf(cepSemFormat));
		}
		
		enderecoDTO.setNomeBairro(endereco.getBairro());
		enderecoDTO.setComplemento(endereco.getComplemento());
		
		enderecoDTO.setNomeMunicipio(endereco.getLocalidade().toUpperCase());
		enderecoDTO.setCodgIbgeMunicipio(endereco.getIbge());
		
		enderecoDTO.setNomeLogradouro(endereco.getLogradouro());
		enderecoDTO.setNomeUf(endereco.getUf());
		
		return enderecoDTO;
	}
}