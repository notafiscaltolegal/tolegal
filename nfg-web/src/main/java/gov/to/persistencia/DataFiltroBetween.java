package gov.to.persistencia;

import java.io.Serializable;
import java.util.Date;

public class DataFiltroBetween implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404594753656886107L;

	private Date dataInicio;
	
	private Date dataFim;

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
}