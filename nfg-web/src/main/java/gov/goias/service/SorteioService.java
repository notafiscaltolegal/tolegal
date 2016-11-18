package gov.goias.service;

import java.util.List;
import java.util.Map;

import gov.goias.dtos.DTOBilhetePessoa;
import gov.goias.entidades.BilhetePessoa;
import gov.goias.entidades.RegraSorteio;

public interface SorteioService {

	List<RegraSorteio> listRegraSorteioDivulgaResultado();

	Integer pontuacaoSemSorteio(Integer idPessoaParticipante);

	SorteioCidadaoDTO carregaDadosDoSorteioParaCidadao(Integer idSorteio, Integer id);

	RegraSorteio buscaSorteioPorId(Integer idSorteio);

	Integer totalDePontosPorSorteio(Integer idSorteio, String cpf);

	Long retornaTotalDeBilhetes(String cpf, Integer idSorteio);

	List<BilhetePessoa> listBilhetes(String cpf, Integer idSorteio);

	List<Map<String,DTOBilhetePessoa>> listBilhetePaginado(String cpf, Integer idSorteio, Integer max, Integer page);

	List<Map<?,?>> consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf, Integer max, Integer page);
	
	Integer consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf);

}