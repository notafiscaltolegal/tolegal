package gov.goias.util;

import gov.goias.exceptions.NFGException;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.BindException;

/**
 * Created by lucas-mp on 13/01/15.
 */
@Service
public class Captcha {

    private static final Logger log = Logger.getLogger(Captcha.class);

    @Value("${recaptcha.key}")
    private String keyReCaptcha;

    public static boolean verificarAtivacaoCaptcha(String contadorSessao, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer tentativasSessao = (Integer) session.getAttribute(contadorSessao);
        int numeroDeTentativas = tentativasSessao == null ? 0 : tentativasSessao.intValue();
        session.setAttribute(contadorSessao, ++numeroDeTentativas);
        return numeroDeTentativas > 2 ? true : false;
    }

    public void validarCaptchaSeAtivo(HttpServletRequest request, String challangeField, String responseField) throws BindException {
        if (challangeField != null && responseField != null && challangeField.length() > 0) {
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
            reCaptcha.setPrivateKey(keyReCaptcha);
            ReCaptchaResponse reCaptchaResponse;

//            setProxyForRecaptcha(reCaptcha);

            try {
                log.info("Captcha:::Remote Addr: " + request.getRemoteAddr());
                log.info("key: " + keyReCaptcha);

                reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), challangeField, responseField);
            } catch (Exception e) {
                log.error("Erro ao se comunicar com o ws do Re Captcha: anulando validacao! "+e.getMessage());
                return;
            }
                if (!reCaptchaResponse.isValid()) throw new NFGException("Código verificador incorreto!");
        }
    }

    public static void setProxyForRecaptcha(ReCaptchaImpl reCaptcha){
        String proxy = "localhost";
        int porta = 3128;
        String user = null;
        String pass = null;
        try {
            log.info("Tentando configurar proxy com as configs: " + proxy + " " + porta + " " + user + " " + pass + " ");
            reCaptcha.setHttpLoader(new SimpleProxyHttpLoader(proxy, porta, user, pass));
        } catch (Exception e) {
            log.error("Erro ao acessar via proxy setado :" + e.getMessage());
        }
    }

}
