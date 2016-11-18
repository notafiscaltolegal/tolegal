package gov.goias.exceptions;

import gov.goias.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author henrique-rh
 * @since 17/07/2014
 */
@Controller
@RequestMapping("/errors")
public class PageErrorController extends BaseController {

    @RequestMapping(value = "/404")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView handle404(HttpServletResponse response) {

        ModelAndView model = new ModelAndView("errors/404");
        String message = (String) request.getAttribute("javax.servlet.error.message");

        model.addObject("message", message);

        return model;
    }

    @RequestMapping(value = "/401")
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ModelAndView handle401() {
        ModelAndView model = new ModelAndView("errors/401");
        String message = (String) request.getAttribute("javax.servlet.error.message");

        model.addObject("message", message);

        return model;
    }
}
