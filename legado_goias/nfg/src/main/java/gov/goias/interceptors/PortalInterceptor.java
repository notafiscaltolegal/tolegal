package gov.goias.interceptors;

import gov.goias.controllers.BaseController;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.exceptions.NFGException;
import gov.sefaz.controle.gerenciador.Portal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by diogo-rs on 7/21/2014.
 */
@Component
public class PortalInterceptor extends HandlerInterceptorAdapter {

    private static final String SESSION_PORTAL="portalLogado";

    private static ThreadLocal<Portal> usuarioPortal = new ThreadLocal<Portal>();

    private static void setUsuarioPortal(Portal portal) {
        usuarioPortal.set(portal);
    }

    public static Portal getUsuarioPortal() {
        return usuarioPortal.get();
    }

    @Autowired
    private GENPessoaFisica genPessoaFisica;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        validaAcesso(request);
        if (((HandlerMethod) handler).getBean() instanceof BaseController) {
            BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
            controller.setResponse(response);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {}

    private GENPessoaFisica validaAcesso(HttpServletRequest request) throws Exception {
        GENPessoaFisica admin = (GENPessoaFisica) request.getSession().getAttribute(SESSION_PORTAL);

        Portal portal = (Portal) request.getSession().getAttribute("portal");

        if (portal == null) {
            throw new NFGException("Usuário não logado", new ModelAndView(new RedirectView("/portalsefaz", false)));
        }

        setUsuarioPortal(portal);

        if (admin != null) {
            return admin;
        }

        admin = genPessoaFisica.findByNumeroBase(portal.getIdPessoa());//985309l genPessoaFisica.findByNumeroBase(684449l)
        request.getSession().setAttribute(SESSION_PORTAL, admin);

        return admin;
    }
}
