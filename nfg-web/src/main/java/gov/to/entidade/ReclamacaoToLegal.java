package gov.to.entidade;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import gov.to.dominio.MotivoReclamacaoEnum;
import gov.to.dominio.ProblemaEmpresaEnum;
import gov.to.dominio.TipoDocumentoFiscalEnum;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_reclamacao")
public class ReclamacaoToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_reclamacao", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_reclamacao", sequenceName = "seq_reclamacao",allocationSize=1)
	@Column(name = "id_reclamacao")
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="id_usuario",referencedColumnName="id_usr_to_legl")
	private UsuarioToLegal usuarioToLegal;
	
	@Enumerated(EnumType.STRING)
	@Column(name="motivo_reclamacao")
	private MotivoReclamacaoEnum motivoReclamacao;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "problema_empresa")
	private ProblemaEmpresaEnum problemaEmpresa;
		
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_doc_fiscal")
	private TipoDocumentoFiscalEnum tipoDocFiscal;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cadastro")
	private Date dataCadastro;	
	
	@Column(name = "inscricao_estadual")
	private String inscricaoEstadual;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao_doc_fiscal")
	private Date dataEmissaoDocFiscal;	
	
	@Column(name = "numero_doc_fiscal")
	private String numeroDocFiscal;
		
	@Column(name = "valor_doc_fiscal")
	private Double valorDocFiscal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioToLegal getUsuarioToLegal() {
		return usuarioToLegal;
	}

	public void setUsuarioToLegal(UsuarioToLegal usuarioToLegal) {
		this.usuarioToLegal = usuarioToLegal;
	}

	public MotivoReclamacaoEnum getMotivoReclamacao() {
		return motivoReclamacao;
	}

	public void setMotivoReclamacao(MotivoReclamacaoEnum motivoReclamacao) {
		this.motivoReclamacao = motivoReclamacao;
	}

	public ProblemaEmpresaEnum getProblemaEmpresa() {
		return problemaEmpresa;
	}

	public void setProblemaEmpresa(ProblemaEmpresaEnum problemaEmpresa) {
		this.problemaEmpresa = problemaEmpresa;
	}

	public TipoDocumentoFiscalEnum getTipoDocFiscal() {
		return tipoDocFiscal;
	}

	public void setTipoDocFiscal(TipoDocumentoFiscalEnum tipoDocFiscal) {
		this.tipoDocFiscal = tipoDocFiscal;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public Date getDataEmissaoDocFiscal() {
		return dataEmissaoDocFiscal;
	}

	public void setDataEmissaoDocFiscal(Date dataEmissaoDocFiscal) {
		this.dataEmissaoDocFiscal = dataEmissaoDocFiscal;
	}

	public String getNumeroDocFiscal() {
		return numeroDocFiscal;
	}

	public void setNumeroDocFiscal(String numeroDocFiscal) {
		this.numeroDocFiscal = numeroDocFiscal;
	}

	public Double getValorDocFiscal() {
		return valorDocFiscal;
	}

	public void setValorDocFiscal(Double valorDocFiscal) {
		this.valorDocFiscal = valorDocFiscal;
	}

		

	
	
	

}