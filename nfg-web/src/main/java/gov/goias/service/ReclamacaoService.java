package gov.goias.service;

import java.util.List;

import gov.goias.entidades.ComplSituacaoReclamacao;
import gov.goias.entidades.GENPessoaJuridica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.SituacaoDocumentoFiscalReclamado;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.to.goias.DocumentoFiscalReclamadoToLegal;

public interface ReclamacaoService {

	PaginacaoDTO<SituacaoDocumentoFiscalReclamado> andamentoDaReclamacao(Integer idReclamacao, Integer page, Integer max);

	DocumentoFiscalReclamadoToLegal reclamacaoPorId(Integer idReclamacao);

	List<ComplSituacaoReclamacao> acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao cidadao,
			DocumentoFiscalReclamadoToLegal reclamacao);

	Boolean alteracaoDeSituacaoReclamacaoPorCidadao(DocumentoFiscalReclamadoToLegal reclamacao, Integer novoCodgTipoCompl, String infoReclamacao, PessoaParticipante cidadao);

	PaginacaoDTO<DocumentoFiscalReclamadoToLegal> listDocumentoFiscalReclamadoToLegalPorCNPJ(String numeroCnpj, Integer max,
			Integer page);

	Boolean alteracaoDeSituacaoReclamacaoPorEmpresa(DocumentoFiscalReclamadoToLegal reclamacao, Integer codigoAcao,
			String descricaoComplemento, GENPessoaJuridica pessoaJuridica);

	DocumentoFiscalReclamadoToLegal DocumentoFiscalReclamadoToLegalPorId(Integer idDocumento);
}