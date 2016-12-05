package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroReclamacaoToLegal implements Filtro{

	@EntityProperty("usuarioToLegal.id")
	private Long idUsuario;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
}