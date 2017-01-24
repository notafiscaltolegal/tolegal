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
import javax.persistence.Transient;

import gov.to.dominio.SituacaoMensagem;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_msg_visu_cidadao")
public class MensagemVisualizadaCidadaoToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_msg_cidadao", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_msg_cidadao", sequenceName = "seq_msg_cidadao",allocationSize=1)
	@Column(name = "id_msg_visu_cidadao")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_leitura")
	private Date dataLeitura;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="mensagem")
	private String mensagem;
	
	@Column(name="cpf")
	private String cpf;
	
	@Column(name="situacao")
	@Enumerated(EnumType.STRING)
	private SituacaoMensagem situacao;
	
	@ManyToOne
	@JoinColumn(name="id_msg_sefaz",referencedColumnName="id_msg_sefaz")
	private MensagemSefazToLegal msgSefazToLegal;
	
	@Transient
	public String getDataLeituraFormat() {
		
		String dataFormat = null;
		
		if (this.getDataLeitura() != null){
			
			dataFormat = formataData(this.getDataLeitura(), "dd/MM/yyyy");
		}
		
		return dataFormat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataLeitura() {
		return dataLeitura;
	}

	public void setDataLeitura(Date dataLeitura) {
		this.dataLeitura = dataLeitura;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public SituacaoMensagem getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoMensagem situacao) {
		this.situacao = situacao;
	}

	public MensagemSefazToLegal getMsgSefazToLegal() {
		return msgSefazToLegal;
	}

	public void setMsgSefazToLegal(MensagemSefazToLegal msgSefazToLegal) {
		this.msgSefazToLegal = msgSefazToLegal;
	}
}