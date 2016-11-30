package gov.goias.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.GENPessoaJuridica;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.service.ContribuinteService;
import gov.goias.service.PaginacaoDTO;
import gov.goias.service.ReclamacaoService;

/**
 * Created by bruno-cff on 30/09/2015.
 */
@Controller
@RequestMapping(value = {"contribuinte/reclamacao" , "/contador/reclamacao"})
public class ReclamacaoController extends BaseController{
    
    @Autowired
    private ReclamacaoService reclamacaoService;
    
    @Autowired
    private ContribuinteService contribuinteService;

    @RequestMapping("/consultar/{numeroCnpj}")
    public ModelAndView consultar(@PathVariable(value="numeroCnpj")String numeroCnpj) {
        ModelAndView modelAndView = new ModelAndView("/reclamacao/consultar");

        modelAndView.addObject("numeroCnpj", numeroCnpj);
        modelAndView.addObject("urlBase", getUrlBase());

        return modelAndView;
    }

    @RequestMapping(value = "/downloadAnexo/{idDocumento}")
    public ResponseEntity<?> downloadAnexo(@PathVariable Integer idDocumento) {
    	
    	 ResponseEntity<InputStreamResource> response = null;
         try {
             DocumentoFiscalReclamado documento =  reclamacaoService.documentoFiscalReclamadoPorId(idDocumento);
             InputStreamResource inputStreamResource = new InputStreamResource(documento.getImgDocumentoFiscal().getBinaryStream());

             HttpHeaders headers = new HttpHeaders();
             setContentType(documento, headers);

             response = new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);

             return response;

         } catch (Exception e) {
        	 e.printStackTrace();
             return response;
         }
    }

    private void setContentType(DocumentoFiscalReclamado documento, HttpHeaders headers){
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
    public @ResponseBody
    Map<String, Object> listar(@PathVariable(value = "page") Integer page, String numeroCnpj) {
    	
    	Integer max = 3;
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

//        PaginacaoDTO<DocumentoFiscalReclamado> paginacaoDocumentoReclamado = reclamacaoService.listDocumentoFiscalReclamadoPorCNPJ(numeroCnpj, max, page);
//        List<DocumentoFiscalReclamado> reclamacoes = paginacaoDocumentoReclamado.getList();
        List<DocumentoFiscalReclamado> listDocumentos = new ArrayList<>();

//        for(DocumentoFiscalReclamado reclamacao : reclamacoes){
//            List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoService.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);
//            if(statusDisponiveis != null && statusDisponiveis.size() > 0){
//               reclamacao.setDisableRadioBtn("");
//               listDocumentos.add(reclamacao);
//            }else{
//                reclamacao.setDisableRadioBtn("disabled");
//                listDocumentos.add(reclamacao);
//            }
//        }

//        pagination.put("total", paginacaoDocumentoReclamado.getCount());
        pagination.put("total", 0);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("listDocumentos", listDocumentos);
        resposta.put("urlDownload", getUrlDownload());
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/selectStatus")
    public @ResponseBody
    	Map<String, Object> selectStatus(Integer idReclamacao) {

        Map<String, Object> resposta = new HashMap<String, Object>();
        DocumentoFiscalReclamado reclamacao = reclamacaoService.reclamacaoPorId(idReclamacao);
        List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoService.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);

        resposta.put("statusDisponiveis", statusDisponiveis);

        return resposta;
    }

    @RequestMapping("/incluirComplemento")
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody
    Map<String, Object> incluirComplemento(String descricaoComplemento, Integer codigoAcao, Integer idReclamacao, String cnpj){
        Map<String, Object> resposta = new HashMap<>();

        DocumentoFiscalReclamado reclamacao = reclamacaoService.reclamacaoPorId(idReclamacao);
        GENPessoaJuridica pessoaJuridica = contribuinteService.pessoaJuridicaPorCNPJ(cnpj);

        Boolean sucesso = reclamacaoService.alteracaoDeSituacaoReclamacaoPorEmpresa(reclamacao, codigoAcao, descricaoComplemento, pessoaJuridica);
        
        resposta.put("sucesso", sucesso);
        
        return resposta;
    }

    private String getUrlDownload(){
        String urlDownload = request.getRequestURI();
        urlDownload = urlDownload.replace("/listar", "/downloadAnexo");
        urlDownload = urlDownload.replaceAll("\\d*", "");
        return urlDownload;
    }
    
    private String getUrlBase() {
    	String contexto = "/nfg-web";
        String urlBase = request.getRequestURI();
        urlBase = urlBase.substring(urlBase.indexOf(contexto)+contexto.length(),urlBase.length());
        urlBase = urlBase.replace("/consultar", "");
        urlBase = urlBase.replaceAll("\\d*", "");
        return  urlBase;
    }
}