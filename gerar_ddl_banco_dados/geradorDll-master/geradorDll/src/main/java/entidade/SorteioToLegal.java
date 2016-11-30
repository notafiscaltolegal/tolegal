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
@Table(name = "tb_sorteio")
public class SorteioToLegal {
	
	@Id
	@GeneratedValue(generator = "seq_sorteio", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "seq_sorteio", sequenceName = "seq_sorteio",allocationSize=1)
	@Column(name = "id_sorteio")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_extracao")
	private Date dataExtracaoLoteria;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_sorteio")
	private Date dataSorteio;
	
	@Column(name="num_extracao")
	private Integer numeroExtracao;
	
	@Column(name="num_sorteio")
	private Integer numeroSorteio;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataExtracaoLoteria() {
		return dataExtracaoLoteria;
	}

	public void setDataExtracaoLoteria(Date dataExtracaoLoteria) {
		this.dataExtracaoLoteria = dataExtracaoLoteria;
	}

	public Integer getNumeroExtracao() {
		return numeroExtracao;
	}

	public void setNumeroExtracao(Integer numeroExtracao) {
		this.numeroExtracao = numeroExtracao;
	}

	public Integer getNumeroSorteio() {
		return numeroSorteio;
	}

	public void setNumeroSorteio(Integer numeroSorteio) {
		this.numeroSorteio = numeroSorteio;
	}

	public Date getDataSorteio() {
		return dataSorteio;
	}

	public void setDataSorteio(Date dataSorteio) {
		this.dataSorteio = dataSorteio;
	}

	public boolean isSorteioRealizado() {
		return numeroExtracao != null;
	}
}