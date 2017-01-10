package gov.goias.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by thiago-mb on 18/07/2014.
 */
@Controller
@RequestMapping("/portal/cfop")
public class CadastroCfopController extends BaseController {

    @RequestMapping(value = "/cadastro")
    public  ModelAndView cadastro(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/cfop/cadastro");
        return modelAndView;
    }

    @RequestMapping(value = "/list-cfops-autorizados/{page}")
    public @ResponseBody Map listarCfops(@PathVariable(value="page") Integer page,  String codigoCFOP, String descricaoCFOP)
    {

        return new HashMap();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cadastrar-cfop")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map cadastrarCfop(Integer codigo) throws Exception
    {
        return new HashMap();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/excluir-cfop")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map excluirCfop(Integer idCfopAutorizado) throws Exception
    {
        return new HashMap();
    }
}
