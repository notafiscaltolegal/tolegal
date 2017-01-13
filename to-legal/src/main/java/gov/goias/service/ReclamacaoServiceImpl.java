package gov.goias.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.springframework.stereotype.Service;

import gov.goias.entidades.ComplSituacaoReclamacao;
import gov.goias.entidades.GENPessoaJuridica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.SituacaoDocumentoFiscalReclamado;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.to.goias.DocumentoFiscalReclamadoToLegal;
import gov.to.service.ContribuinteToLegalService;
import gov.to.service.ReclamacaoToLegalService;

@Service
public class ReclamacaoServiceImpl implements ReclamacaoService {

	@EJB
	private ContribuinteToLegalService serviceContribuinte;
	
	@EJB
	private ReclamacaoToLegalService serviceReclamacao;
	
	private PaginacaoDTO<DocumentoFiscalReclamadoToLegal> getPaginacaoDocumentoFiscalReclamadoToLegal() {
		
		List<DocumentoFiscalReclamadoToLegal> list = new ArrayList<>();
		
		DocumentoFiscalReclamadoToLegal dcFiscalReclamadao = getDocumentoFiscalReclamadoToLegal(63);
		
		PaginacaoDTO<DocumentoFiscalReclamadoToLegal> paginacaoDTO = new PaginacaoDTO<DocumentoFiscalReclamadoToLegal>();
		
		
		
		
		
		paginacaoDTO.setCount(1);
		list.add(dcFiscalReclamadao);
		paginacaoDTO.setList(list);
		return paginacaoDTO;
	}

	private DocumentoFiscalReclamadoToLegal getDocumentoFiscalReclamadoToLegal(Integer idReclamacao) {
		
		DocumentoFiscalReclamadoToLegal dcFiscalReclamado = new DocumentoFiscalReclamadoToLegal();
		
		dcFiscalReclamado = serviceReclamacao.findReclamacoesPorIdReclamacao(idReclamacao);
		
		return dcFiscalReclamado;
	}

	@Override
	public PaginacaoDTO<SituacaoDocumentoFiscalReclamado> andamentoDaReclamacao(Integer idReclamacao, Integer page,Integer max) {
		
		PaginacaoDTO<SituacaoDocumentoFiscalReclamado> paginacaoDTO = new PaginacaoDTO<SituacaoDocumentoFiscalReclamado>();
		
		SituacaoDocumentoFiscalReclamado situacaoDocumentoFiscalReclamadoToLegal = new SituacaoDocumentoFiscalReclamado();
		
		situacaoDocumentoFiscalReclamadoToLegal.setId(1);
		situacaoDocumentoFiscalReclamadoToLegal.setDataCadastroSituacao(new Date());
		situacaoDocumentoFiscalReclamadoToLegal.setDocumentoFiscalReclamadoToLegal(getDocumentoFiscalReclamadoToLegal(idReclamacao));
		situacaoDocumentoFiscalReclamadoToLegal.setInfo("Info Teste");
		situacaoDocumentoFiscalReclamadoToLegal.setPerfilCadastro(TipoPerfilCadastroReclamacao.CIDADAO);
		situacaoDocumentoFiscalReclamadoToLegal.setTipoPerfil(TipoPerfilCadastroReclamacao.CIDADAO.getValue());
		
		List<SituacaoDocumentoFiscalReclamado> list = new ArrayList<>();
		
		list.add(situacaoDocumentoFiscalReclamadoToLegal);
		
		paginacaoDTO.setCount(1);
		paginacaoDTO.setList(list);
		
		return paginacaoDTO;
	}

	@Override
	public DocumentoFiscalReclamadoToLegal reclamacaoPorId(Integer idReclamacao) {
		return getDocumentoFiscalReclamadoToLegal(idReclamacao);
	}

	@Override
	public List<ComplSituacaoReclamacao> acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao cidadao, DocumentoFiscalReclamadoToLegal reclamacao) {
		
		ComplSituacaoReclamacao csr = new ComplSituacaoReclamacao();
		
		csr.setCodigo(1);
		csr.setDescricao("Teste Mock");
		
		
		
		List<ComplSituacaoReclamacao> list = new ArrayList<>();
		list.add(csr);
		
		csr = new ComplSituacaoReclamacao();
		csr.setCodigo(11);
		csr.setDescricao("teste");
		list.add(csr);
		
		return list;
	}

	@Override
	public Boolean alteracaoDeSituacaoReclamacaoPorCidadao(DocumentoFiscalReclamadoToLegal reclamacao,
			Integer novoCodgTipoCompl, String infoReclamacao, PessoaParticipante cidadao) {
		
		System.out.println("Alteracao reclamaç&#225;o por cidad&#225;o DocumentoFiscalReclamadoToLegal mock");
		
		return true;
	}

	@Override
	public PaginacaoDTO<DocumentoFiscalReclamadoToLegal> listDocumentoFiscalReclamadoToLegalPorCNPJ(String numeroCnpj, Integer max, Integer page) {


		
		
		
		
		
		return getPaginacaoDocumentoFiscalReclamadoToLegal();
	}

	@Override
	public Boolean alteracaoDeSituacaoReclamacaoPorEmpresa(DocumentoFiscalReclamadoToLegal reclamacao, Integer codigoAcao, String descricaoComplemento, GENPessoaJuridica pessoaJuridica) {
		
		System.out.println("Alterado Situacao Reclamaç&#225;o por empresa DocumentoFiscalReclamadoToLegal mock");
		
		return true;
	}

	@Override
	public DocumentoFiscalReclamadoToLegal DocumentoFiscalReclamadoToLegalPorId(Integer idDocumento) {
		return getDocumentoFiscalReclamadoToLegal(idDocumento);
	}

}