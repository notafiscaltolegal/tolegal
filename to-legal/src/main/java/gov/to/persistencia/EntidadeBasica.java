package gov.to.persistencia;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class EntidadeBasica implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract Serializable getId(); 
	
	protected String formataData(Date date, String pattern) {
		
		String dataUltimaAlteracaoFormat;
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		
		dataUltimaAlteracaoFormat = dateFormat.format(date);
		return dataUltimaAlteracaoFormat;
	}
	
}
