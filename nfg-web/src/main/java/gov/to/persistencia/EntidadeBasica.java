package gov.to.persistencia;

import java.io.Serializable;

public abstract class EntidadeBasica implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract Serializable getId(); 
	
}
