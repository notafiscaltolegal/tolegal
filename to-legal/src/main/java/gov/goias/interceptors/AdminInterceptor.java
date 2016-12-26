package gov.goias.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
* @author henrique-rh
* @since 27/11/2014.
*/
@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {
//    private static ThreadLocal<GENCredencialUsuario> adminLogado = new ThreadLocal<GENCredencialUsuario>();

//    private static void setAdminLogado(GENCredencialUsuario admin) {
//        adminLogado.set(admin);
//    }

//    public static GENCredencialUsuario getAdminLogado() {
//        return adminLogado.get();
//    } 

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        GENCredencialUsuario usuarioLogado = (GENCredencialUsuario) request.getSession().getAttribute(BaseController.SESSION_ADMIN_LOGADO);
//        if (usuarioLogado == null) {
//            if (request.getRequestURI().equals("/to-legal/admin/")) {
//                throw new NFGException(null, "/admin/login");
//            }
//            throw new NFGException("Usuário n&#225;o logado", "/admin/login");
//        }
//        BaseController controller = (BaseController) ((HandlerMethod) handler).getBean();
//        controller.setResponse(response);
//        controller.setAdminLogado(usuarioLogado);
//        setAdminLogado(usuarioLogado);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        GENCredencialUsuario usuarioLogado = (GENCredencialUsuario) request.getSession().getAttribute(BaseController.SESSION_ADMIN_LOGADO);
//        if (usuarioLogado != null && modelAndView != null) {
//            modelAndView.addObject("usuario", usuarioLogado.getPessoaFisica().getNome());
//            modelAndView.addObject("isAdmin", Arrays.asList("00011590114", "03794778154", "89464796120").contains(usuarioLogado.getCpf()));
//        }
    }
}