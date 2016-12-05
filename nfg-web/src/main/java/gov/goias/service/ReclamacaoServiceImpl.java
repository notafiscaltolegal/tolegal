package gov.goias.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import gov.goias.entidades.ComplSituacaoReclamacao;
import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.GENPessoaJuridica;
import gov.goias.entidades.MotivoReclamacao;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.SituacaoDocumentoFiscalReclamado;
import gov.goias.entidades.enums.TipoMotivoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;

@Service
public class ReclamacaoServiceImpl implements ReclamacaoService {

	private PaginacaoDTO<DocumentoFiscalReclamado> getPaginacaoDocumentoFiscalReclamado() {
		
		List<DocumentoFiscalReclamado> list = new ArrayList<>();
		
		DocumentoFiscalReclamado dcFiscalReclamadao = getDocumentoFiscalReclamado();
		
		PaginacaoDTO<DocumentoFiscalReclamado> paginacaoDTO = new PaginacaoDTO<DocumentoFiscalReclamado>();
		
		paginacaoDTO.setCount(1);
		list.add(dcFiscalReclamadao);
		paginacaoDTO.setList(list);
		return paginacaoDTO;
	}

	private DocumentoFiscalReclamado getDocumentoFiscalReclamado() {
		DocumentoFiscalReclamado dcFiscalReclamadao = new DocumentoFiscalReclamado();
		
		dcFiscalReclamadao.setDataDocumentoFiscal(new Date());
		dcFiscalReclamadao.setDataReclamacao(new Date());
		dcFiscalReclamadao.setDisableRadioBtn("N");
		dcFiscalReclamadao.setId(1);
		dcFiscalReclamadao.setImgDocumentoFiscal(null);
		dcFiscalReclamadao.setInscricaoEmpresa("123456");
		dcFiscalReclamadao.setListaAndamentoStr("Lista de andamento mock");
		
		MotivoReclamacao motivoReclamacao = new MotivoReclamacao();
		motivoReclamacao.setDescricao("Descriç&#225;o mock");
		motivoReclamacao.setTipoMotivo(TipoMotivoReclamacao.EMPRESA_ALEGOU_PROBLEMA);
		
		dcFiscalReclamadao.setMotivoReclamacao(motivoReclamacao);
		dcFiscalReclamadao.setNomeFantasiaEmpresa("Casas Bahia mock");
		dcFiscalReclamadao.setNumero(123457);
		dcFiscalReclamadao.setNumeroCnpjEmpresa("0000000000000");
		dcFiscalReclamadao.setPessoaParticipante(MockCidadao.getPessoaParticipante(""));
		dcFiscalReclamadao.setRazaoSocial("Raz&#225;o social mock");
		dcFiscalReclamadao.setReclamacaoResolvida("N");
		dcFiscalReclamadao.setStatusAndamentoStr("Status Mock");
		dcFiscalReclamadao.setTipoDocumentoFiscal(1);
		dcFiscalReclamadao.setTipoExtensao(1);
		dcFiscalReclamadao.setValor(1234.5);
		return dcFiscalReclamadao;
	}

	@Override
	public PaginacaoDTO<SituacaoDocumentoFiscalReclamado> andamentoDaReclamacao(Integer idReclamacao, Integer page,Integer max) {
		
		PaginacaoDTO<SituacaoDocumentoFiscalReclamado> paginacaoDTO = new PaginacaoDTO<SituacaoDocumentoFiscalReclamado>();
		
		SituacaoDocumentoFiscalReclamado situacaoDocumentoFiscalReclamado = new SituacaoDocumentoFiscalReclamado();
		
		situacaoDocumentoFiscalReclamado.setId(1);
		situacaoDocumentoFiscalReclamado.setDataCadastroSituacao(new Date());
		situacaoDocumentoFiscalReclamado.setDocumentoFiscalReclamado(getDocumentoFiscalReclamado());
		situacaoDocumentoFiscalReclamado.setInfo("Info Teste");
		situacaoDocumentoFiscalReclamado.setPerfilCadastro(TipoPerfilCadastroReclamacao.CIDADAO);
		situacaoDocumentoFiscalReclamado.setTipoPerfil(TipoPerfilCadastroReclamacao.CIDADAO.getValue());
		
		List<SituacaoDocumentoFiscalReclamado> list = new ArrayList<>();
		
		list.add(situacaoDocumentoFiscalReclamado);
		
		paginacaoDTO.setCount(1);
		paginacaoDTO.setList(list);
		
		return paginacaoDTO;
	}

	@Override
	public DocumentoFiscalReclamado reclamacaoPorId(Integer idReclamacao) {
		return getDocumentoFiscalReclamado();
	}

	@Override
	public List<ComplSituacaoReclamacao> acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao cidadao, DocumentoFiscalReclamado reclamacao) {
		
		ComplSituacaoReclamacao csr = new ComplSituacaoReclamacao();
		
		csr.setCodigo(1);
		csr.setDescricao("Descricao Mock");
		
		List<ComplSituacaoReclamacao> list = new ArrayList<>();
		list.add(csr);
		
		return list;
	}

	@Override
	public Boolean alteracaoDeSituacaoReclamacaoPorCidadao(DocumentoFiscalReclamado reclamacao,
			Integer novoCodgTipoCompl, String infoReclamacao, PessoaParticipante cidadao) {
		
		System.out.println("Alteracao reclamaç&#225;o por cidad&#225;o DocumentoFiscalReclamado mock");
		
		return true;
	}

	@Override
	public PaginacaoDTO<DocumentoFiscalReclamado> listDocumentoFiscalReclamadoPorCNPJ(String numeroCnpj, Integer max, Integer page) {

		return getPaginacaoDocumentoFiscalReclamado();
	}

	@Override
	public Boolean alteracaoDeSituacaoReclamacaoPorEmpresa(DocumentoFiscalReclamado reclamacao, Integer codigoAcao, String descricaoComplemento, GENPessoaJuridica pessoaJuridica) {
		
		System.out.println("Alterado Situacao Reclamaç&#225;o por empresa DocumentoFiscalReclamado mock");
		
		return true;
	}

	@Override
	public DocumentoFiscalReclamado documentoFiscalReclamadoPorId(Integer idDocumento) {
		return getDocumentoFiscalReclamado();
	}

}