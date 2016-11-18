package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.to.entidade.UsuarioToLegal;
import gov.to.filtro.FiltroUsuarioToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface UsuarioToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<UsuarioToLegal> pesquisar(FiltroUsuarioToLegal filtro, String... propriedadesHbInitialize);

	UsuarioToLegal primeiroRegistro(FiltroUsuarioToLegal filtro, String...propriedadesHbInitialize);

	void enviaEmailConfirmacaoCadastro(UsuarioToLegal usuarioToLegal);

}
