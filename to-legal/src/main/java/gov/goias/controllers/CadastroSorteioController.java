package gov.goias.controllers;

import java.text.ParseException;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author henrique-rh
 * @since 17/07/15.
 */
@Controller
public class CadastroSorteioController extends BaseController {

    @RequestMapping(value = "/admin/cadastro/sorteio", method = RequestMethod.GET)
    public ModelAndView viewCadastroSorteio() {
        ModelAndView mav = new ModelAndView("admin/cadastroSorteio");
        return mav;
    }

    @Transactional
    @RequestMapping(value = "/admin/cadastro/sorteio", method = RequestMethod.POST)
    public @ResponseBody String cadastrarSorteio(String password) throws ParseException {
        return "CadastroSorteioController 39";
    }

    @RequestMapping(value = "/admin/sorteio/{idSorteio}", method = RequestMethod.GET)
    public @ResponseBody Object carregaSorteio(@PathVariable(value = "idSorteio") String idSorteio) {
        return "CadastroSorteioController 44";
    }
}
