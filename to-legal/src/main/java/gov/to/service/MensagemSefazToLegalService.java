package gov.to.service;

import java.util.List;

import javax.ejb.Local;

import gov.to.entidade.MensagemSefazToLegal;
import gov.to.filtro.FiltroMensagemSefazToLegalDTO;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface MensagemSefazToLegalService {

	/**
	 * 
	 * @param filtro
	 * @param hibernateInitialize 
	 * @return
	 */
	List<MensagemSefazToLegal> pesquisar(FiltroMensagemSefazToLegalDTO filtroDoc, String...strings);

	Integer qntMensagemNaoLidaCidadao(String cpf);

	void gravarLeituraDasMensagens(String cpf);

	Integer qntMensagemNaoLidaEmpresa(String inscricaoEstadual);

	void gravarLeituraDasMensagensEmpresa(String id);

}
