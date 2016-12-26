package gov.goias.controllers;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.goias.exceptions.NFGException;

/**
 * @author henrique-rh
 * @since 13/07/15.
 */
@Controller
public class AdminController extends BaseController {

    @RequestMapping(value = "/admin")
    public ModelAndView index () {
        return new ModelAndView("admin/index");
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("admin/login");
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public RedirectView login(String matricula, String senha) {
        return new RedirectView("/admin", true);
    }

    @RequestMapping(value = "/admin/logout")
    public RedirectView logout() {
        request.getSession().removeAttribute(SESSION_ADMIN_LOGADO);
        return new RedirectView("/admin/login", true);
    }

}
