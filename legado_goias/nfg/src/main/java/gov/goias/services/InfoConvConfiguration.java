package gov.goias.services;

import gov.goias.bindings.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

/**
* Created by lucas-mp on 05/08/15.
*/
@Configuration
public class InfoConvConfiguration  {

    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("gov.goias.bindings");
        return marshaller;
    }


    @Bean
    public InfoConvService infoConvService(Jaxb2Marshaller marshaller) {
        InfoConvService client = new InfoConvService();
        client.setDefaultUri("https://infoconv.receita.fazenda.gov.br/ws/cpf/ConsultarCPF.asmx");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}


