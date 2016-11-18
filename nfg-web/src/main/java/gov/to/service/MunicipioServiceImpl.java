package gov.to.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.to.dominio.Estado;
import gov.to.entidade.MunicipioToLegal;
import gov.to.filtro.FiltroMunicipioToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class MunicipioServiceImpl implements MunicipioService{
	
	@EJB
	private ConsultasDaoJpa<MunicipioToLegal> reposiroty;

	@Override
	public List<MunicipioToLegal> pesquisar(FiltroMunicipioToLegal filtro, String... hbInitialize) {
		return reposiroty.filtrarPesquisa(filtro, MunicipioToLegal.class, hbInitialize);
	}

	@Override
	public List<MunicipioToLegal> municipiosPorCodigoUF(String uf) {
		
		FiltroMunicipioToLegal filtro = new FiltroMunicipioToLegal();
		
		filtro.setCodgUF(Estado.estadoPorName(uf));
		
		return pesquisar(filtro);
	}
}