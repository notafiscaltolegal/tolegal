package gov.goias.controllers;

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

        return "HomeController 35";
    }
}
