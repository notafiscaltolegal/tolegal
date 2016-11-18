package gov.goias.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by bruno-cff on 22/06/2015.
 */

@Controller
@RequestMapping("portal/premiacao")
public class PremiacaoController extends BaseController{

    @RequestMapping(value = "/inicio")
    public ModelAndView viewPremiacao(){
        ModelAndView modelAndView = new ModelAndView("/premiacaoPortal");

        modelAndView.addObject("sorteios", new ArrayList<>());
        return modelAndView;
    }

    @RequestMapping(value = "/listarPremiacao/{page}")
    public @ResponseBody
    Map listarPremiacao(@PathVariable(value = "page") Integer page, Integer idSorteio){

        return new HashMap<>();
    }

}
