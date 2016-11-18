package gov.goias.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Boot implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(Boot.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Context destroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

    }
}