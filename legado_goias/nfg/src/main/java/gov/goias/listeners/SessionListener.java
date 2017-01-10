package gov.goias.listeners;

import gov.goias.controllers.BaseController;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.services.ContadorLoginService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	private Logger logger = Logger.getLogger(SessionListener.class);

    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        PessoaParticipante cidadao = (PessoaParticipante) session.getAttribute(BaseController.SESSION_CIDADAO_LOGADO);

        logger.info("Sessão CRIADA: " + session.getId() + ". Cidadao: " + (cidadao!=null?cidadao.getId():"null"));
    }

	public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        PessoaParticipante cidadao = (PessoaParticipante) session.getAttribute(BaseController.SESSION_CIDADAO_LOGADO);

        logger.info("Sessão DESTRUIDA: " + session.getId() + ". Cidadao: " + (cidadao!=null?cidadao.getId():"null"));

    }
}
