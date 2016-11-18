package gov.to.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import gov.to.persistencia.EntidadeBasica;
import gov.to.persistencia.GenericPersistence;

@Stateless
public class GenericServiceImpl<T extends EntidadeBasica, ID extends Serializable> implements GenericService<T, ID>{
	
	@EJB
	private GenericPersistence<T, ID> reposiroty;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(T t) {
		reposiroty.salvar(t);
	}

	@Override
	public T getById(Class<T> clazz, ID id) {
		
		if (id == null){
			return null;
		}
		
		return reposiroty.getById(clazz, id);
	}

	@Override
	public List<T> listarTodos(Class<T> clazz) {
		return reposiroty.listarTodos(clazz);
	}
	
	@Override
	public List<T> listarTodos(Class<T> clazz, String... camposInitialize) {
		return reposiroty.listarTodos(clazz, camposInitialize);
	}

	@Override
	public T getById(Class<T> clazz, ID id, String... camposInitialize) {
		return reposiroty.getById(clazz, id, camposInitialize);
	}

	@Override
	public void excluir(T t) {
		reposiroty.excluir(t);
	}

	@Override
	public T merge(T t) {
		return reposiroty.merge(t);
	}

	@Override
	public boolean emptyTable(Class<T> t) {
		return reposiroty.emptyTable(t);
	}

	@Override
	public boolean singleLine(Class<T> class1) {
		return reposiroty.singleLine(class1);
	}
}