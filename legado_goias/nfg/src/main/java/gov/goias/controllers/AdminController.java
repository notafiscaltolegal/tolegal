package gov.goias.controllers;

import gov.goias.entidades.GENCredencialUsuario;
import gov.goias.entidades.RegraSorteio;
import gov.goias.entidades.SorteioPontuacao;
import gov.goias.entidades.enums.StatusProcessamentoSorteio;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.Historico;
import gov.goias.persistencia.historico.HistoricoAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author henrique-rh
 * @since 13/07/15.
 */
@Controller
public class AdminController extends BaseController {

    @Autowired
    private GENCredencialUsuario genCredencialUsuarioRepository;

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
        try {
            senha = senha.toUpperCase();
            GENCredencialUsuario usuario = genCredencialUsuarioRepository.findByMatriculaAndPassword(matricula, senha);
            usuario.setRawPassword(senha);
            request.getSession().setAttribute(SESSION_ADMIN_LOGADO, usuario);
        } catch (NoResultException e) {
            throw new NFGException("Usuário ou senha inválido", "/admin/login");
        }
        return new RedirectView("/admin", true);
    }

    @RequestMapping(value = "/admin/logout")
    public RedirectView logout() {
        request.getSession().removeAttribute(SESSION_ADMIN_LOGADO);
        return new RedirectView("/admin/login", true);
    }

}
