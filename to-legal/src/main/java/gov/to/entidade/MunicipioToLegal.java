package gov.to.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import gov.to.dominio.Estado;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "TBCDMN")
public class MunicipioToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@Column(name = "munibge")
	private Long id;

	@Column(name="ufcdg")
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	@Column(name = "munnom")
	private String munNome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getMunNome() {
		return munNome;
	}

	public void setMunNome(String munNome) {
		this.munNome = munNome;
	}
}