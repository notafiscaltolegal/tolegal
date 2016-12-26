package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroReclamacaoToLegal implements Filtro{

	

	@EntityProperty("usuarioToLegal.id")
	private Long idUsuario;
	
	
	@EntityProperty("id")
	private Long idReclamacao;
	
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public Long getIdReclamacao() {
		return idReclamacao;
	}

	public void setIdReclamacao(Long idReclamacao) {
		this.idReclamacao = idReclamacao;
	}

	
	
}