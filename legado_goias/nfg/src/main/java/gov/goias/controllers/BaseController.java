package gov.goias.controllers;

import gov.goias.auth.Certificado;
import gov.goias.dtos.DTOMenu;
import gov.goias.entidades.GENCredencialUsuario;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author henrique-rh
 * @since 18/07/2014
 */

public class BaseController {

    public static final String SESSION_ADMIN_LOGADO = "adminLogadoSession";
    public static final String SESSION_CIDADAO_LOGADO = "cidadaoLogadoSession";
    public static final String SESSION_ID_CIDADAO_SEM_CREDENC = "idCidadaoSemCredencial";

    /**Henrique, Diogo, Allan, Giselle, Marcio, Leonardo...*/
    public static final List<String> CPFS_ADMINS = Arrays.asList("01265009163", "03317460180", "89464796120", "80635253100", "60746610149", "17331831000149", "63404931149", "00237371154", "00237371154", "03794778154", "00011590114");

    private static final Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private PessoaParticipante pessoaParticipanteRepository;

    protected HttpServletResponse response;

    protected GENEmpresa empresaLogada;

    protected GENPessoaFisica contadorLogado;

    protected GENCredencialUsuario adminLogado;

    private String message;

    public Certificado getCertificadoPf() {
        return certificadoPf;
    }

    public void setCertificadoPf(Certificado certificadoPf) {
        this.certificadoPf = certificadoPf;
    }

    private Certificado certificadoPf;

    @ModelAttribute("successMessage")
    public String getSuccessMessage() {
        String success = this.successMessage;
        this.successMessage = "";
        return success;
    }

    @ModelAttribute("mensagemDeErro")
    public String getMensagemDeErro() {
        String mensagemDeErro = this.mensagemDeErro;
        this.mensagemDeErro = "";
        return mensagemDeErro;
    }

//    @ModelAttribute("listaMenu")
//    public DTOMenu[] getListaMenu() {
//        return getMenus();
//    }

    @Autowired
    private RestTemplate restTemplate;

//    @Value("${urlMenu}")
//    private String urlMenus;
//
//    public DTOMenu[] getMenus() {
//        return restTemplate.getForObject(urlMenus, DTOMenu[].class);
//    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public void setMensagemDeErro(String mensagemDeErro) {
        this.mensagemDeErro = mensagemDeErro;
    }

    private String successMessage;
    private String mensagemDeErro;


    public void setErrorModal(String errorModal) {
        this.errorModal = errorModal;
    }

    private String errorModal;

    @ModelAttribute("errorModal")
    public String getErrorModal() {
        String error = this.errorModal;
        this.errorModal = "";
        return error;
    }

    private String errorMessage;

    @ModelAttribute("errorMessage2")
    public String getErrorMessage() {
        String error = this.errorMessage;
        this.errorMessage = "";
        return error;
    }

    @ModelAttribute("message")
    public String getMessage() {
        String error = this.message;
        this.message = "";
        return error;
    }

    public GENEmpresa getEmpresaLogada() {
        return empresaLogada;
    }

    public void setEmpresaLogada(GENEmpresa empresaLogada) {
        this.empresaLogada = empresaLogada;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpServletRequest getRequest() {
        return request;
    }


    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public GENPessoaFisica getContadorLogado() {
        return contadorLogado;
    }

    public void setContadorLogado(GENPessoaFisica contadorLogado) {
        this.contadorLogado = contadorLogado;
    }

    public Boolean isAdmin(){
        return (Boolean) request.getSession().getAttribute(SESSION_ADMIN_LOGADO);
    }

    public PessoaParticipante getCidadaoLogado() {
        logger.info("Interceptor pegando cidadao da sessao:" +request.getSession().getId());
        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);

        if (cidadao != null) {
            logger.info("CIDADAO "+cidadao.getId()+" na SESSAO " +request.getSession().getId()+" retornado.");
            return cidadao;
        }else{
            logger.info("CIDADAO nao encontrado na SESSAO " +request.getSession().getId()+". Url:"+request.getRequestURI());
        }
        return null;
    }


    public GENCredencialUsuario getAdminLogado() {
        return adminLogado;
    }

    public void setAdminLogado(GENCredencialUsuario adminLogado) {
        this.adminLogado = adminLogado;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}