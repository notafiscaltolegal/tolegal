package gov.to.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.web.multipart.MultipartFile;

import gov.goias.entidades.PessoaParticipante;
import gov.goias.exceptions.NFGException;
import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.MotivoReclamacaoEnum;
import gov.to.dominio.PerfilGeralEnum;
import gov.to.dominio.ProblemaEmpresaEnum;
import gov.to.dominio.ReclamacaoStatusEnum;
import gov.to.dominio.TipoDocumentoFiscalEnum;
import gov.to.entidade.ContribuinteToLegal;
import gov.to.entidade.ReclamacaoLogToLegal;
import gov.to.entidade.ReclamacaoToLegal;
import gov.to.entidade.UsuarioToLegal;
import gov.to.filtro.FiltroReclamacaoToLegal;
import gov.to.goias.DocumentoFiscalReclamadoToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class ReclamacaoToLegalServiceImpl extends ConsultasDaoJpa<ReclamacaoToLegal>
		implements ReclamacaoToLegalService {

	@EJB
	private GenericService<ReclamacaoToLegal, Long> servicoReclamacao;

	@EJB
	private GenericService<ReclamacaoLogToLegal, Long> servicoReclamacaoLog;

	@EJB
	private GenericService<UsuarioToLegal, Long> servicoUsuarioToLegal;

	@EJB
	private ContribuinteToLegalService serviceContribuinte;

	@Override
	public DocumentoFiscalReclamadoToLegal findReclamacoesPorIdReclamacao(Integer idReclamacao) {

		DocumentoFiscalReclamadoToLegal paginacaoDTO = new DocumentoFiscalReclamadoToLegal();

		// FiltroReclamacaoToLegal filtro = new FiltroReclamacaoToLegal();

		// filtro.setIdReclamacao(Long.valueOf(idReclamacao));

		// ReclamacaoToLegal Reclamacao =
		// super.primeiroRegistroPorFiltro(filtro, ReclamacaoToLegal.class);
		// ReclamacaoToLegal Reclamacao = super.filtrarPesquisa(filtro,
		// ReclamacaoToLegal.class, "reclamacaoToLegal").get(0);
		// ReclamacaoToLegal reclamacao =
		// servicoReclamacao.getById(ReclamacaoToLegal.class,
		// Long.valueOf(idReclamacao),"listLogReclamacao");
		ReclamacaoToLegal reclamacao = servicoReclamacao.getById(ReclamacaoToLegal.class, Long.valueOf(idReclamacao));

		paginacaoDTO.setDataDocumentoFiscal(reclamacao.getDataEmissaoDocFiscal());
		paginacaoDTO.setDataReclamacao(reclamacao.getDataCadastro());
		paginacaoDTO.setId(reclamacao.getId().intValue());

		ContribuinteToLegal contribuinte = serviceContribuinte
				.findByInscricaoEstadual(Integer.valueOf(reclamacao.getInscricaoEstadual()));

		paginacaoDTO.setDisableRadioBtn("N");

		paginacaoDTO.setDataDocumentoFiscal(reclamacao.getDataEmissaoDocFiscal());
		paginacaoDTO.setDataReclamacao(reclamacao.getDataCadastro());

		paginacaoDTO.setId(reclamacao.getId().intValue());
		paginacaoDTO.setInscricaoEmpresa(reclamacao.getInscricaoEstadual());
		paginacaoDTO.setListaAndamentoStr(reclamacao.getStatusReclamacao().getLabel());

		paginacaoDTO.setMotivoReclamacao(reclamacao.getMotivoReclamacao().getLabel());
		paginacaoDTO.setNomeFantasiaEmpresa(contribuinte.getRazaoSocial());
		paginacaoDTO.setNumero(Integer.valueOf(reclamacao.getNumeroDocFiscal()));
		paginacaoDTO.setNumeroCnpjEmpresa(contribuinte.getCnpj());
		// paginacaoDTO.setPessoaParticipante(MockCidadao.getPessoaParticipante(""));
		paginacaoDTO.setRazaoSocial(contribuinte.getRazaoSocial());
		// paginacaoDTO.setReclamacaoResolvida("N");
		// deletar
		// paginacaoDTO.setStatusAndamentoStr(reclamacao.getStatusReclamacao().getLabel());
		// paginacaoDTO.setTipoDocumentoFiscal(reclamacao.getTipoDocFiscal().getLabel());
		paginacaoDTO.setDescUltimaSituacao(reclamacao.getStatusReclamacao().getLabel());
		// paginacaoDTO.setTipoExtensao(1);
		paginacaoDTO.setValor(reclamacao.getValorDocFiscal());
		;

		paginacaoDTO.setInscricaoEmpresa(reclamacao.getInscricaoEstadual());
		
		
		
		

		return paginacaoDTO;
	}

	@Override
	public PaginacaoDTO<DocumentoFiscalReclamadoToLegal> findReclamacoesDoCidadao(PessoaParticipante cidadao,
			Integer page, Integer max) {

		PaginacaoDTO<DocumentoFiscalReclamadoToLegal> paginacaoDTO = new PaginacaoDTO<>();

		FiltroReclamacaoToLegal filtro = new FiltroReclamacaoToLegal();

		filtro.setIdUsuario(cidadao.getId().longValue());

		List<ReclamacaoToLegal> listReclamacoes = super.filtrarPesquisa(filtro, ReclamacaoToLegal.class,
				"usuarioToLegal");

		int inicio = calcInicio(page, max);
		int fim = calcPagFim(page, max);

		if (listReclamacoes != null) {
			paginacaoDTO.setCount(listReclamacoes.size());
		}

		List<DocumentoFiscalReclamadoToLegal> listsDocs = new ArrayList<>();
		;

		for (int i = inicio; i <= fim; i++) {

			DocumentoFiscalReclamadoToLegal doc = new DocumentoFiscalReclamadoToLegal();

			if (i == listReclamacoes.size()) {
				break;
			}

			ReclamacaoToLegal rec = listReclamacoes.get(i);
			ContribuinteToLegal contribuinte = serviceContribuinte
					.findByInscricaoEstadual(Integer.valueOf(rec.getInscricaoEstadual()));
			if (contribuinte != null) {

				doc.setDataDocumentoFiscal(rec.getDataEmissaoDocFiscal());
				doc.setDataReclamacao(rec.getDataCadastro());
				doc.setInscricaoEmpresa(rec.getInscricaoEstadual());
				doc.setNumero(Integer.valueOf(rec.getNumeroDocFiscal()));
				doc.setValor(rec.getValorDocFiscal());
				doc.setId(Integer.valueOf(rec.getId().intValue()));
				doc.setRazaoSocial(contribuinte.getRazaoSocial());
				doc.setNumeroCnpjEmpresa(contribuinte.getCnpj());
				doc.setStatusAndamentoStr(rec.getStatusReclamacao().getLabel());

				listsDocs.add(doc);
			}
		}

		paginacaoDTO.setList(listsDocs);
		;

		return paginacaoDTO;
	}

	private static int calcPagFim(Integer page, Integer max) {

		return (calcInicio(page, max) + max) - 1;
	}

	private static int calcInicio(Integer page, Integer max) {

		return (page * max);
	}

	@Override
	public void cadastraNovaReclamacao(Integer tipoDocFiscalReclamacao, Integer codgMotivo, Date dataEmissaoDocFiscal,
			Integer numeroReclamacao, Integer iEReclamacao, Double valorReclamacao, MultipartFile fileReclamacao,
			PessoaParticipante cidadao, boolean dataDentroDoPrazo, Integer problemaEmpresaReclamacao) throws Exception {

		ContribuinteToLegal contribuinte = serviceContribuinte.findByInscricaoEstadual(iEReclamacao);

		if (contribuinte == null) {
			throw new NFGException("Inscrição estadual inválida.");
		}

		ReclamacaoToLegal reclamacao = new ReclamacaoToLegal();
		reclamacao.setTipoDocFiscal(TipoDocumentoFiscalEnum.convertCodTipoDocumentoFiscalParaEnum(codgMotivo));
		reclamacao.setMotivoReclamacao(MotivoReclamacaoEnum.convertCodigoMotivoParaEnum(codgMotivo));
		reclamacao.setDataEmissaoDocFiscal(dataEmissaoDocFiscal);
		reclamacao.setNumeroDocFiscal(numeroReclamacao.toString());
		reclamacao.setInscricaoEstadual(iEReclamacao.toString());
		reclamacao.setValorDocFiscal(valorReclamacao);
		reclamacao.setProblemaEmpresa(
				ProblemaEmpresaEnum.convertCodProblemaEmpresaEnumParaEnum(problemaEmpresaReclamacao));
		reclamacao.setAnexoNota(fileReclamacao.getBytes());
		reclamacao.setStatusReclamacao(ReclamacaoStatusEnum.EMPRESA_NO_PRAZO);

		reclamacao.setDataCadastro(new Date());
		reclamacao.setUsuarioToLegal(servicoUsuarioToLegal.getById(UsuarioToLegal.class, cidadao.getId().longValue()));
		servicoReclamacao.salvar(reclamacao);

		ReclamacaoLogToLegal log = new ReclamacaoLogToLegal();
		log.setPerfilGeral(PerfilGeralEnum.SISTEMA);
		log.setStatusReclamacao(ReclamacaoStatusEnum.EMPRESA_NO_PRAZO);
		log.setDataReclamacao(new Date());
		log.setReclamacaoToLegal(reclamacao);
		servicoReclamacaoLog.salvar(log);

	}

	@Override
	public Boolean alterarStatusReclamacao(Integer idReclamacao, Integer novoCodgTipoCompl) {
		
		if (idReclamacao == null || novoCodgTipoCompl == null){
			return Boolean.FALSE;
		}
		
		ReclamacaoToLegal rec = servicoReclamacao.getById(ReclamacaoToLegal.class, Long.valueOf(idReclamacao));
		rec.setStatusReclamacao(ReclamacaoStatusEnum.convertCodEnumGoiasParaEnumToLegal(novoCodgTipoCompl));
		
		servicoReclamacao.merge(rec);
		
		return Boolean.TRUE;
	}
}