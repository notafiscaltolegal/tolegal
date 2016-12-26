package gov.goias.service;

import gov.goias.entidades.PessoaParticipante;
import gov.to.entidade.UsuarioToLegal;

public class PessoaParticipanteDTO {

	private PessoaParticipante pessoaParticipante;
	private UsuarioToLegal usuarioToLegal;
	private boolean possuiCredencial;
	private String erro;
	private boolean contador;
	
	public PessoaParticipanteDTO(){
		this.possuiCredencial = Boolean.TRUE;
		this.contador = Boolean.FALSE;
	}
	
	public PessoaParticipante getPessoaParticipante() {
		return pessoaParticipante;
	}
	public void setPessoaParticipante(PessoaParticipante pessoaParticipante) {
		this.pessoaParticipante = pessoaParticipante;
	}
	public boolean isPossuiCredencial() {
		return possuiCredencial;
	}
	public void setPossuiCredencial(boolean possuiCredencial) {
		this.possuiCredencial = possuiCredencial;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}

	public boolean isContador() {
		return contador;
	}

	public void setContador(boolean contador) {
		this.contador = contador;
	}

	public UsuarioToLegal getUsuarioToLegal() {
		return usuarioToLegal;
	}

	public void setUsuarioToLegal(UsuarioToLegal usuarioToLegal) {
		this.usuarioToLegal = usuarioToLegal;
	}
}