package gov.goias.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import gov.goias.entidades.ComplSituacaoReclamacao;
import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.GENPessoaJuridica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.SituacaoDocumentoFiscalReclamado;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;

public interface ReclamacaoService {

	PaginacaoDTO<DocumentoFiscalReclamado> findReclamacoesDoCidadao(PessoaParticipante cidadao, Integer page, Integer max);

	PaginacaoDTO<SituacaoDocumentoFiscalReclamado> andamentoDaReclamacao(Integer idReclamacao, Integer page, Integer max);

	DocumentoFiscalReclamado reclamacaoPorId(Integer idReclamacao);

	List<ComplSituacaoReclamacao> acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao cidadao,
			DocumentoFiscalReclamado reclamacao);

	Boolean alteracaoDeSituacaoReclamacaoPorCidadao(DocumentoFiscalReclamado reclamacao, Integer novoCodgTipoCompl, String infoReclamacao, PessoaParticipante cidadao);

	Boolean cadastraNovaReclamacao(Integer tipoDocFiscalReclamacao, Integer codgMotivo, Date dataEmissaoDocFiscal,
			Integer numeroReclamacao, Integer iEReclamacao, Double valorReclamacao, MultipartFile fileReclamacao,
			PessoaParticipante cidadao, boolean dataDentroDoPrazo);

	PaginacaoDTO<DocumentoFiscalReclamado> listDocumentoFiscalReclamadoPorCNPJ(String numeroCnpj, Integer max,
			Integer page);

	Boolean alteracaoDeSituacaoReclamacaoPorEmpresa(DocumentoFiscalReclamado reclamacao, Integer codigoAcao,
			String descricaoComplemento, GENPessoaJuridica pessoaJuridica);

	DocumentoFiscalReclamado documentoFiscalReclamadoPorId(Integer idDocumento);
}