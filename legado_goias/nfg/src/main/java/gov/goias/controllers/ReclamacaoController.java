package gov.goias.controllers;

import gov.goias.entidades.*;
import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by bruno-cff on 30/09/2015.
 */
@Controller
@RequestMapping(value = {"contribuinte/reclamacao" , "/contador/reclamacao"})
public class ReclamacaoController extends BaseController{

    private static final Logger logger = Logger.getLogger(ReclamacaoController.class);

    @Autowired
    DocumentoFiscalReclamado documentoFiscalReclamadoRepository;

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
            DocumentoFiscalReclamado documento =  documentoFiscalReclamadoRepository.retornaDocumentoFiscalPorId(idDocumento);
            InputStreamResource inputStreamResource = new InputStreamResource(documento.getImgDocumentoFiscal().getBinaryStream());

            HttpHeaders headers = new HttpHeaders();
            documentoFiscalReclamadoRepository.setContentType(documento, headers);

            response = new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);

            return response;

        } catch (Exception e) {
            logger.error(e);
            return response;
        }
    }

    @RequestMapping("/listar/{page}")
    public @ResponseBody
    Map<String, Object> listar(@PathVariable(value = "page") Integer page, String numeroCnpj) {
        Integer max = 3;
        Integer count = 0;
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        Map reclamacoesMap = documentoFiscalReclamadoRepository.listDocumentoFiscalReclamado(numeroCnpj, max, page);
        List<DocumentoFiscalReclamado> reclamacoes = (List<DocumentoFiscalReclamado>) reclamacoesMap.get("list");
        List<DocumentoFiscalReclamado> listDocumentos = new ArrayList<>();

        for(DocumentoFiscalReclamado reclamacao : reclamacoes){
            List<ComplSituacaoReclamacao> statusDisponiveis = documentoFiscalReclamadoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);
            if(statusDisponiveis != null && statusDisponiveis.size() > 0){
               reclamacao.setDisableRadioBtn("");
               listDocumentos.add(reclamacao);
            }else{
                reclamacao.setDisableRadioBtn("disabled");
                listDocumentos.add(reclamacao);
            }
        }

        count = (Integer) reclamacoesMap.get("count");


        pagination.put("total", count);
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
        DocumentoFiscalReclamado reclamacao = documentoFiscalReclamadoRepository.findReclamacaoPorId(idReclamacao);
        List<ComplSituacaoReclamacao> statusDisponiveis = documentoFiscalReclamadoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);

        resposta.put("statusDisponiveis", statusDisponiveis);

        return resposta;
    }

    @RequestMapping("/incluirComplemento")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody
        Map<String, Object> incluirComplemento(String descricaoComplemento, Integer codigoAcao, Integer idReclamacao, String cnpj){
        Map<String, Object> resposta = new HashMap<>();

        DocumentoFiscalReclamado reclamacao = documentoFiscalReclamadoRepository.findReclamacaoPorId(idReclamacao);
        GENPessoaJuridica pessoaJuridica = documentoFiscalReclamadoRepository.findPessoaJuridicaByCnpj(cnpj);

        resposta = documentoFiscalReclamadoRepository.alteracaoDeSituacaoReclamacaoPorEmpresa(reclamacao, codigoAcao, descricaoComplemento, pessoaJuridica);

        return resposta;
    }

    private String getUrlDownload(){
        String urlDownload = request.getRequestURI();
        urlDownload = urlDownload.replace("/listar", "/downloadAnexo");
        urlDownload = urlDownload.replaceAll("\\d*", "");
        return urlDownload;
    }

    private String getUrlBase() {
        String urlBase = request.getRequestURI();
        urlBase = urlBase.substring(urlBase.indexOf("/nfg")+4,urlBase.length());
        urlBase = urlBase.replace("/consultar", "");
        urlBase = urlBase.replaceAll("\\d*", "");
        return  urlBase;
    }

}
