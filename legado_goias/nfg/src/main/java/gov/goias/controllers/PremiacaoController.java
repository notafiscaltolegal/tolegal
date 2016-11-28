package gov.goias.controllers;

import gov.goias.dtos.DTOPremiacaoPortal;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.RegraSorteio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno-cff on 22/06/2015.
 */

@Controller
@RequestMapping("portal/premiacao")
public class PremiacaoController extends BaseController{

    @Autowired
    RegraSorteio regraSorteioRepository;

    @Autowired
    PessoaParticipante pessoaParticipanteRepository;

    @RequestMapping(value = "/inicio")
    public ModelAndView viewPremiacao(){
        ModelAndView modelAndView = new ModelAndView("/premiacaoPortal");

        List<RegraSorteio> sorteios = regraSorteioRepository.listSorteios();

        modelAndView.addObject("sorteios", sorteios);
        return modelAndView;
    }

    @RequestMapping(value = "/listarPremiacao/{page}")
    public @ResponseBody
    Map listarPremiacao(@PathVariable(value = "page") Integer page, Integer idSorteio){
        Integer max = 40;
        Integer count = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<DTOPremiacaoPortal> listPremiacoes = new ArrayList<>();

        if(idSorteio != null && idSorteio.toString() != ""){
            listPremiacoes = (List<DTOPremiacaoPortal>) pessoaParticipanteRepository.listarPremiacaoPortal(idSorteio, max, page, false);
            count = ((List<Integer>) pessoaParticipanteRepository.listarPremiacaoPortal(idSorteio, max, page, true)).get(0);
        }

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("listPremiacoes", listPremiacoes);
        resposta.put("pagination", pagination);

        return resposta;
    }

}
