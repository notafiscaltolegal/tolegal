package gov.goias.services;

import com.thoughtworks.xstream.XStream;
import gov.goias.controllers.BaseController;
import gov.goias.dtos.DTOSessaoUsuarioAsp;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.util.TextUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by diogo-rs on 7/31/2014.
 */
@Service
public class ContadorLoginService {

    private static Logger logger = Logger.getLogger(ContadorLoginService.class);
    public static final String SESSION_CONTADOR_LOGADO = "contadorLogadoSession";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GENPessoaFisica genPessoaFisica;

    //http://10.6.0.46/pat/service/auth/login/login-10-digitos-completa-com-zero-a-esquerda/senha-uppercase-10-digitos-completa-com-espaco-a-direita/FF/8/10.6.0.0
    public DTOSessaoUsuarioAsp loginPortalContabilista(String login, String senha) {
        DTOSessaoUsuarioAsp usuario = null;

        try {
            senha = senha.substring(0,10);
            senha = TextUtils.encodeURIComponent(senha);
            login = TextUtils.encodeURIComponent(login);
            login = String.format("%010d", Integer.valueOf(login));
            senha = String.format("%-10s", senha.toUpperCase());

            HttpClient httpClient = new DefaultHttpClient();
            final String urlLogin = "http://10.6.0.46/pat/service/auth/login/" + login + "/" + senha + "/FF/8/10.6.0.0";
            HttpGet httpMethod = new HttpGet(urlLogin);

            httpMethod.addHeader("Accept", "application/xml");
            HttpResponse response = httpClient.execute(httpMethod);
            HttpEntity entity = response.getEntity();

            String xmlResponse = EntityUtils.toString(entity);
            logger.debug("Resposta login contador: " + xmlResponse);
            httpMethod.releaseConnection();

            XStream xStream = new XStream();
            xStream.processAnnotations(DTOSessaoUsuarioAsp.class);
            xStream.ignoreUnknownElements();
            usuario = (DTOSessaoUsuarioAsp) xStream.fromXML(xmlResponse);

            request.getSession().setAttribute(BaseController.SESSION_ADMIN_LOGADO, usuarioEhAdmin(usuario));

            GENPessoaFisica contador = genPessoaFisica.findByCpf(usuario.getUsuario().getFuncionarioXML().getNumrCpf());//(637050l)sem contribuintes;//genPessoaFisica.findByNumeroBase(684449l);
            request.getSession().setAttribute(SESSION_CONTADOR_LOGADO, contador);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw new RuntimeException("Usuário e senha inválidos");
        }

        return usuario;
    }

    public Boolean usuarioEhAdmin(DTOSessaoUsuarioAsp usuario) {
        return BaseController.CPFS_ADMINS.contains(usuario.getUsuario().getFuncionarioXML().getNumrCpf());
    }
}
