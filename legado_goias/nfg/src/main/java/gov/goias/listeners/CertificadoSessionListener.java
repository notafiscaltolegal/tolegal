package gov.goias.listeners;

import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.services.ContadorLoginService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CertificadoSessionListener implements HttpSessionListener {

	private Logger logger = Logger.getLogger(CertificadoSessionListener.class);

    public void sessionCreated(HttpSessionEvent se) {
	    logger.debug("Sessão inciada!");
    }

	public void sessionDestroyed(HttpSessionEvent se) {
        se.getSession().removeAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO);
        se.getSession().removeAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA);
        logger.debug("Sessão destruida!");
	}
}
