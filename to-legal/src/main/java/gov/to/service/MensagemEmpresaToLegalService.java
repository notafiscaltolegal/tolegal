package gov.to.service;

import java.util.List;

import gov.to.entidade.MensagemEmpresaToLegal;
import gov.to.filtro.FiltroMensagemEmpresaToLegalDTO;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
public interface MensagemEmpresaToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<MensagemEmpresaToLegal> pesquisar(FiltroMensagemEmpresaToLegalDTO filtroDoc, String...strings);

}
