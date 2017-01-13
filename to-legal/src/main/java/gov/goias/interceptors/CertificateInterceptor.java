package gov.goias.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import gov.goias.entidades.GENEmpresa;
import gov.goias.exceptions.NFGException;
import gov.goias.service.EmpresaService;
import gov.to.controller.BaseController;

/**
 * @author henrique-rh
 * @since 18/07/2014
 */

@Component
public class CertificateInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private EmpresaService empresaService;

    public static final String SESSION_EMPRESA_LOGADA = "empresaLogadaSession";

    public static Boolean emManutencao = false;

    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        if (emManutencao) {
            throw new NFGException("", new ModelAndView("/cidadao/manutencao"));
        }
        BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
        controller.setResponse(response);
        
//        if (request.getServerName().equals("localhost") || request.getServerName().equals("127.0.0.1")){
//        	response.sendRedirect("https://" + request.getServerName()+":"+8443+ request.getRequestURI());
//            return false;
//        }
//        
//        if(!request.isSecure() || request.getScheme().equals("http")) {
//            response.sendRedirect("https://" + request.getServerName() + request.getRequestURI());
//            return false;
//        }
        return isContribuinteLogado(request, response, controller);
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        //System.out.println("postHandle");
    }

    private boolean isContribuinteLogado(HttpServletRequest request, HttpServletResponse response, BaseController controller) throws Exception {

//        GENEmpresa empresa = (GENEmpresa) request.getSession().getAttribute(SESSION_EMPRESA_LOGADA);
//        Boolean isAdmin = (Boolean) request.getSession().getAttribute(BaseController.SESSION_ADMIN_LOGADO);
//        isAdmin = isAdmin == null ? false : isAdmin;
//        if(isAdmin && empresa != null && (request.getParameter("c") == null || request.getParameter("c").isEmpty())) {
//            controller.setEmpresaLogada(empresa);
//            return true;
//        }
//
//        X509Certificate[] x509certificate = (X509Certificate[]) request.getAttribute(Certificado.REQUEST_ATTRIBUTE);
//        if(x509certificate == null) {
//            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Nenhum certificado válido encontrado!");
//            return false;
//        }
//
//        Certificado certificado = CertificadoExtrator.extrairDados(x509certificate[0]);
//
//        if(empresa != null && !isAdmin && certificado.getPjCnpj().substring(0,8).equals(empresa.getNumeroCnpjBase())) {
//            controller.setEmpresaLogada(empresa);
//            return true;
//        }
//
//        if(certificado.getPjCnpj() != null) {
//            try{
//                empresa = empresaService.findByCnpj(certificado.getPjCnpj());
//            } catch (NoResultException ex) {
//                String msg = "Nenhum contribuinte encontrado para o CNPJ: " + certificado.getPjCnpj();
//                response.sendError(HttpStatus.UNAUTHORIZED.value(), msg);
//                return false;
//            }
//        } else if(BaseController.CPFS_ADMINS.contains(certificado.getPfCpf()) && request.getParameter("c") != null) {
//            String cnpj = request.getParameter("c");
//            request.getSession().setAttribute(BaseController.SESSION_ADMIN_LOGADO, true);
//            try {
//                empresa = empresaService.findByCnpj(cnpj);
//            } catch (NoResultException ex) {
//                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Nenhum contribuinte encontrado para o certificado informado");
//                return false;
//            }
//        } else {
//            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Esse certificado n&#225;o possui um CNPJ");
//            return false;
//        }
    	
//    	GENEmpresa empresa = empresaService.findByCnpj("12345678911111");
//        request.getSession().setAttribute(SESSION_EMPRESA_LOGADA, empresa);
//        controller.setEmpresaLogada(empresa);
        return true;
    }
}
