package gov.goias.entidades;

import gov.goias.persistencia.TipoLogradouroRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="GEN_TIPO_LOGRADOURO")
@Repository
@Cacheable(true)
public class GENTipoLogradouro  extends TipoLogradouroRepository implements Serializable{

	private static final long serialVersionUID = 1366178855715961416L;

	@Id
	@Column(name="TIPO_LOGRADOURO")
	private Integer tipoLogradouro;
	
	@Column(name="DESC_TIPO_LOGRADOURO")
	private String descTipoLogradouro;
	
	@Column(name="SIGL_TIPO_LOGRADOURO")
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
