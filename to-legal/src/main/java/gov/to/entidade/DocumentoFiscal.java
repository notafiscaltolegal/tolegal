package gov.to.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_documentoFiscal")
public class DocumentoFiscal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_documentoFiscal", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_documentoFiscal", sequenceName = "seq_documentoFiscal",allocationSize=1)
	@Column(name = "id_documentoFiscal")
	private Long id;
	
	@Column(name = "inscricao_estadual")
	private Integer inscricaoEstadual;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao")
	private Date dataEmissao;
	
	@Column(name = "numero_doc")
	private Integer numeroDoc;
	
	@Column(name = "valor_doc")
	private Double valorDoc;
	
	@Lob
	@Column(name = "anexo_nota")
	private byte[] anexoNota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(Integer inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Integer getNumeroDoc() {
		return numeroDoc;
	}

	public void setNumeroDoc(Integer numeroDoc) {
		this.numeroDoc = numeroDoc;
	}

	public Double getValorDoc() {
		return valorDoc;
	}

	public void setValorDoc(Double valorDoc) {
		this.valorDoc = valorDoc;
	}

	public byte[] getAnexoNota() {
		return anexoNota;
	}

	public void setAnexoNota(byte[] anexoNota) {
		this.anexoNota = anexoNota;
	}
}