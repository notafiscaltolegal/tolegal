package gov.goias.controllers;

import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.Mensagem;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.services.ContadorLoginService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**k
 * Created by bruno-cff on 20/08/2015.
 */
@ControllerAdvice
public class UsuarioController  {
    private static final Logger logger = Logger.getLogger(UsuarioController.class);
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Mensagem mensagemRepository;

    @ModelAttribute(value = "nomeUsuario")
    public String getNomeUsuario() {
        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
        if (cidadao != null && cidadao.getGenPessoaFisica() != null) {
            logger.info("cidadao em getNomeUsuario() "+cidadao.getId()+". Sessao:"+request.getSession().getId());
            return cidadao.getGenPessoaFisica().getNome();

        }else {
            logger.info("cidadao em getTipoUsuario() NULL"+". Sessao:"+request.getSession().getId());
        }
        GENPessoaFisica contador = (GENPessoaFisica) request.getSession().getAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO);
        if (contador != null) {
            return contador.getNome();
        }
        GENEmpresa empresa = (GENEmpresa) request.getSession().getAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA);
        if (empresa != null) {
            return empresa.getNomeEmpresa();
        }
        return null;
    }

    @ModelAttribute(value = "tipoUsuario")
    public Integer getTipoUsuario() {
        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
        if (cidadao != null && cidadao.getGenPessoaFisica() != null) {
            logger.info("cidadao em getTipoUsuario() "+cidadao.getId()+". Sessao:"+request.getSession().getId());
            return 1;
        }else {
            logger.info("cidadao em getTipoUsuario() NULL"+". Sessao:"+request.getSession().getId());
        }
        GENPessoaFisica contador = (GENPessoaFisica) request.getSession().getAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO);
        if (contador != null) {
            return 2;
        }
        GENEmpresa empresa = (GENEmpresa) request.getSession().getAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA);
        if (empresa != null) {
            return 3;
        }
        return null;
    }



}
