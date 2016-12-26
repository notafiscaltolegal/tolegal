package gov.to.filtro;

import gov.to.dominio.Estado;
import gov.to.persistencia.EntityProperty;

public class FiltroMunicipioToLegal implements Filtro{

	@EntityProperty("estado")
	private Estado codgUF;

	public Estado getCodgUF() {
		return codgUF;
	}

	public void setCodgUF(Estado codgUF) {
		this.codgUF = codgUF;
	}
}
