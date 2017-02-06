package gov.to.service;

import javax.ejb.Local;

import gov.goias.service.PaginacaoDTO;
import gov.to.goias.ReclamacaoLogDTO;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface ReclamacaoLogToLegalService {

	/**
	 * 
	 * @param max 
	 * @param page 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	PaginacaoDTO<ReclamacaoLogDTO> logReclamacaoPorIdReclamacao(Integer idReclamacao, Integer page, Integer max) ;
	void cadastrarLog();
	
}
