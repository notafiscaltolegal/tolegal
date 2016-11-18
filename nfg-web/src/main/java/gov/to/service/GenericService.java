package gov.to.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import gov.to.persistencia.EntidadeBasica;

@Local
public interface GenericService<T extends EntidadeBasica, ID extends Serializable> {

	void salvar(T t) ;

	T getById(Class<T> clazz, ID id);
	
	List<T> listarTodos(Class<T> clazz);
	
	List<T> listarTodos(Class<T> clazz, String... camposInitialize);

	T getById(Class<T> clazz, ID id, String... camposInitialize);

	void excluir(T id);
	
	T merge(T id);

	boolean emptyTable(Class<T> t);

	boolean singleLine(Class<T> class1);

}
