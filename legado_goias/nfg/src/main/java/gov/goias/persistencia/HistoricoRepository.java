package gov.goias.persistencia;

import javax.persistence.Query;

/**
 * @author henrique-rh
 * @since 23/09/2014
 */
public class HistoricoRepository extends GenericRepository<Integer, String> {

    public void incluirUsuarioSessao(String usuario) {
        String sql = "{call GEN.PKGGEN_INICIALIZA_USER.SPGEN_INICIALIZA_USER(:usuario)}";
        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("NFG_HISTORICO");
        query.setParameter("usuario", usuario);
        query.executeUpdate();
    }
}
