package gov.goias.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.goias.exceptions.NFGException;

/**
 * Created by diogo-rs on 8/1/2014.
 */

@Controller
public class LoginContadorController extends BaseController {

    Logger logger = Logger.getLogger(LoginContadorController.class);

    @RequestMapping("/contador")
    public ModelAndView index() {
        return new ModelAndView("/contador/index");
    }

    @RequestMapping("/login-contador")
    public ModelAndView login(String login, String senha, HttpServletRequest request) throws NFGException{
        try{
            RedirectView redirectView = new RedirectView("/contador/contribuintes/cadastro", true);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        } catch(Exception e){
            logger.info(e.getMessage(), e);
            throw new NFGException(e.getMessage(), "/contador");
        }
    }
}
