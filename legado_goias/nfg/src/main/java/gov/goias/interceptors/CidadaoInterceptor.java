package gov.goias.interceptors;

import gov.goias.controllers.BaseController;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by diogo-rs on 7/21/2014.
 */
@Component
public class CidadaoInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = Logger.getLogger(CidadaoInterceptor.class);

    public static Boolean emManutencao = false;

    @Autowired
    private PessoaParticipante pessoaParticipanteRepository;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        logger.info("Novo BaseController criado no acesso da url"+request.getRequestURI()+". Sessao:"+request.getSession().getId());
        BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
        controller.setResponse(response);
        filtrosUrlCidadao(request);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    private void filtrosUrlCidadao(HttpServletRequest request) throws Exception {

        if (emManutencao ) {
            throw new NFGException("", new ModelAndView("/cidadao/manutencao"));
        }

        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
//
        if (cidadao != null) {
            Boolean adminLogado = BaseController.CPFS_ADMINS.contains(cidadao.getGenPessoaFisica().getCpf());
            if(adminLogado && !StringUtils.isEmpty(request.getParameter("c"))){
                try{
                    logger.info("tentarei passar o seguinte cpf para cidadao_logado:"+request.getParameter("c"));
                    cidadao = pessoaParticipanteRepository.findByCPF(request.getParameter("c"));
                    logger.info("deu certo?"+(cidadao!=null));
                    request.getSession().setAttribute(BaseController.SESSION_CIDADAO_LOGADO, cidadao);
                    logger.info("validando:" + request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO));
                } catch(Exception e){
                    throw new NFGException("Usuário não logado", new ModelAndView("/cidadao/login"));
                }
            }
        }else{

            boolean urlSemFiltro = request.getRequestURI().contains("login");
            urlSemFiltro |= request.getRequestURI().contains("efetuarlogin");
            urlSemFiltro |= request.getRequestURI().contains("esqueciSenha");
            urlSemFiltro |= request.getRequestURI().contains("validardadoscidadao");
            urlSemFiltro |= request.getRequestURI().contains("cadastrar");
            urlSemFiltro |= request.getRequestURI().contains("efetuarcadastro");
            urlSemFiltro |= request.getRequestURI().contains("recuperarsenha");
            urlSemFiltro |= request.getRequestURI().contains("verificaSenha");
            urlSemFiltro |= request.getRequestURI().contains("gravaNovaSenha");
            urlSemFiltro |= request.getRequestURI().contains("certificado");
            urlSemFiltro |= request.getRequestURI().contains("carregaCidadesPorUf");
            urlSemFiltro |= request.getRequestURI().contains("verificaEndereco");
            urlSemFiltro |= request.getRequestURI().contains("verificaPreCadastro");
            urlSemFiltro |= request.getRequestURI().contains("notificaViaEmail");
            urlSemFiltro |= request.getRequestURI().contains("conclusaoDadosPerfil");
            urlSemFiltro |= request.getRequestURI().contains("gravaConclusaoPerfil");
            urlSemFiltro |= request.getRequestURI().contains("redirecionarHome");
            urlSemFiltro |= request.getRequestURI().contains("telaCidadao");

            urlSemFiltro |= request.getRequestURI().contains("inicioSite");

            if (request.getRequestURI().contains("inicioSite")){
                throw new NFGException("Usuário não logado.", "/cidadao/telaCidadao");
            }

            if (!urlSemFiltro){
                throw new NFGException("Usuário não logado.",  "/cidadao/login");
            }
        }
    }

}
