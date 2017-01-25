package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.goias.entidades.Mensagem;
import gov.goias.service.PaginacaoDTO;
import gov.to.entidade.MensagemVisualizadaCidadaoToLegal;
import gov.to.filtro.FiltroMensagemVisuCidadaoToLegalDTO;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface MensagemVisuCidadaoToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<MensagemVisualizadaCidadaoToLegal> pesquisar(FiltroMensagemVisuCidadaoToLegalDTO filtroDoc, String...strings);

	Long qntMensagemAguardandoLeitura(String cpf);

	PaginacaoDTO<Mensagem> findMensagensCaixaDeEntrada(Integer max, Integer page, String cpf);

}
