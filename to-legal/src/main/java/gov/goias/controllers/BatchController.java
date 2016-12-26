package gov.goias.controllers;

import java.net.BindException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author henrique-rh
 * @since 23/04/15.
 */
@Controller
@RequestMapping("/admin")
public class BatchController extends AdminBaseController {

    private Logger logger = LoggerFactory.getLogger(BatchController.class);

    @RequestMapping("geracaoDeBilhetes")
    public ModelAndView geracaoDeBilhetes() throws ParseException {
        ModelAndView modelAndView = new ModelAndView("batch/geracaoDeBilhetes");

        return modelAndView;
    }

    @RequestMapping("geracaoDePontuacao")
    public ModelAndView geracaoDePontuacao() throws ParseException {
        ModelAndView modelAndView = new ModelAndView("batch/geracaoDePontuacao");
        return modelAndView;
    }


    @RequestMapping("carregaDadosDoSorteioParaUsuario")
    public @ResponseBody Map<String, Object> carregaDadosDoSorteio(Integer idSorteio, BindException bind) throws ParseException {
    	return new HashMap();
    }

    @RequestMapping("carregaDadosDaRegraPontuacao")
    public @ResponseBody Map<String, Object> carregaDadosDaRegraPontuacao(Integer idRegra, BindException bind) {
        Map<String, Object> resposta = new HashMap<>();
        return new HashMap();
    }


    @RequestMapping("chamarBatchParaGerarBilhetes")
    public @ResponseBody Map<String, Object> chamarBatchParaGerarBilhetes(String idRegraSorteio, BindException bind, String password) throws ParseException {
        return new HashMap();
    }


    @RequestMapping("chamarBatchParaGerarPontuacao")
    public @ResponseBody Map<String, Object> chamarBatchParaGerarPontuacao(Integer idRegra, Date dataLimitePontuacao, BindException bind, String password) throws ParseException {
        return new HashMap();
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
        return getRestTemplate().postForObject(batchUrl + "/processamento/lotes/count", map, List.class);
    }

    @RequestMapping("processamento/lotes")
    public @ResponseBody List batchGetLotes() throws ParseException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        return getRestTemplate().postForObject(batchUrl + "/processamento/lotes", map, List.class);
    }

    @RequestMapping("processamento/reprocessar")
    public @ResponseBody String batchReprocessarLote() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        return getRestTemplate().postForObject(batchUrl + "/processamento/reprocessar", map, String.class);
    }

    @RequestMapping("processamento/revalidar")
    public @ResponseBody String batchRevalidarLote() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        return getRestTemplate().postForObject(batchUrl + "/processamento/revalidar", map, String.class);
    }

    @RequestMapping("/parametros")
    public ModelAndView viewParametros() {
        return new ModelAndView("admin/parametros");
    }

}
