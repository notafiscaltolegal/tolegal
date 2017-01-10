package gov.to.service;

import java.util.Date;

import javax.ejb.Local;

import org.springframework.web.multipart.MultipartFile;

import gov.goias.entidades.PessoaParticipante;
import gov.goias.service.PaginacaoDTO;
import gov.to.goias.DocumentoFiscalReclamadoToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface ReclamacaoToLegalService {

	PaginacaoDTO<DocumentoFiscalReclamadoToLegal> findReclamacoesDoCidadao(PessoaParticipante cidadao, Integer page, Integer max);
	
	void cadastraNovaReclamacao(Integer tipoDocFiscalReclamacao, Integer codgMotivo, Date dataEmissaoDocFiscal,
			Integer numeroReclamacao, Integer iEReclamacao, Double valorReclamacao, MultipartFile fileReclamacao,
			PessoaParticipante cidadao, boolean dataDentroDoPrazo, Integer problemaEmpresaReclamacao) throws Exception;
	
	DocumentoFiscalReclamadoToLegal findReclamacoesPorIdReclamacao(Integer idReclamacao);

	Boolean alterarStatusReclamacao(Integer idReclamacao, Integer novoCodgTipoCompl);
}
