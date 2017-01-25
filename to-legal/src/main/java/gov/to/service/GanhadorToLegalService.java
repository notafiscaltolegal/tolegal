package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.to.entidade.GanhadorSorteioToLegal;
import gov.to.filtro.FiltroGanhadorToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface GanhadorToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */

	List<GanhadorSorteioToLegal> pesquisar(FiltroGanhadorToLegal filtroDoc, String...strings);

	List<GanhadorSorteioToLegal> ganhadorPorSorteio(Integer idSorteio, String cpf);
}
