package gov.goias.controllers;

import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.GENTipoLogradouro;
import gov.goias.entidades.GENUf;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.persistencia.MonitorRepository;
import gov.goias.util.*;
import org.apache.log4j.Logger;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucas-mp on 10/10/14.
 */
@Controller
public class MonitorController extends AdminBaseController {
    private static final Logger logger = Logger.getLogger(MonitorController.class);

    private @Autowired
    ServletContext servletContext;
    @Autowired
    PessoaParticipante pessoaParticipanteRepository;

    @Autowired
    MonitorRepository monitorRepository;

    @Autowired
    MensagemEmailEnvioService mensagemEmailEnvio;

    @Autowired
    GENUf ufRepository;
    @Autowired
    GENTipoLogradouro tipoLogradouroRepository;

    @Autowired
    GENPessoaFisica genPessoaRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public @ResponseBody String monitorarNFG(HttpServletResponse response) {
        int status = monitorRepository.efetuarMonitoramentoNFGWeb();

        if(status==0){ //ACESSANDO MONITOR DO BATCH
            try {
                status = getRestTemplateNoAuth().getForObject(batchUrl + "bmonitor/start", Integer.class);
            } catch (Exception e) {
                logger.error(e.getMessage());
                status = 3;
            }
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return ""+status;

    }

    @RequestMapping("/estatistica/jpa")
    public @ResponseBody String gerarEstatisticaJpa(HttpServletResponse response) {
        EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
        EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
        EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl)emf;
        Statistics stats = empImpl.getSessionFactory().getStatistics();
        System.out.println("******************************************************************************************"+
                "\n"+stats+"\n"+
                "******************************************************************************************");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return stats.toString();
    }

}
