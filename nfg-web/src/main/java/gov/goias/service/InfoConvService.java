package gov.goias.service;


import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import gov.goias.bindings.ArrayOfPessoaPerfil3;
import gov.goias.bindings.ConsultarCPFP3;
import gov.goias.bindings.ConsultarCPFP3Response;
import gov.goias.bindings.ConsultarCPFP3T;
import gov.goias.bindings.ConsultarCPFP3TResponse;
import gov.goias.bindings.PessoaPerfil3;

/**
* Created by lucas-mp on 05/08/15.
*/
public class InfoConvService extends WebServiceGatewaySupport {

    public String consultaPorCpfInfoConv(String cpf){
        ConsultarCPFP3 consultarCPF = new ConsultarCPFP3();
        consultarCPF.setCPFUsuario(cpf);

        ConsultarCPFP3Response consultarCPFResponse = (ConsultarCPFP3Response) getWebServiceTemplate().marshalSendAndReceive(consultarCPF,
                new SoapActionCallback("https://infoconv.receita.fazenda.gov.br/ws/cpf/ConsultarCPFP3"));

        ArrayOfPessoaPerfil3 consultarCPFResult = consultarCPFResponse.getConsultarCPFP3Result();
        PessoaPerfil3 pessoaPerfil = consultarCPFResult.getPessoaPerfil3().get(0);

        return pessoaPerfil.getNome();
    }

    public PessoaPerfil3 consultaPorCpf(String cpf){
        ConsultarCPFP3T consultarCPF = new ConsultarCPFP3T();
        consultarCPF.setCPFUsuario(cpf);

        ConsultarCPFP3TResponse consultarCPFResponse = (ConsultarCPFP3TResponse) getWebServiceTemplate().marshalSendAndReceive(consultarCPF,

                new SoapActionCallback("https://infoconv.receita.fazenda.gov.br/ws/cpf/ConsultarCPFP3T"));

        ArrayOfPessoaPerfil3 consultarCPFResult = consultarCPFResponse.getConsultarCPFP3TResult();
//      PessoaPerfil3 pessoaPerfil = consultarCPFResult.getPessoaPerfil3().get(0);
        
        PessoaPerfil3 pessoaPerfil = new PessoaPerfil3();
        pessoaPerfil.setCPF("03066547175");

        return pessoaPerfil;
    }

}
