package gov.goias.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.dtos.DTOCnaeCadastro;


/**
 * Created by thiago-mb on 18/07/2014.
 */
@Controller
@RequestMapping("/portal/cnae")
public class CadastroCnaeController extends BaseController {

    @RequestMapping(value = "/cadastro")
    public  ModelAndView listSecao(HttpServletRequest request){
        return new ModelAndView("/cadastro-cnae");
    }

    @RequestMapping(value = "/list-cnaes-autorizados")
    public @ResponseBody Map listCnaes(HttpServletRequest request){
        return new HashMap();
    }

    @RequestMapping(value = "/list-secao")
    public @ResponseBody Map listSecao(){
        return new HashMap();
    }

    @RequestMapping(value = "/list-divisao")
    public @ResponseBody Map listDivisao(String idSecao){
        return new HashMap();
    }

    @RequestMapping(value = "/list-grupo")
    public @ResponseBody Map listGrupo(String idDivisao){
        return new HashMap();
    }
    @RequestMapping(value = "/list-classe")
    public @ResponseBody Map listClasse(String idGrupo){
        return new HashMap();
    }
    @RequestMapping(value = "/list-subclasse")
    public @ResponseBody Map listSubClasse(String idClasse){
        return new HashMap();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cadastrar-cnae")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ModelAndView cadastrarCnae(DTOCnaeCadastro dtoCnaeCadastro, HttpServletRequest request) throws Exception {
		return new ModelAndView();

    }

    @RequestMapping(method = RequestMethod.POST, value = "/excluir-cnae")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map excluirCnae(Integer idCnaeAutorizadoDel, HttpServletRequest request) throws Exception {
        return new HashMap();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map update(Integer cnae, String data, HttpServletRequest request) throws Exception {

        return new HashMap();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ha-contribuintes-cadastrados")
    public @ResponseBody Map validarNovaData(Long cnae, HttpServletRequest request) throws Exception {

        return new HashMap();
    }
}
