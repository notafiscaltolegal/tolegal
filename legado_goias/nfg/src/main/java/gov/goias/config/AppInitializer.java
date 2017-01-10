package gov.goias.config;

import gov.goias.util.PropertiesLoader;
import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class AppInitializer implements WebApplicationInitializer {

    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();

    Logger logger = Logger.getLogger(AppInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("Properties do jboss:" + System.getProperty("spring.profiles.active"));
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", propertiesLoader.load("environment.properties").getProperty("profile"));
        }
        logger.info("================================ ");
        logger.info("888b    888 8888888888 .d8888b.  ");
        logger.info("8888b   888 888       d88P  Y88b ");
        logger.info("88888b  888 888       888    888 ");
        logger.info("888Y88b 888 8888888   888        ");
        logger.info("888 Y88b888 888       888  88888 ");
        logger.info("888  Y88888 888       888    888 ");
        logger.info("888   Y8888 888       Y88b  d88P ");
        logger.info("888    Y888 888        'Y8888P88 ");
        logger.info("================================ ");
        logger.info("============versao:2.0========== ");
        logger.info("Environment: " + System.getProperty("spring.profiles.active"));
    }
}