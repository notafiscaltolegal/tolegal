package gov.goias.entidades;

import gov.goias.persistencia.UfRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="GEN_UF")
@Repository
@Cacheable(true)
public class GENUf extends UfRepository implements Serializable{
	
	private static final long serialVersionUID = 655593573661615913L;

	@Id
	@Column(name="CODG_UF")
	private String codgUf;
	
	@Column(name="NOME_UF")
	private String nomeUf;
	
	@Column(name="CODG_IBGE")
	private Integer codgIbge;

	public GENUf() {
		;
	}
	
	public String getCodgUf() {
		return codgUf;
	}

	public void setCodgUf(String codgUf) {
		this.codgUf = codgUf;
	}

	public String getNomeUf() {
		return nomeUf;
	}

	public void setNomeUf(String nomeUf) {
		this.nomeUf = nomeUf;
	}

	public Integer getCodgIbge() {
		return codgIbge;
	}

	public void setCodgIbge(Integer codgIbge) {
		this.codgIbge = codgIbge;
	}
	
}
