//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.goias.persistencia.historico;

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
public class HistoricoAspectoNFG {

    public HistoricoAspectoNFG() {
    }

    @Before("@annotation(historicoNFG)")
    @Transactional(
            propagation = Propagation.MANDATORY,
            rollbackFor = {Throwable.class}
    )
    public void inicializarUsuario(JoinPoint joinPoint, HistoricoNFG historicoNFG) {
        String usuarioNFG = "355232";
        Logger.getRootLogger().debug("Inicializando usuário " + usuarioNFG + " para histórico em: " + joinPoint.getSignature().toString());
        HistoricoRepository repository = new HistoricoRepository();
        repository.incluirUsuarioSessao(usuarioNFG);
    }
}
