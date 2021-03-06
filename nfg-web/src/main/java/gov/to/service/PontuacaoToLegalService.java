package gov.to.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import gov.to.entidade.PontuacaoToLegal;
import gov.to.filtro.FiltroPontuacaoToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface PontuacaoToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<PontuacaoToLegal> pesquisar(FiltroPontuacaoToLegal filtro, String... propriedadesHbInitialize);

	PontuacaoToLegal primeiroRegistro(FiltroPontuacaoToLegal filtro, String...propriedadesHbInitialize);

	int totalPontosSorteioPorCpf(String cpf, Integer idSorteio);
	
	int totalNotasSorteioPorCpf(String cpf, Integer idSorteio);

	List<Map<String, Object>> consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf, Integer max, Integer page);

	List<Map<String, Object>> pontuacaoBonus(Integer idSorteio, String cpf, Integer max, Integer page);
}
