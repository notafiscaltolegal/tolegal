package gov.to.service;

import java.util.Date;

import javax.ejb.Local;

import org.springframework.web.multipart.MultipartFile;

import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.service.PaginacaoDTO;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface ReclamacaoToLegalService {

	PaginacaoDTO<DocumentoFiscalReclamado> findReclamacoesDoCidadao(PessoaParticipante cidadao, Integer page, Integer max);
	
	void cadastraNovaReclamacao(Integer tipoDocFiscalReclamacao, Integer codgMotivo, Date dataEmissaoDocFiscal,
			Integer numeroReclamacao, Integer iEReclamacao, Double valorReclamacao, MultipartFile fileReclamacao,
			PessoaParticipante cidadao, boolean dataDentroDoPrazo, Integer problemaEmpresaReclamacao) throws Exception;
}
