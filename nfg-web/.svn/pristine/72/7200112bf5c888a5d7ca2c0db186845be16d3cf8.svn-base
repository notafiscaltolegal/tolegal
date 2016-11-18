package gov.goias.persistencia;

import gov.goias.entidades.GENCredencialUsuario;
import gov.goias.util.Encrypter;
import sun.security.provider.SHA;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author henrique-rh
 * @since 15/07/2015
 */
public abstract class GENCredencialUsuarioRepository extends GenericRepository<Long, GENCredencialUsuario> {

    public GENCredencialUsuario findByMatriculaAndPassword(String matricula, String password) {
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root credencial = criteriaQuery.from(GENCredencialUsuario.class);
        criteriaQuery.where(criteriaBuilder.and(
                        criteriaBuilder.equal(credencial.get("nomeCredencialPortal"), matricula),
                        criteriaBuilder.equal(credencial.get("infoSenha"), Encrypter.encryptSHA512(password).toUpperCase())
                ));
        TypedQuery query = entityManager().createQuery(criteriaQuery);
        return (GENCredencialUsuario) query.getSingleResult();
    }

}