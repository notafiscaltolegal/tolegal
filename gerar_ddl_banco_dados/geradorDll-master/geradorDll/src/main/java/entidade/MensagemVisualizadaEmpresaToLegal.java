package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_msg_visu_empresa")
public class MensagemVisualizadaEmpresaToLegal {
	
	@Id
	@GeneratedValue(generator = "seq_msg_visu_empresa", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_msg_visu_empresa", sequenceName = "seq_msg_visu_empresa",allocationSize=1)
	@Column(name = "id_msg_visu_empresa")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_leitura")
	private Date dataLeitura;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="mensagem")
	private String mensagem;
	
	@Column(name="inscricao_estadual")
	private String inscricaoEstadual;
	

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

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
}