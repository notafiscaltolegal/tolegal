package gov.goias.interceptors;

import gov.goias.controllers.BaseController;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.exceptions.NFGException;
import gov.goias.services.ContadorLoginService;
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
public class ContadorInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = Logger.getLogger(ContadorInterceptor.class);

    @Autowired
    private GENPessoaFisica genPessoaFisicaRepository;

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

        GENPessoaFisica contador = (GENPessoaFisica) request.getSession().getAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO);
        Boolean adminLogado = (Boolean)request.getSession().getAttribute(BaseController.SESSION_ADMIN_LOGADO);
        if(adminLogado != null && adminLogado && !StringUtils.isEmpty(request.getParameter("c"))){
            try{
                contador = genPessoaFisicaRepository.findByCpf(request.getParameter("c"));
                request.getSession().setAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO, contador);
            } catch(Exception e){
                logger.info(e.getMessage(), e);
                throw new NFGException("Contador não encontrado", "/contador/contribuintes/cadastro");
            }
        }

        if (contador != null) {
            return contador;
        }


        throw new NFGException("Usuário não logado", "/contador");
    }
}
