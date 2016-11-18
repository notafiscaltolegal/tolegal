package gov.to.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;



/**
 * The Class HibernateUtil.
 */
public final class HibernateUtil {

	
	/**
	 * Instantiates a new hibernate util.
	 */
	private HibernateUtil(){
	}
	
	/**
	 * Gets the objeto inicializado.
	 *
	 * @param proxy the proxy
	 * @return the objeto inicializado
	 */
	public static Object getObjetoInicializado(Object proxy){
		
		if (proxy instanceof  HibernateProxy){
			return (( HibernateProxy ) proxy).getHibernateLazyInitializer().getImplementation();
		}else{
			Hibernate.initialize(proxy);
		}
		
		return proxy;
	}
	
	public static <E> List<E> persistBagToArrayList(Collection<E> collection){
		
	    List<E> list;
	    
		if (collection instanceof List) {
			list = (List<E>) collection;
		} else {
			list = new ArrayList<E>(collection);
		}
		
		return list;
	}
}