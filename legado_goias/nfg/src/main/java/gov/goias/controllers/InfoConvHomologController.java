package gov.goias.controllers;

import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.RegraSorteio;
import gov.goias.persistencia.historico.HistoricoNFG;
import gov.goias.services.InfoConvConfiguration;
import gov.goias.services.InfoConvService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucas-mp on 10/10/14.
 */
@RestController
@RequestMapping("/infoconvhom")
public class InfoConvHomologController   {

    private static final Logger logger = Logger.getLogger(InfoConvHomologController.class);

    @RequestMapping("/consultar")
    public Map consultar(@RequestParam(value="cpf") String cpf) {
//        ApplicationContext ctx = SpringApplication.run(InfoConvConfiguration.class);
//
//        InfoConvService infoConvService = ctx.getBean(InfoConvService.class);


        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("gov.goias.bindings");

        logger.info("instanciei um novo marshaller Jaxb");

        InfoConvService infoConvService = new InfoConvService();
        infoConvService.setDefaultUri("https://infoconv.receita.fazenda.gov.br/ws/cpf/ConsultarCPF.asmx");
        infoConvService.setMarshaller(marshaller);
        infoConvService.setUnmarshaller(marshaller);

        logger.info("instanciei um novo InfoConvService");

        Map<String,Object> mapa = new HashMap<String,Object>();

        logger.info("Chamada do serviço consultaPorCpfInfoConvHomolog");

        String nome=null;

        try{
            nome = infoConvService.consultaPorCpfInfoConvHomolog(cpf);
        }catch (Exception e){
            logger.info("Erro na chamada ao servico: \n\r");
            e.printStackTrace();
        }

        mapa.put("nome",nome);

        logger.info("Retorno do nome +" + nome);

        return mapa;
    }

    @RequestMapping("/consultarprod")
    public Map consultarProd(@RequestParam(value="cpf") String cpf) {
//        ApplicationContext ctx = SpringApplication.run(InfoConvConfiguration.class);
//
//        InfoConvService infoConvService = ctx.getBean(InfoConvService.class);


        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("gov.goias.bindings");

        logger.info("instanciei um novo marshaller Jaxb");

        InfoConvService infoConvService = new InfoConvService();
        infoConvService.setDefaultUri("https://infoconv.receita.fazenda.gov.br/ws/cpf/ConsultarCPF.asmx");
        infoConvService.setMarshaller(marshaller);
        infoConvService.setUnmarshaller(marshaller);

        logger.info("instanciei um novo InfoConvService");

        Map<String,Object> mapa = new HashMap<String,Object>();

        logger.info("Chamada do serviço consultaPorCpfInfoConvHomolog");

        String nome=null;

        try{
            nome = infoConvService.consultaPorCpfInfoConv(cpf);
        }catch (Exception e){
            logger.info("Erro na chamada ao servico: \n\r");
            e.printStackTrace();
        }

        mapa.put("nome",nome);

        logger.info("Retorno do nome +" + nome);

        return mapa;
    }


}









