package gov.to.entidade;

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
import javax.persistence.Transient;

import gov.to.dominio.SituacaoSorteio;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_sorteio")
public class SorteioToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_sorteio", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_sorteio", sequenceName = "seq_sorteio",allocationSize=1)
	@Column(name = "id_sorteio")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_extracao")
	private Date dataExtracaoLoteria;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_sorteio")
	private Date dataSorteio;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INICIO_SORTEIO")
	private Date dataInicioSorteio;
	
	@Column(name="num_extracao")
	private Integer numeroExtracao;
	
	@Column(name="num_extracao2")
	private Integer numeroExtracao2;

	@Column(name="num_extracao3")
	private Integer numeroExtracao3;
	
	@Column(name="num_extracao4")
	private Integer numeroExtracao4;
	
	@Column(name="num_extracao5")
	private Integer numeroExtracao5;
	
	@Column(name="max_bilhete")
	private Integer maxBilhete;
	
	@Column(name="num_sorteio")
	private Integer numeroSorteio;
	
	@Column(name="qnt_ganhadores")
	private Integer qntGanhadores;
	
	@Column(name="situacao")
	@Enumerated(EnumType.STRING)
	private SituacaoSorteio situacao;
	
	public SorteioToLegal() {
		situacao = SituacaoSorteio.INATIVO;
	}
	
	@Transient
	public boolean getAtivo() {
		
		return SituacaoSorteio.ATIVO.equals(situacao);
	}
	
	@Transient
	public boolean getInativo() {
		
		return SituacaoSorteio.INATIVO.equals(situacao);
	}
	
	@Transient
	public boolean getSorteado() {
		
		return SituacaoSorteio.SORTEADO.equals(situacao);
	}
	
	@Transient
	public boolean getAguardandoSorteio() {
		
		return SituacaoSorteio.AGUARDANDO_SORTEIO.equals(situacao);
	}
	
	@Transient
	public String getDataExtracaoFormat() {
		
		String dataFormat = null;
		
		if (this.getDataExtracaoLoteria() != null){
			
			dataFormat = formataData(this.getDataExtracaoLoteria(), "dd/MM/yyyy");
		}
		
		return dataFormat;
	}
	
	@Transient
	public String getDataSorteioFormat() {
		
		String dataFormat = null;
		
		if (this.getDataSorteio() != null){
			
			dataFormat = formataData(this.getDataSorteio(), "dd/MM/yyyy");
		}
		
		return dataFormat;
	}
	
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

	public SituacaoSorteio getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoSorteio situacao) {
		this.situacao = situacao;
	}

	public Integer getNumeroExtracao2() {
		return numeroExtracao2;
	}

	public void setNumeroExtracao2(Integer numeroExtracao2) {
		this.numeroExtracao2 = numeroExtracao2;
	}

	public Integer getNumeroExtracao3() {
		return numeroExtracao3;
	}

	public void setNumeroExtracao3(Integer numeroExtracao3) {
		this.numeroExtracao3 = numeroExtracao3;
	}

	public Integer getNumeroExtracao4() {
		return numeroExtracao4;
	}

	public void setNumeroExtracao4(Integer numeroExtracao4) {
		this.numeroExtracao4 = numeroExtracao4;
	}

	public Integer getMaxBilhete() {
		return maxBilhete;
	}

	public void setMaxBilhete(Integer maxBilhete) {
		this.maxBilhete = maxBilhete;
	}

	public Integer getQntGanhadores() {
		return qntGanhadores;
	}

	public void setQntGanhadores(Integer qntGanhadores) {
		this.qntGanhadores = qntGanhadores;
	}

	public Integer getNumeroExtracao5() {
		return numeroExtracao5;
	}

	public void setNumeroExtracao5(Integer numeroExtracao5) {
		this.numeroExtracao5 = numeroExtracao5;
	}

	public Date getDataInicioSorteio() {
		return dataInicioSorteio;
	}

	public void setDataInicioSorteio(Date dataInicioSorteio) {
		this.dataInicioSorteio = dataInicioSorteio;
	}
}