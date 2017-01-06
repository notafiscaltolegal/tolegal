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

import gov.to.dominio.SerieEnum;
import gov.to.dominio.TipoDocumentoEnum;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_nota_empresa")
public class NotaEmpresaToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_nota_empresa", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_nota_empresa", sequenceName = "seq_nota_empresa",allocationSize=1)
	@Column(name = "id_nota_empresa")
	private Long id;
	
	@Column(name = "numero_documento")
	private String numeroDocumento;

	@Column(name = "subserie")
	private String subSerie;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao")
	private Date dataEmissao;
	
	@Column(name = "cpf_destinatario")
	private String cpfDestinatario;	
	
	@Column(name = "valor")
	private Double valor;
		
	@Column(name = "inscricao_estadual")
	private String inscricaoEstadual;
		
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_documento")
	private TipoDocumentoEnum tipoDocumento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "serie")
	private SerieEnum serie;	
	
	
	public NotaEmpresaToLegal (){
		//this.situacaoPontuacaoNota = SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getSubSerie() {
		return subSerie;
	}

	public void setSubSerie(String subSerie) {
		this.subSerie = subSerie;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getCpfDestinatario() {
		return cpfDestinatario;
	}

	public void setCpfDestinatario(String cpfDestinatario) {
		this.cpfDestinatario = cpfDestinatario;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public TipoDocumentoEnum getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public SerieEnum getSerie() {
		return serie;
	}

	public void setSerie(SerieEnum serie) {
		this.serie = serie;
	}

	
}