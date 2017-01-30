package gov.to.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.entidades.ComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.service.ContribuinteService;
import gov.goias.service.PaginacaoDTO;
import gov.goias.service.ReclamacaoService;
import gov.to.entidade.ContribuinteToLegal;
import gov.to.goias.DocumentoFiscalReclamadoToLegal;
import gov.to.service.ContribuinteToLegalService;
import gov.to.service.MensagemVisuCidadaoToLegalService;
import gov.to.service.ReclamacaoToLegalService;

/**
 * Created by bruno-cff on 30/09/2015.
 */
@Controller
@RequestMapping(value = { "contribuinte/reclamacao", "/contador/reclamacao" })
public class ReclamacaoController extends BaseController {

	@Autowired
	private ReclamacaoService reclamacaoService;

	@Autowired
	private ContribuinteService contribuinteService;
	@Autowired
	private ContribuinteToLegalService contribuinteToLegalService;

	@Autowired
	private ReclamacaoToLegalService reclamacaoToLegalService;

	@RequestMapping("/consultar/{numeroCnpj}")
	public ModelAndView consultar(@PathVariable(value = "numeroCnpj") String numeroCnpj) {
		ModelAndView modelAndView = new ModelAndView("/reclamacao/consultar");

		modelAndView.addObject("numeroCnpj", numeroCnpj);
		modelAndView.addObject("urlBase", getUrlBase());

		return modelAndView;
	}

	@RequestMapping(value = "/downloadAnexo/{idDocumento}")
	public ResponseEntity<?> downloadAnexo(@PathVariable Integer idDocumento) {

		ResponseEntity<InputStreamResource> response = null;
		try {
			DocumentoFiscalReclamadoToLegal documento = reclamacaoService
					.DocumentoFiscalReclamadoToLegalPorId(idDocumento);
			InputStreamResource inputStreamResource = new InputStreamResource(
					documento.getImgDocumentoFiscal().getBinaryStream());

			HttpHeaders headers = new HttpHeaders();
			setContentType(documento, headers);

			response = new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return response;
		}
	}

	private void setContentType(DocumentoFiscalReclamadoToLegal documento, HttpHeaders headers) {
		String fileName = documento.getNumero().toString();
		Integer extensaoDoArquivo = documento.getTipoExtensao();

		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		switch (extensaoDoArquivo) {
		case 1:
			headers.setContentDispositionFormData(fileName.concat(".pdf"), fileName.concat(".pdf"));
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			break;
		case 2:
			headers.setContentDispositionFormData(fileName.concat(".jpg"), fileName.concat(".jpg"));
			headers.setContentType(MediaType.parseMediaType("application/jpg"));
			break;
		case 3:
			headers.setContentDispositionFormData(fileName.concat(".png"), fileName.concat(".png"));
			headers.setContentType(MediaType.parseMediaType("application/png"));
			break;
		}
	}

	@RequestMapping("/listar/{page}")
	public @ResponseBody Map<String, Object> listar(@PathVariable(value = "page") Integer page, String numeroCnpj) {

		Integer max = 3;
		Map<String, Object> resposta = new HashMap<String, Object>();
		Map<String, Object> pagination = new HashMap<String, Object>();

		// localizar contribuinte por cnpj e retornar inscrição estadual
		ContribuinteToLegal contribuinte = new ContribuinteToLegal();
		contribuinte = contribuinteToLegalService.findByCNPJ(numeroCnpj);

		// PaginacaoDTO<DocumentoFiscalReclamadoToLegal>
		// paginacaoDocumentoReclamado =
		// reclamacaoService.listDocumentoFiscalReclamadoToLegalPorCNPJ(numeroCnpj,
		// max, page);
		PaginacaoDTO<DocumentoFiscalReclamadoToLegal> paginacaoDocumentoReclamado = reclamacaoToLegalService
				.findReclamacoesByInscricaoEstadual(contribuinte.getId(), page, max);

		List<DocumentoFiscalReclamadoToLegal> reclamacoes = paginacaoDocumentoReclamado.getList();
		List<DocumentoFiscalReclamadoToLegal> listDocumentos = new ArrayList<>();

		for (DocumentoFiscalReclamadoToLegal reclamacao : reclamacoes) {
			List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoService
					.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);
			if (statusDisponiveis != null && statusDisponiveis.size() > 0) {
				reclamacao.setDisableRadioBtn("");
				listDocumentos.add(reclamacao);
			} else {
				reclamacao.setDisableRadioBtn("disabled");
				listDocumentos.add(reclamacao);
			}
		}

		pagination.put("total", paginacaoDocumentoReclamado.getCount());
		pagination.put("total", 0);
		pagination.put("page", ++page);
		pagination.put("max", max);
		resposta.put("listDocumentos", listDocumentos);
		resposta.put("urlDownload", getUrlDownload());
		resposta.put("pagination", pagination);

		return resposta;
	}

	@RequestMapping("/selectStatus")
	public @ResponseBody Map<String, Object> selectStatus(Integer idReclamacao) {

		Map<String, Object> resposta = new HashMap<String, Object>();
		DocumentoFiscalReclamadoToLegal reclamacao = reclamacaoService.reclamacaoPorId(idReclamacao);
		List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoService
				.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);

		resposta.put("statusDisponiveis", statusDisponiveis);

		return resposta;
	}
	
	
	 @RequestMapping("verReclamacaoDetalhe/{idReclamacao}")
	    public @ResponseBody Map<String, Object> verReclamacaoDetalhe(@PathVariable("idReclamacao") Integer idReclamacao){
		 	Map<String, Object> resposta = new HashMap<>();

	        DocumentoFiscalReclamadoToLegal reclamacao = reclamacaoService.reclamacaoPorId(idReclamacao);

	        List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoService.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CIDADAO,reclamacao);

	        resposta.put("reclamacao", reclamacao);
	        resposta.put("statusDisponiveis", statusDisponiveis);
	        resposta.put("nomeFantasia", reclamacao.getNomeFantasiaEmpresa());
	        resposta.put("dataEmissaoStr", DateFormatUtils.format(reclamacao.getDataDocumentoFiscal(),"dd/MM/yyyy"));
	        resposta.put("dataReclamacaoStr", DateFormatUtils.format(reclamacao.getDataReclamacao(),"dd/MM/yyyy"));

	        return resposta;
	    }
	 
	 @Autowired
	    private MensagemVisuCidadaoToLegalService mensagemVisualizadaCidadaoToLegalService;

	@RequestMapping("/incluirComplemento")
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	public @ResponseBody Map<String, Object> incluirComplemento(String descricaoComplemento, Integer codigoAcao,
			Integer idReclamacao, String cnpj) {
		 Map<String, Object> resposta = new HashMap<>();

	        //urgente:  localizar reclamacao por cnpj
	        
	        
	        DocumentoFiscalReclamadoToLegal reclamacao = reclamacaoToLegalService.findReclamacoesPorIdReclamacao(idReclamacao);
	        
	        
	        //urgente: 
	        //GENPessoaJuridica pessoaJuridica = contribuinteService.pessoaJuridicaPorCNPJ(cnpj);
	        //mensagemVisualizadaCidadaoToLegalService.cadastrarMensagem(dataLeitura, titulo, mensagem, cpf, titulo)
	        try{
	        	this.mensagemVisualizadaCidadaoToLegalService.cadastrarMensagem(new Date(), "teste", descricaoComplemento, reclamacao.getCpf(), cnpj);
	        	
	        	 resposta.put("sucesso", Boolean.TRUE);
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        	
	        	resposta.put("sucesso", Boolean.FALSE);
	        }
	        
	        return resposta;
	}

	private String getUrlDownload() {
		String urlDownload = request.getRequestURI();
		urlDownload = urlDownload.replace("/listar", "/downloadAnexo");
		urlDownload = urlDownload.replaceAll("\\d*", "");
		return urlDownload;
	}

	private String getUrlBase() {
		String contexto = "/to-legal";
		String urlBase = request.getRequestURI();
		urlBase = urlBase.substring(urlBase.indexOf(contexto) + contexto.length(), urlBase.length());
		urlBase = urlBase.replace("/consultar", "");
		urlBase = urlBase.replaceAll("\\d*", "");
		return urlBase;
	}
}