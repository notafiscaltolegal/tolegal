package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.goias.entidades.BilhetePessoa;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface BilheteToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */

	List<BilhetePessoa> listBilhetes(String cpf, Integer idSorteio);

	Long totalBilheteSorteioPorCpf(String cpf, Integer idSorteio);

	List<?> listBilhetePaginado(String cpf, Integer idSorteio, Integer max, Integer page);
}
