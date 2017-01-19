package gov.to.service;

import java.util.List;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
public interface BloqueioCpfToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<String> cpfsBloqueados();

}
