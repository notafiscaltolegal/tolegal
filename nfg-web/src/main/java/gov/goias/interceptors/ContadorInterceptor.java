package gov.goias.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import gov.goias.controllers.BaseController;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.exceptions.NFGException;
import gov.goias.service.PessoaFisicaService;

/**
 * Created by diogo-rs on 7/21/2014.
 */
@Component
public class ContadorInterceptor extends HandlerInterceptorAdapter {
    public static final String SESSION_CONTADOR_LOGADO = "contador_logado";
	private static Logger logger = Logger.getLogger(ContadorInterceptor.class);

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    public static Boolean emManutencao = false;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (emManutencao) {
            throw new NFGException("", new ModelAndView("/cidadao/manutencao"));
        }
        BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
        controller.setResponse(response);
        controller.setContadorLogado(getContadorLogado(request));
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {}

    private GENPessoaFisica getContadorLogado(HttpServletRequest request) throws Exception {

        GENPessoaFisica contador = (GENPessoaFisica) request.getSession().getAttribute(SESSION_CONTADOR_LOGADO);
        Boolean adminLogado = (Boolean)request.getSession().getAttribute(BaseController.SESSION_ADMIN_LOGADO);
        if(adminLogado != null && adminLogado && !StringUtils.isEmpty(request.getParameter("c"))){
            try{
                contador = pessoaFisicaService.findByCpf(request.getParameter("c"));
                request.getSession().setAttribute(SESSION_CONTADOR_LOGADO, contador);
            } catch(Exception e){
                logger.info(e.getMessage(), e);
                throw new NFGException("Contador n&#225;o encontrado", "/contador/contribuintes/cadastro");
            }
        }

        if (contador != null) {
            return contador;
        }


        throw new NFGException("Usuário n&#225;o logado", "/contador");
    }
}
