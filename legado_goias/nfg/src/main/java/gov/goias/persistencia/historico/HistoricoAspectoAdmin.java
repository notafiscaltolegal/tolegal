//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.goias.persistencia.historico;

import gov.goias.entidades.GENCredencialUsuario;
import gov.goias.interceptors.AdminInterceptor;
import gov.goias.persistencia.HistoricoRepository;
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
public class HistoricoAspectoAdmin {

    public HistoricoAspectoAdmin() {
    }

    @Before("@annotation(historicoAdmin)")
    @Transactional(
            propagation = Propagation.MANDATORY,
            rollbackFor = {Throwable.class}
    )
    public void inicializarUsuario(JoinPoint joinPoint, HistoricoAdmin historicoAdmin) {
        GENCredencialUsuario usuario = AdminInterceptor.getAdminLogado();
        if(usuario == null) {
            throw new IllegalArgumentException("Usuário admin não encontrado! Deve ser inicializado em AdminInterceptor.adminLogado");
        }

        Logger.getRootLogger().debug("Inicializando usuário " + usuario.getNomeCredencialPortal() + " para histórico em: " + joinPoint.getSignature().toString());
        HistoricoRepository repository = new HistoricoRepository();
        repository.incluirUsuarioSessao(usuario.getNomeCredencialPortal());
    }
}
