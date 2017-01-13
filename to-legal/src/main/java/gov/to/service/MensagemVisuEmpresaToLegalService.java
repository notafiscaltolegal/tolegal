package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.goias.entidades.Mensagem;
import gov.goias.service.PaginacaoDTO;
import gov.to.entidade.MensagemVisualizadaEmpresaToLegal;
import gov.to.filtro.FiltroMensagemVisuEmpresaToLegalDTO;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface MensagemVisuEmpresaToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<MensagemVisualizadaEmpresaToLegal> pesquisar(FiltroMensagemVisuEmpresaToLegalDTO filtroDoc, String...strings);

	List<Long> ids(String inscricaoEstadual);

	PaginacaoDTO<Mensagem> findMensagensCaixaDeEntrada(Integer max, Integer page, String inscricaoEstadual);

}
