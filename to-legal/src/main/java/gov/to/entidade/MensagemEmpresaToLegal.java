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

import gov.to.dominio.SituacaoMensagem;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_msg_empresa")
public class MensagemEmpresaToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_msg_empresa", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_msg_empresa", sequenceName = "seq_msg_empresa",allocationSize=1)
	@Column(name = "id_msg_empresa")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_envio")
	private Date dataEnvio;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="mensagem")
	private String mensagem;
	
	@Column(name="cpf_adm_logado")
	private String cpfAdmLogado;
	
	@Column(name="nome_adm_logado")
	private String nomeAdmLogado;
	
	@Column(name="situacao")
	@Enumerated(EnumType.STRING)
	private SituacaoMensagem situacao;
	
	public MensagemEmpresaToLegal() {
		situacao = SituacaoMensagem.AGUARDANDO_ENVIO;
	}
	
	@Transient
	public String getDataEnvioFormat() {
		
		String dataFormat = null;
		
		if (this.getDataEnvio() != null){
			
			dataFormat = formataData(this.getDataEnvio(), "dd/MM/yyyy");
		}
		
		return dataFormat;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
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

	public SituacaoMensagem getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoMensagem situacao) {
		this.situacao = situacao;
	}

	public String getCpfAdmLogado() {
		return cpfAdmLogado;
	}

	public void setCpfAdmLogado(String cpfAdmLogado) {
		this.cpfAdmLogado = cpfAdmLogado;
	}

	public String getNomeAdmLogado() {
		return nomeAdmLogado;
	}

	public void setNomeAdmLogado(String nomeAdmLogado) {
		this.nomeAdmLogado = nomeAdmLogado;
	}
}