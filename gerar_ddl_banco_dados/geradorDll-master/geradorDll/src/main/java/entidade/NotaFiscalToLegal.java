package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_nota_legal")
public class NotaFiscalToLegal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@Column(name = "id_nota_legal")
	private Long id;

	@Column(name = "num_nota")
	private String numNota;
	
	@Column(name = "nome_fantasia")
	private String nomeFantasia;
	
	@Column(name = "razao_social")
	private String razaoSocial;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao")
	private Date dataEmissao;
	
	@Column(name = "valor")
	private Double valor;
	
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

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
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
}