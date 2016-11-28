package gov.goias.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by diogo-rs on 7/14/2014.
 */
@Controller
public class LoginPortalSefazController {

    @RequestMapping("/home-contador")
    public ModelAndView loginContadorPortalSefaz(HttpServletRequest request){
        return new ModelAndView(new RedirectView("/portal/home", true));
    }
}