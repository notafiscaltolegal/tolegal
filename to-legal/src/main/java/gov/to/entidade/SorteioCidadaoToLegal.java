package gov.to.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_sort_cidadao")
public class SorteioCidadaoToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_sort_cidadao", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_sort_cidadao", sequenceName = "seq_sort_cidadao",allocationSize=1)
	@Column(name = "id_sort_cidadao")
	private Long id;

	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "total_pontos")
	private Integer totalPontos;
	
	@Column(name = "total_notas")
	private Integer totalNotas;
	
	@Column(name = "total_bilhetes")
	private Integer totalBilhetes;
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id_sorteio")
	private SorteioToLegal sorteioToLegal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Integer getTotalPontos() {
		return totalPontos;
	}

	public void setTotalPontos(Integer totalPontos) {
		this.totalPontos = totalPontos;
	}

	public Integer getTotalNotas() {
		return totalNotas;
	}

	public void setTotalNotas(Integer totalNotas) {
		this.totalNotas = totalNotas;
	}

	public Integer getTotalBilhetes() {
		return totalBilhetes;
	}

	public void setTotalBilhetes(Integer totalBilhetes) {
		this.totalBilhetes = totalBilhetes;
	}

	public SorteioToLegal getSorteioToLegal() {
		return sorteioToLegal;
	}

	public void setSorteioToLegal(SorteioToLegal sorteioToLegal) {
		this.sorteioToLegal = sorteioToLegal;
	}
}