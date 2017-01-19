package gov.to.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import gov.to.entidade.NotaFiscalToLegal;
import gov.to.filtro.FiltroNotaFiscalToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface NotaFiscalToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<NotaFiscalToLegal> pesquisar(FiltroNotaFiscalToLegal filtro, String... propriedadesHbInitialize);

	NotaFiscalToLegal primeiroRegistro(FiltroNotaFiscalToLegal filtro, String...propriedadesHbInitialize);

	int totalNotasPorCpf(String cpf, Integer idSorteio);

	List<String> chaveAcessoPorDataEmissao(Date dataInicio, Date dataFim);
}
