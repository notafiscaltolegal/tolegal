package gov.to.persistencia;

import java.io.Serializable;
import java.util.List;

public interface GenericPersistenceAudit<T extends EntidadeBasicaAudit, ID extends Serializable> {

	void salvar(T t) ;

	T getById(Class<T> clazz,ID id);

	List<T> listarTodos(Class<T> clazz);

	T getById(Class<T> clazz, ID id, String... camposInitialize);

	List<T> listarTodos(Class<T> class1, String... camposInitialize);

	void excluir(T id);
	
	T merge(T t);

	boolean emptyTable(Class<T> t);

	boolean singleLine(Class<T> class1);
}
