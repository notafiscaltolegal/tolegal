package gov.goias.controllers;

import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.interceptors.CidadaoInterceptor;
import gov.goias.interceptors.ContadorInterceptor;
import gov.goias.interceptors.PortalInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author henrique-rh
 * @since 22/07/2014
 */
@Controller
public class HomeController extends BaseController {

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/cidadao/login", true);
    }

    @RequestMapping("/portal/home")
    public ModelAndView indexPortal() {
        ModelAndView model = new ModelAndView("indexPortal");
        return model;
    }

    @RequestMapping("/manutencao/{bool}/{pass}/{contexto}")
    public @ResponseBody String setEmManutencao(@PathVariable(value = "bool") String emManutencao, @PathVariable(value = "pass") String pass,  @PathVariable(value = "contexto") String contexto) {
        try {
            if (!pass.equals("senhamuitodificil")) {
                return "Valor inválido";
            }
            switch (contexto) {
                case "cidadao":
                    CidadaoInterceptor.emManutencao = Boolean.valueOf(emManutencao);
                    break;
                case "contribuinte":
                    ContadorInterceptor.emManutencao = Boolean.valueOf(emManutencao);
                    CertificateInterceptor.emManutencao = Boolean.valueOf(emManutencao);
                    break;
                case "empresa":
                    BuscaController.emManutencao = Boolean.valueOf(emManutencao);
                    break;
            }
        } catch (Exception e) {
            return "Valor inválido";
        }

        return emManutencao;
    }
}
