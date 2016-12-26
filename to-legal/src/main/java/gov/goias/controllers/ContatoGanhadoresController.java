package gov.goias.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by bruno-cff on 02/12/2015.
 */
@Controller
@RequestMapping("/portal/contato-ganhadores/contatoGanhadores")
//@RequestMapping("/contato-ganhadores/contatoGanhadores")
public class ContatoGanhadoresController extends BaseController{

    private static final Logger logger = Logger.getLogger(ContatoGanhadoresController.class);

    @RequestMapping("/inicio")
    public ModelAndView viewContatos(){
        ModelAndView modelAndView = new ModelAndView("/contato-ganhadores/contatoGanhadoresPortal");

        modelAndView.addObject("sorteios", new ArrayList<>());

        return modelAndView;
    }

    @RequestMapping(value = "/listarContatoDetalhe/{page}")
    public @ResponseBody
    Map listarContatoDetalhe(@PathVariable(value = "page") Integer page, Integer idPessoa){
        Map<String, Object> resposta = new HashMap<String, Object>();

        return resposta;
    }

    @RequestMapping(value = "/listarContatosRel")
    public ModelAndView listarContatosRel(@RequestParam(value = "selectSorteios") Integer selectSorteios){
        ModelAndView modelAndView = new ModelAndView("/contato-ganhadores/relatorioGanhadores");

        modelAndView.addObject("idSorteio",selectSorteios);

        return modelAndView;
    }

    @RequestMapping(value = "/listarContatosRelatorio/{page}")
    public @ResponseBody
    Map listarContatosRelatorio(@PathVariable(value = "page") Integer page, Integer idSorteio){
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        Integer max = 40;
        Integer count = 0;


        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("listContatos", new ArrayList<>());
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping(value = "/listarContatos/{page}")
    public @ResponseBody
    Map listarContatos(@PathVariable(value = "page") Integer page, Integer idSorteio){
        Map<String, Object> resposta = new HashMap<String, Object>();

        return resposta;
    }

    @RequestMapping("/viewContatoDetalhe")
    public ModelAndView viewContatoDetalhe(){
        ModelAndView modelAndView = new ModelAndView("/contato-ganhadores/viewContatoDetalhe");

        return modelAndView;
    }
}
