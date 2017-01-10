package gov.goias.controllers;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author henrique-rh
 * @since 13/08/2014
 */
@Controller
@RequestMapping(value = {"/busca", "/portal/busca"})
public class BuscaController extends BaseController {
    Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("empresas")
    public ModelAndView viewEmpresas() {
        ModelAndView modelAndView = new ModelAndView("busca/empresasParticipantes");
        return modelAndView;
    }

    @RequestMapping("empresas/{page}")
    public @ResponseBody Map empresas(@PathVariable(value="page") Integer page, Long idSubClasseCnae, String nome, String cnpj, String codgMunicipio, String nomeBairro) {
        return new HashMap();
    }

    @RequestMapping("empresa/{numrInscricao}")
    public ModelAndView viewEmpresa(@PathVariable("numrInscricao") String numrInscricao) {
        ModelAndView modelAndView = new ModelAndView("busca/detalheEmpresaParticipante");
        modelAndView.addObject("endereco","Av Pinheiro Chagas, 856, Jundiaí, Anápolis, Goiás");

        return modelAndView;
    }
}
