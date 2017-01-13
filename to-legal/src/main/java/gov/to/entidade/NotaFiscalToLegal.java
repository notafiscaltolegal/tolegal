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

import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_nota_legal")
public class NotaFiscalToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_nota", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_nota", sequenceName = "seq_nota",allocationSize=1)
	@Column(name = "id_nota_legal")
	private Long id;
	
	@Column(name = "chave_acesso")
	private String chaveAcesso;

	@Column(name = "num_nota")
	private String numNota;
	
	@Column(name = "razao_social")
	private String razaoSocial;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao")
	private Date dataEmissao;
	
	@Column(name = "valor")
	private Double valor;
	
	@Column(name = "VALOR_PROD_CFOP")
	private Double valorProdCfop;
	
	@Column(name = "cpf_cidadao")
	private String cpf;
	
	@Column(name = "cnpj_emitente")
	private String cnpj;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "situacao_pontuacao")
	private SituacaoPontuacaoNota situacaoPontuacaoNota;
	
	public NotaFiscalToLegal (){
		this.situacaoPontuacaoNota = SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumNota() {
		return numNota;
	}

	public void setNumNota(String numNota) {
		this.numNota = numNota;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public SituacaoPontuacaoNota getSituacaoPontuacaoNota() {
		return situacaoPontuacaoNota;
	}

	public void setSituacaoPontuacaoNota(SituacaoPontuacaoNota situacaoPontuacaoNota) {
		this.situacaoPontuacaoNota = situacaoPontuacaoNota;
	}

	public Double getValorProdCfop() {
		return valorProdCfop;
	}

	public void setValorProdCfop(Double valorProdCfop) {
		this.valorProdCfop = valorProdCfop;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
}