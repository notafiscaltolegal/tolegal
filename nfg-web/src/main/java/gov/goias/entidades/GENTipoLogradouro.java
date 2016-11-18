package gov.goias.entidades;

import java.io.Serializable;

public class GENTipoLogradouro implements Serializable{

	private static final long serialVersionUID = 1366178855715961416L;

	private Integer tipoLogradouro;
	
	private String descTipoLogradouro;
	
	private String siglTipoLogradouro;
	
	public GENTipoLogradouro() {
		;
	}

	public Integer getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(Integer tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getDescTipoLogradouro() {
		return descTipoLogradouro;
	}

	public void setDescTipoLogradouro(String descTipoLogradouro) {
		this.descTipoLogradouro = descTipoLogradouro;
	}

	public String getSiglTipoLogradouro() {
		return siglTipoLogradouro;
	}

	public void setSiglTipoLogradouro(String siglTipoLogradouro) {
		this.siglTipoLogradouro = siglTipoLogradouro;
	}
}
