package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.to.entidade.EnderecoToLegal;
import gov.to.entidade.PessoaFisicaToLegal;
import gov.to.filtro.FiltroPessoaFisicaToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface PessoaFisicaToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<PessoaFisicaToLegal> pesquisar(FiltroPessoaFisicaToLegal filtro, String... propriedadesHbInitialize);

	EnderecoToLegal enderecoPorCpf(String cpf);

	PessoaFisicaToLegal primeiroRegistro(FiltroPessoaFisicaToLegal filtro, String string, String string2);

}
