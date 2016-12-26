package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.to.entidade.MunicipioToLegal;
import gov.to.filtro.FiltroMunicipioToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface MunicipioService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<MunicipioToLegal> pesquisar(FiltroMunicipioToLegal filtro, String... propriedadesHbInitialize);

	List<MunicipioToLegal> municipiosPorCodigoUF(String uf);
}
