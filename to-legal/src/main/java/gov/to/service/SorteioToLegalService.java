package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.goias.entidades.RegraSorteio;
import gov.goias.service.SorteioCidadaoDTO;
import gov.to.entidade.SorteioToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface SorteioToLegalService {

	List<RegraSorteio> listRegraSorteioDivulgaResultado();

	SorteioCidadaoDTO carregaDadosDoSorteioParaCidadao(Integer idSorteio, String cpf);

	Integer pontuacaoSemSorteio(Integer id);

	RegraSorteio sorteioPorId(Integer idSorteio);

	SorteioToLegal sorteioAtual();

}
