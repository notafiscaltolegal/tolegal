package gov.goias.interceptors;

import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import gov.goias.auth.Certificado;
import gov.goias.auth.util.CertificadoExtrator;
import gov.goias.exceptions.NFGException;
import gov.to.controller.BaseController;

/**
 * @author henrique-rh
 * @since 18/07/2014
 */

@Component
public class CidadaoCertificateInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private GENEmpresa genEmpresaRepository;


    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
        controller.setResponse(response);
        if(!request.isSecure() || request.getScheme().equals("http")) {
            response.sendRedirect("https://" + request.getServerName() + request.getRequestURI());
            return false;
        }
        Certificado certificado = getCertificado(request,response,controller);
        if(certificado!=null){
            controller.setCertificadoPf(certificado);
            return true;
        }else {
            throw new NFGException("Certificado n&#225;o encontrado!",new ModelAndView("/cidadao/login"));
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    private Certificado getCertificado(HttpServletRequest request, HttpServletResponse response, BaseController controller) throws Exception {

        X509Certificate[] x509certificate = (X509Certificate[]) request.getAttribute(Certificado.REQUEST_ATTRIBUTE);
        if(x509certificate == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Nenhum certificado válido encontrado!");
            return null;
        }

        Certificado certificado = CertificadoExtrator.extrairDados(x509certificate[0]);
//        certificado.setPfCpf("03794778154"); //TODO retirar mock para commit
//        certificado.setPfCpf("76762076149"); //TODO retirar mock para commit

        if (certificado != null && certificado.getPfCpf() != null && certificado.getPfCpf().length() > 0){
            return certificado;
        }else{
            return null;
        }
    }
}
