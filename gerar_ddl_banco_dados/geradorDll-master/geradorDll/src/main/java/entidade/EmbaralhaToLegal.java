package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_embaralhamento")
public class EmbaralhaToLegal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_embaralhamento", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_embaralhamento", sequenceName = "seq_embaralhamento",allocationSize=1)
	@Column(name = "id_embaralhamento")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_embaralhamento")
	private Date dataEmbaralhamento;
	
	@Column(name="posicao")
	private Integer posicao;
	
	@Column(name="num_sorteio")
	private Integer numeroSorteio;
	
	@Column(name="num_bilhete")
	private Integer numeroBilhete;
	
	@Column(name="cpf")
	private String cpf;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataEmbaralhamento() {
		return dataEmbaralhamento;
	}

	public void setDataEmbaralhamento(Date dataEmbaralhamento) {
		this.dataEmbaralhamento = dataEmbaralhamento;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
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
}