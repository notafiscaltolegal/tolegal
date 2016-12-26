package gov.to.filtro;

import gov.to.dominio.SituacaoUsuario;
import gov.to.persistencia.EntityProperty;

public class FiltroUsuarioToLegal implements Filtro{

	@EntityProperty("pessoaFisica.cpf")
	private String cpf;
	
	@EntityProperty("situacao")
	private SituacaoUsuario situacao;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public SituacaoUsuario getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoUsuario situacao) {
		this.situacao = situacao;
	}
}