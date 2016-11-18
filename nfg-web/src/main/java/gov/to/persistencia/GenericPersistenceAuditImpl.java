package gov.to.persistencia;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;

@Stateless
public class GenericPersistenceAuditImpl<T extends EntidadeBasicaAudit, ID extends Serializable> 
					extends AbstractModel implements GenericPersistenceAudit<T, ID> {
	
	@EJB
	private ConsultasDaoJpa<T> consultaReposiroty;

	@Override
	public void salvar(T t) {
		
		if (t.getId() == null) {
			em.persist(t);
		} else {
			em.merge(t);
		}
		
		em.flush();
		em.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getById(Class<T> entityName, ID id) {
		return (T) consultaReposiroty.getSession().get(entityName.getName(), id, LockOptions.READ);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getById(Class<T> entityName, ID id, String... camposInitialize) {
		return (T) consultaReposiroty.inicializaCampo(camposInitialize, getById(entityName, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listarTodos(Class<T> clazz) {
		return consultaReposiroty.getSession().createCriteria(clazz).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> listarTodos(Class<T> clazz, String...camposInitialize) {
		return (List<T>) consultaReposiroty.inicializaCampos(consultaReposiroty.getSession().createCriteria(clazz).list(),camposInitialize);
	}

	@Override
	public void excluir(T t) {
		em.remove(merge(t));
	}

	@Override
	public T merge(T t) {
		return em.merge(t);
	}

	@Override
	public boolean emptyTable(Class<T> clazz) {
		
		Criteria criteria = consultaReposiroty.getSession().createCriteria(clazz);
		
		criteria.setMaxResults(1);
		
		boolean tabelaSemRegistro = true;
		
		try{
			
			tabelaSemRegistro = criteria.list().size() == 0;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return tabelaSemRegistro;
	}

	@Override
	public boolean singleLine(Class<T> clazz) {
		
		Criteria criteria = consultaReposiroty.getSession().createCriteria(clazz);
		
		criteria.setMaxResults(2);
		
		boolean tabelaComUmUnico = true;
		
		try{
			
			tabelaComUmUnico = criteria.list().size() == 1;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return tabelaComUmUnico;
	}
}