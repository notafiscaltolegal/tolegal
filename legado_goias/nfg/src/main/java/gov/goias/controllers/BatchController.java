package gov.goias.controllers;

import gov.goias.entidades.RegraPontuacaoDocumentoFiscal;
import gov.goias.entidades.RegraSorteio;
import gov.goias.exceptions.NFGException;
import gov.goias.interceptors.PortalInterceptor;
import gov.goias.util.BasicAuthInterceptor;
import gov.goias.util.HeaderHttpRequestInterceptor;
import gov.sefaz.controle.gerenciador.Portal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.BindException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author henrique-rh
 * @since 23/04/15.
 */
@Controller
@RequestMapping("/admin")
public class BatchController extends AdminBaseController {

    private Logger logger = LoggerFactory.getLogger(BatchController.class);

    @Autowired
    private RegraSorteio regraSorteioRepository;

    @Autowired
    private RegraPontuacaoDocumentoFiscal regraPontuacaoDocumentoFiscalRepo;

    @RequestMapping("geracaoDeBilhetes")
    public ModelAndView geracaoDeBilhetes() throws ParseException {
        ModelAndView modelAndView = new ModelAndView("batch/geracaoDeBilhetes");

        List<RegraSorteio> sorteios = regraSorteioRepository.listSorteios();

        modelAndView.addObject("sorteios", sorteios);
        return modelAndView;
    }

    @RequestMapping("geracaoDePontuacao")
    public ModelAndView geracaoDePontuacao() throws ParseException {
        ModelAndView modelAndView = new ModelAndView("batch/geracaoDePontuacao");

        List<RegraPontuacaoDocumentoFiscal> regras = regraPontuacaoDocumentoFiscalRepo.list();

        modelAndView.addObject("regras", regras);
        return modelAndView;
    }


    @RequestMapping("carregaDadosDoSorteioParaUsuario")
    public @ResponseBody Map<String, Object> carregaDadosDoSorteio(Integer idSorteio, BindException bind) throws ParseException {
        return regraSorteioRepository.carregaDadosDoSorteioParaUsuario(idSorteio);
    }

    @RequestMapping("carregaDadosDaRegraPontuacao")
    public @ResponseBody Map<String, Object> carregaDadosDaRegraPontuacao(Integer idRegra, BindException bind) {
        RegraPontuacaoDocumentoFiscal regraPontuacao = regraPontuacaoDocumentoFiscalRepo.get(idRegra);
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("regraPontuacao", regraPontuacao);
        return resposta;
    }


    @RequestMapping("chamarBatchParaGerarBilhetes")
    public @ResponseBody Map<String, Object> chamarBatchParaGerarBilhetes(String idRegraSorteio, BindException bind, String password) throws ParseException {
        if (!password.toUpperCase().equals(adminLogado.getRawPassword())) {
            throw new NFGException("Senha não confere");
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("idRegraSorteio", idRegraSorteio);
        String retorno;
        try {
            retorno = getRestTemplate().postForObject(batchUrl + "sorteio/bilhetes/iniciar", map, String.class);
        } catch (HttpClientErrorException e) {
            retorno = e.getResponseBodyAsString();
            response.setStatus(e.getStatusCode().value());
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("message", retorno);
        return resposta;
    }


    @RequestMapping("chamarBatchParaGerarPontuacao")
    public @ResponseBody Map<String, Object> chamarBatchParaGerarPontuacao(Integer idRegra, Date dataLimitePontuacao, BindException bind, String password) throws ParseException {
        if (!password.toUpperCase().equals(adminLogado.getRawPassword())) {
            throw new NFGException("Senha não confere");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("idRegra", idRegra.toString());
        map.add("dataLimitePontuacao", dateFormat.format(dataLimitePontuacao));
        String retorno;
        try {
            retorno = getRestTemplate().postForObject(batchUrl + "sorteio/pontuacao/iniciar", map, String.class);
        } catch (HttpClientErrorException e) {
            retorno = e.getResponseBodyAsString();
            response.setStatus(e.getStatusCode().value());
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("message",retorno);
        return resposta;
    }

    @RequestMapping("status/{job}")
    public @ResponseBody Map statusPontuacao(@PathVariable(value = "job") String job) {
        return getRestTemplate().getForObject(batchUrl + "sorteio/"+job+"/status", Map.class);
    }

    @RequestMapping("/processamento")
    public ModelAndView viewProcessamento() {
        return new ModelAndView("admin/processamento");
    }

    @RequestMapping("processamento/lotes/count")
    public @ResponseBody List batchCountLotes() throws ParseException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("status", request.getParameter("status"));
        map.add("tipo", request.getParameter("tipo"));
        return getRestTemplate().postForObject(batchUrl + "/processamento/lotes/count", map, List.class);
    }

    @RequestMapping("processamento/lotes")
    public @ResponseBody List batchGetLotes() throws ParseException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("status", request.getParameter("status"));
        map.add("tipo", request.getParameter("tipo"));
        return getRestTemplate().postForObject(batchUrl + "/processamento/lotes", map, List.class);
    }

    @RequestMapping("processamento/reprocessar")
    public @ResponseBody String batchReprocessarLote() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("idLote", request.getParameter("idLote"));
        return getRestTemplate().postForObject(batchUrl + "/processamento/reprocessar", map, String.class);
    }

    @RequestMapping("processamento/revalidar")
    public @ResponseBody String batchRevalidarLote() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("idLote", request.getParameter("idLote"));
        return getRestTemplate().postForObject(batchUrl + "/processamento/revalidar", map, String.class);
    }

    @RequestMapping("/parametros")
    public ModelAndView viewParametros() {
        return new ModelAndView("admin/parametros");
    }

}
