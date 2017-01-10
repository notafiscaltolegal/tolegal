package gov.goias.config;

import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.EmpresaParticipante;
import gov.goias.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author henrique-rh
 * @since 16/04/15.
 */
public class Boot implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(Boot.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Context destroyed");
    }

    @Override

    public void contextInitialized(ServletContextEvent arg0) {
        if (System.getProperty("spring.profiles.active").equals("prod")) {
            logger.info("Carregando empresas na memória");
            EmpresaParticipante empresaParticipanteRepository = (EmpresaParticipante) SpringUtils.getApplicationContext().getBean("empresaParticipante");
            empresaParticipanteRepository.listEmpresasParticipantes(1, 10, null, null, null, null, null);
        }

    }


}




