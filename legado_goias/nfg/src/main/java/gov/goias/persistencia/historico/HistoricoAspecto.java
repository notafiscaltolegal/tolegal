//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.goias.persistencia.historico;

import gov.goias.interceptors.PortalInterceptor;
import gov.goias.persistencia.HistoricoRepository;
import gov.sefaz.controle.gerenciador.Portal;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author henrique-rh
 * @since 23/09/2014
 */
@Aspect
public class HistoricoAspecto {

    public HistoricoAspecto() {
    }

    @Before("@annotation(historico)")
    @Transactional(
            propagation = Propagation.MANDATORY,
            rollbackFor = {Throwable.class}
    )
    public void inicializarUsuario(JoinPoint joinPoint, Historico historico) {
        Portal portal = PortalInterceptor.getUsuarioPortal();
        if(portal == null) {
            throw new IllegalArgumentException("Usuário do portal não encontrado! Deve ser inicializado em PortalInterceptor.usuarioPortal");
        }

        String usuarioPortal = portal.getUsuario();

        Logger.getRootLogger().debug("Inicializando usuário " + usuarioPortal + " para histórico em: " + joinPoint.getSignature().toString());
        HistoricoRepository repository = new HistoricoRepository();
        repository.incluirUsuarioSessao(usuarioPortal);
    }
}
