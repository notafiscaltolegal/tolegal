package entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tb_ganhador_sorteio")
public class GanhadorSorteioToLegal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_ganhador_sorteio", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_ganhador_sorteio", sequenceName = "seq_ganhador_sorteio",allocationSize=1)
	@Column(name = "id_ganhador")
	private Long id;
	
	@Column(name="num_sorteio")
	private Integer numeroSorteio;
	
	@Column(name="num_bilhete")
	private Integer numeroBilhete;
	
	@Column(name="cpf")
	private String cpf;

	@Column(name="nome")
	private String nome;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroSorteio() {
		return numeroSorteio;
	}

	public void setNumeroSorteio(Integer numeroSorteio) {
		this.numeroSorteio = numeroSorteio;
	}

	public Integer getNumeroBilhete() {
		return numeroBilhete;
	}

	public void setNumeroBilhete(Integer numeroBilhete) {
		this.numeroBilhete = numeroBilhete;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}