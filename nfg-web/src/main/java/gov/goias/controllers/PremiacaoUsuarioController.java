package gov.goias.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.dtos.DTOPremiacao;

/**
 * Created by bruno-cff on 13/11/2015.
 */
@Controller
@RequestMapping("/portal/premiacao/usuario")
public class PremiacaoUsuarioController extends BaseController{

    private static final Logger logger = Logger.getLogger(PremiacaoUsuarioController.class);

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("premiacao/index");
        modelAndView.addObject("sorteios", new ArrayList<>());
        return modelAndView;
    }



//    @RequestMapping("/gerarRelatorio")
//    public ModelAndView gerarRelatorio(
//            @RequestParam(value="data") String data,
//            @RequestParam(value="dataFim") String dataFim,
//                                       @RequestParam(value="situacao") Integer situacao,
//                                       @RequestParam(value="motivoReclamacao") Integer motivoReclamacao,
//                                       @RequestParam(value="tipoDocFiscalReclamacao") Integer tipoDocFiscalReclamacao,
//                                       @RequestParam(value="recNoPrazo") Integer recNoPrazo) {
//        ModelAndView modelAndView = new ModelAndView("reclamacao/relatorio");
//        modelAndView.addObject("situacao",situacao);
//        modelAndView.addObject("data",data);
//        modelAndView.addObject("dataFim",dataFim);
//        modelAndView.addObject("motivoReclamacao",motivoReclamacao);
//        modelAndView.addObject("tipoDocFiscalReclamacao",tipoDocFiscalReclamacao);
//        modelAndView.addObject("recNoPrazo",recNoPrazo);
//        return modelAndView;
//    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        //o parametro true faz com que o binder gere data nula para string vazia do js
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("/listarPremiacoes/{page}")
    public @ResponseBody Map listarReclamacoesRel(@PathVariable(value = "page") Integer page, Integer sorteio) {
        Integer max = 20;

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", 0);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("premiacoes", new ArrayList<>());
        resposta.put("pagination", pagination);
        return resposta;
    }

    @RequestMapping("/editarPremiacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map editarPremiacao(String dataResgate, String infoResgate, Integer idPremioBilhete) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        HashMap<String, Object> dados = new HashMap<>();
        dados.put("id", idPremioBilhete);

        return resposta;
    }


}
