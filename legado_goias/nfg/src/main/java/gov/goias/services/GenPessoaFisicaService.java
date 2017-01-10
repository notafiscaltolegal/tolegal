package gov.goias.services;


import entidade.*;
import entidade.endereco.*;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.exceptions.NFGException;
import gov.goias.pattern.CircuitBraker;
import gov.goias.pattern.TimeoutFuture;
import gov.goias.util.CircuitBreakerOpenException;
import gov.goias.util.Duration;
import gov.goias.util.UriCallable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by lucas-mp on 1/8/2015.
 *
 * TIPO_TELEFONE
 1 - comercial
 2 - residencial
 3 - celular
 4 - contato
 5 - recado
 6 - Fax
 7 - Cobrança
 */
@Service
public class GenPessoaFisicaService implements IGenPessoaFisicaService {
    @Value("${gen.service}")
    private String serverAddr;

    private static final Logger logger = Logger.getLogger(GenPessoaFisicaService.class);

    private String senhaGenService="BOJK4(V2afv3ASD331sdtg553dda";

    Integer tipoTelefone = 4; //Tipo: TELEFONE CONTAT
    Integer tipoEndereco = 1; //Tipo: ENDERECO RESIDENCIAL

    public HttpClient httpClient;
    public HttpContext httpContext ;
    private CircuitBraker circuitBraker;

    public GenPessoaFisicaService(){
        httpClient = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        circuitBraker = new CircuitBraker(8000,Duration.create(60, TimeUnit.SECONDS),Duration.create(1, TimeUnit.MINUTES),5,15,50);
    }

    @Override
    public String getEmailPessoaFisicaWs(String idpessoa)   {
        logger.info("Gen Service: tentativa de Get Email Pessoa Fisica");
        if (idpessoa == null || idpessoa.trim().equals("")) {
            return null;
        }
        String jsonRetorno = callGetWs("/gen/email/get?idpessoa=" + idpessoa);
        if (respostaServicoInvalida(jsonRetorno)){
            return null;
        }
        HashMap<String,Object> result = null;
        try {
            result = new ObjectMapper().readValue(jsonRetorno, HashMap.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        String respostaEmail = (String) result.get("email");
//        return respostaEmail!=null ? respostaEmail.toLowerCase() : null;
        return respostaEmail;
    }

    @Override
    public List<Integer> idPessoaDeEmailExistente(String email) {
        logger.info("Gen Service: tentativa de Get ID Email Pessoa ");
        String jsonRetorno = callGetWs("/gen/email/pessoas?email=" + email);
        List<Integer> idPessoas = new ArrayList<>();
        if (respostaServicoInvalida(jsonRetorno) || jsonRetorno.length()==0){
            return null;
        }
        HashMap<String,Object> result = null;
        try {
            result = new ObjectMapper().readValue(jsonRetorno, HashMap.class);

            for (Map.Entry<String, Object> entry : result.entrySet())
            {
                idPessoas.add(Integer.parseInt((String) entry.getKey()));
            }

        } catch (IOException e) {
            logger.error("GenPessoaFisicaService:idPessoaDeEmailExistente" + e.getMessage(), e);
        }
        return idPessoas;
    }

    @Override
    public String getTelefonePessoaFisicaWs(String idpessoa) {
        logger.info("Gen Service: tentativa de Get Telefone Pessoa ");
        if (idpessoa == null || idpessoa.trim().equals("")) {
            return null;
        }
        String jsonRetorno = callGetWs("/gen/telefone/get?idpessoa=" + idpessoa+"&tipo="+tipoTelefone);
        if (respostaServicoInvalida(jsonRetorno)){
            return null;
        }
        HashMap<String,Object> result = null;
        try {
            result = new ObjectMapper().readValue(jsonRetorno, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ddd = (Integer) result.get("numeroDDD") != null? ((Integer)result.get("numeroDDD")).toString(): "";
        String telefone = (Integer) result.get("numeroTelefone") != null? ((Integer)result.get("numeroTelefone")).toString(): "";
        return ddd.concat(telefone);
    }

    @Override
    public GENPessoaFisica getPessoaFisicaWs(String cpf) {
        return null; //nao foi implementado porque o nfg esta fazendo esta consulta diretamente do banco
    }

    @Override
    public Credencial getSenhaPessoaFisicaWs(String cpf) {
        logger.info("Gen Service: tentativa de Get Senha Pessoa. Url: "+serverAddr+"/gen/senha/get?cpf="+cpf);
        String jsonRetorno = callGetWs("/gen/senha/get?cpf=" + cpf);
        if (respostaServicoInvalida(jsonRetorno)){
            return null;
        }
        HashMap<String,Object> result = null;
        try {
            result = new ObjectMapper().readValue(jsonRetorno, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Credencial credencial = new Credencial();
        String senha = (String) result.get("hashSenha");
        String portal = (String) result.get("portal");
        String status = (String) result.get("status");
        credencial.setInfoSenha(senha);
        credencial.setIndiPortal(portal != null ? portal.charAt(0) : null);
        credencial.setStatusCredencial(status != null ? status.charAt(0) : null);
        return credencial;
    }

    @Override
    public Endereco getEnderecoPessoaFisicaWs(String idpessoa) {
        logger.info("Gen Service: tentativa de Get Endereco Pessoa ");
        if (idpessoa == null || idpessoa.trim().equals("")) {
            return null;
        }
        String jsonRetorno = callGetWs("/gen/endereco/get?idpessoa=" + idpessoa+"&tipo="+tipoEndereco);
        if (respostaServicoInvalida(jsonRetorno)){
            return null;
        }
        return Endereco.fromJson(jsonRetorno);
    }

    @Override
    public Logradouro getLogradouroWs(Integer cep) {
        logger.info("Gen Service: tentativa de Get Logradouro");
        String jsonRetorno = callGetWs("/gen/logradouro/get?cep=" + cep);
        if (respostaServicoInvalida(jsonRetorno)){
            return null;
        }
        return Logradouro.fromJson(jsonRetorno);
    }

    @Override
    public List<Municipio> getMunicipiosPorEstadoWs(String uf) {
        logger.info("Gen Service: tentativa de Get Municipio Por Estado");
        String jsonRetorno = callGetWs("/gen/municipio/get?uf=" + uf);
        if (respostaServicoInvalida(jsonRetorno)){
            return null;
        }
        return Municipio.municipiosfromJson(jsonRetorno);
    }

    @Override
    public String setPessoaFisicaWs(GENPessoaFisica pessoaFisica) {
        logger.info("Gen Service: tentativa de Set Pessoa Fisica");
        PessoaFisica pfGen = new PessoaFisica();
        pfGen.setNumeroCpf(pessoaFisica.getCpf());
        pfGen.setNome(pessoaFisica.getNome());
        pfGen.setDataNascimento(pessoaFisica.getDataDeNascimento());
        pfGen.setNomeMae(pessoaFisica.getNomeDaMae());

        String response =  callPostWs("/gen/put/", pfGen.toJson());
        if (respostaServicoInvalida(response)){
            return null;
        }
        return response;
    }

    @Override
    public String setEmailPessoaFisicaWs(String email, Integer idPessoa,String matricula) {
        logger.info("Gen Service: tentativa de Set Email Pessoa Fisica");
        EmailPessoa emailPessoa = new EmailPessoa();
        emailPessoa.setEmail(email.toLowerCase());
        emailPessoa.setIdPessoa(idPessoa);
        String response = callPostWs("/gen/email/put/"+matricula, emailPessoa.toJson());
        if (respostaServicoInvalida(response)){
            return null;
        }
        return response;
    }


    @Override
    public String setTelefonePessoaFisicaWs(Integer dddTelefone,Integer nrTelefone, Integer idPessoa,String matricula) {
        logger.info("Gen Service: tentativa de Set Telefone Pessoa Fisica");
        TelefonePessoa telefonePessoa = new TelefonePessoa();
        telefonePessoa.setNumeroDDD(dddTelefone);
        telefonePessoa.setNumeroTelefone(nrTelefone);
        telefonePessoa.setTipoTelefone(tipoTelefone); //TIPO: Contato
        telefonePessoa.setIdPessoa(idPessoa);
        String response = callPostWs("/gen/telefone/put/"+matricula, telefonePessoa.toJson());
        if (respostaServicoInvalida(response)){
            return null;
        }
        return response;
    }


    @Override
    public String setSenhaPessoaFisicaWs(String cpf, String senha,String matricula) {
        logger.info("Gen Service: tentativa de Set Senha Pessoa Fisica");
        Credencial credencial = new Credencial();
        credencial.setNumeroCpf(cpf);
        credencial.setSenha(senha);
        String response =  callPostWs("/gen/senha/put/"+matricula, credencial.toJson());
        if (respostaServicoInvalida(response)){
            return null;
        }
        return response;
    }

    @Override
    public String setEnderecoPessoaFisicaWs(Endereco endereco,String matricula,Integer idPessoa) {
        logger.info("Gen Service: tentativa de Set Endereco Pessoa Fisica");
        String response =  callPostWs("/gen/endereco/put/" + matricula+"?idpessoa="+idPessoa+"&tipo="+tipoEndereco, endereco.toJson());
        if (respostaServicoInvalida(response)){
            return null;
        }
        return response;
    }

    @Override
    public boolean enviaEmailRecuperacaoSenhaPessoaFisicaWs(Integer idPessoa,String path) {
        logger.info("Gen Service: tentativa de Envia Email Recuperacao Senha Pessoa Fisica");
        String response =  callGetWs("/gen/senha/recuperacao?idpessoa=" + idPessoa + "&path=" + path);
        if (respostaServicoInvalida(response)){
            return false;
        }
        return Boolean.parseBoolean(response);
    }

    @Override
    public boolean verificaTokenSenhaPessoaFisicaWs(String cpf, String token) {
        logger.info("Gen Service: tentativa de Verifica Token Senha Pessoa Fisica");
        String response =   callGetWs("/gen/senha/verifica?token=" + token+"&cpf="+cpf);
        if (respostaServicoInvalida(response)){
            return false;
        }
        return Boolean.parseBoolean(response);
    }

    public boolean respostaServicoInvalida(String resposta){
        if (resposta==null) return true;

        String respostaTratada = resposta.toUpperCase();
        if(respostaTratada.contains("EXCEPTION")|| respostaTratada.contains("ERRO:")|| respostaTratada.contains("FALHA AO GRAVAR")
                || respostaTratada.contains("ERROR REPORT")){
            logger.info("Erro em Gen Servico: "+resposta);
            return true;
        }
        return false;
    }

    public String callPostWs(String path, String json)  throws CircuitBreakerOpenException {
        HttpPost post = new HttpPost("http://"+serverAddr+path);
        try{
            StringEntity entity = new StringEntity(json,"UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            post.setHeader("Accept", "application/json");

            try{
                final TimeoutFuture<String> f = circuitBraker.call(new UriCallable(post));
                return f.get();

            } catch (CancellationException e) {
                logger.error("Ocorreu um Timeout no Gen Serviço", e);
                return null;
            } catch (ExecutionException e) {
                logger.error("Falha na comunicação Gen: ExecutionException", e);
                return null;
            } catch (InterruptedException e) {
                logger.error("Falha na comunicação Gen: InterruptedException", e);
                return null;
            }

        }  catch (Exception e) {
            logger.error("Falha ao chamar Gen Serviço: "+ e.getMessage());
            return null;
        }
    }

    public String callGetWs(String path)  throws CircuitBreakerOpenException {
        HttpGet get = new HttpGet("http://"+serverAddr+path);
        try {
            get.setHeader("Content-type", "application/json");
            get.setHeader("Accept", "application/json");
            try{
                final TimeoutFuture<String> f = circuitBraker.call(new UriCallable(get));
                return f.get();

            } catch (CancellationException e) {
                logger.error("Ocorreu um Timeout no Gen Serviço", e);
                return null;
            } catch (ExecutionException e) {
                logger.error("Falha na comunicação Gen: ExecutionException", e);
                logger.error(e.getMessage());
                return null;
            } catch (InterruptedException e) {
                logger.error("Falha na comunicação Gen: InterruptedException", e);
                return null;
            }
        } catch (Exception e) {
            logger.error("Falha ao chamar Gen Serviço: "+ e.getMessage());
            return null;
        }
    }


}