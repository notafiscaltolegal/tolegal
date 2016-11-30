package entidade;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_pont_bonus")
public class PontuacaoBonusToLegal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_pont_bonus", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "seq_pont_bonus", sequenceName = "seq_pont_bonus",allocationSize=1)
	@Column(name = "id_pont_bonus")
	private Long id;

	@Column(name = "qnt_ponto")
	private Integer qntPonto;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "cpf")
	private String cpf;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_pontuacao")
	private Date dataPontuacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private SituacaoBonusPontuacao situacaoBonusPontuacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_limite_validade")
	private Date dataLimiteBonus;
	
	public PontuacaoBonusToLegal(){}
	
	public String getDataPontuacaoFormat(){
		
		if (this.getDataPontuacao() != null){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.format(getDataPontuacao());
		}
		
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQntPonto() {
		return qntPonto;
	}

	public void setQntPonto(Integer qntPonto) {
		this.qntPonto = qntPonto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SituacaoBonusPontuacao getSituacaoBonusPontuacao() {
		return situacaoBonusPontuacao;
	}

	public void setSituacaoBonusPontuacao(SituacaoBonusPontuacao situacaoBonusPontuacao) {
		this.situacaoBonusPontuacao = situacaoBonusPontuacao;
	}

	public Date getDataLimiteBonus() {
		return dataLimiteBonus;
	}

	public void setDataLimiteBonus(Date dataLimiteBonus) {
		this.dataLimiteBonus = dataLimiteBonus;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataPontuacao() {
		return dataPontuacao;
	}

	public void setDataPontuacao(Date dataPontuacao) {
		this.dataPontuacao = dataPontuacao;
	}
}