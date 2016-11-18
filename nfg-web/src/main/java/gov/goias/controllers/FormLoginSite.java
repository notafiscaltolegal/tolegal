package gov.goias.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by diogo-rs on 8/13/2014.
 */
@Controller
public class FormLoginSite {

    @RequestMapping("/form-login")
    public String getForm(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("endereco", request.getRequestURL().toString().replaceAll("form-login.*", "")+"images/site/certificado.png");
        response.addHeader("Access-Control-Allow-Origin", "*");
        return "form-login";
    }
}
