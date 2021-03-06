package gov.to.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import gov.to.dominio.SituacaoBilhete;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_bilhete")
public class BilheteToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_bilhete", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_bilhete", sequenceName = "seq_bilhete",allocationSize=1)
	@Column(name = "id_bilhete")
	private Long id;
	
	@Column(name="st_bilhete")
	@Enumerated(EnumType.STRING)
	private SituacaoBilhete stBilhete;
	
	@Column(name="num_seq_bilhete")
	private Integer numeroSeqBilhete;
	
	@ManyToOne
	@JoinColumn(name="ID_SORTEIO", referencedColumnName="id_sorteio")
	private SorteioToLegal sorteioToLegal;
	
	@ManyToOne
	@JoinColumn(name="ID_PONTUACAO", referencedColumnName="id_pont_to_legl")
	private PontuacaoToLegal pontuacaoToLegal;
	
	@ManyToOne
	@JoinColumn(name="ID_PONTUACAO_BONUS", referencedColumnName="id_pont_bonus")
	private PontuacaoBonusToLegal pontuacaoBonusToLegal;
	
	@Column(name="cpf")
	private String cpf;
	
	@Column(name="premiado")
	private Boolean premiado;
	
	public BilheteToLegal(){
		this.premiado = Boolean.FALSE;
	}
	
	public String getPremiadoFormat(){
		
		if (this.getPremiado() == null || this.getPremiado().equals(Boolean.FALSE)){
			return "N";
		}
		
		return "S";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SituacaoBilhete getStBilhete() {
		return stBilhete;
	}

	public void setStBilhete(SituacaoBilhete stBilhete) {
		this.stBilhete = stBilhete;
	}

	public Integer getNumeroSeqBilhete() {
		return numeroSeqBilhete;
	}

	public void setNumeroSeqBilhete(Integer numeroSeqBilhete) {
		this.numeroSeqBilhete = numeroSeqBilhete;
	}

	public SorteioToLegal getSorteioToLegal() {
		return sorteioToLegal;
	}

	public void setSorteioToLegal(SorteioToLegal sorteioToLegal) {
		this.sorteioToLegal = sorteioToLegal;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Boolean getPremiado() {
		return premiado;
	}

	public void setPremiado(Boolean premiado) {
		this.premiado = premiado;
	}

	public PontuacaoToLegal getPontuacaoToLegal() {
		return pontuacaoToLegal;
	}

	public void setPontuacaoToLegal(PontuacaoToLegal pontuacaoToLegal) {
		this.pontuacaoToLegal = pontuacaoToLegal;
	}

	public PontuacaoBonusToLegal getPontuacaoBonusToLegal() {
		return pontuacaoBonusToLegal;
	}

	public void setPontuacaoBonusToLegal(PontuacaoBonusToLegal pontuacaoBonusToLegal) {
		this.pontuacaoBonusToLegal = pontuacaoBonusToLegal;
	}
}