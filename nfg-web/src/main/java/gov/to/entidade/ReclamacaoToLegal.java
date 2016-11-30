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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="id_usuario",referencedColumnName="id_usr_to_legl")
	private UsuarioToLegal usuarioToLegal;
	
	public UsuarioToLegal getUsuarioToLegal() {
		return usuarioToLegal;
	}
	
	public void setUsuarioToLegal(UsuarioToLegal usuarioToLegal) {
		this.usuarioToLegal = usuarioToLegal;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="motivoReclamacao")
	private String motivoReclamacao;
	
	public String getMotivoReclamacao() {
		return motivoReclamacao;
	}
	
	public void setMotivoReclamacao(String motivoReclamacao) {
		this.motivoReclamacao = motivoReclamacao;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipoDocFiscal")
	private String tipoDocFiscal;
	
	public String getTipoDocFiscal() {
		return tipoDocFiscal;
	}
	
	public void setTipoDocFiscal(String tipoDocFiscal) {
		this.tipoDocFiscal = tipoDocFiscal;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cadastro")
	private Date dataCadastro;
	
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	
	@Column(name = "inscricaoEstadual")
	private String inscricaoEstadual;
	
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstaudal(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataEmissaoDocFiscal")
	private Date dataEmissaoDocFiscal;
	
	public Date getDataEmissaoDocFiscal() {
		return dataEmissaoDocFiscal;
	}

	public void setDataEmissaoDocFiscal(Date dataEmissaoDocFiscal) {
		this.dataEmissaoDocFiscal = dataEmissaoDocFiscal;
	}
	
	@Column(name = "numeroDocFiscal")
	private String numeroDocFiscal;
	
	public String getNumeroDocFiscal() {
		return numeroDocFiscal;
	}

	public void setNumeroDocFiscal(String numeroDocFiscal) {
		this.numeroDocFiscal = numeroDocFiscal;
	}
	
	@Column(name = "valorDocFiscal")
	private Double valorDocFiscal;
	
	public Double getValorDocFiscal() {
		return valorDocFiscal;
	}

	public void setValorDocFiscal(Double valorDocFiscal) {
		this.valorDocFiscal = valorDocFiscal;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "problemaEmpresa")
	private String problemaEmpresa;
	
	public String getProblemaEmpresa() {
		return problemaEmpresa;
	}

	public void setProblemaEmpresa(String problemaEmpresa) {
		this.problemaEmpresa = problemaEmpresa;
	}
	
	
	
	
	
	
	

	




}