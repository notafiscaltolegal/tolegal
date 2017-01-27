package entidade;

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

@Entity
@Table(name = "tb_bloqueio_cpf")
public class BloqueioCpfToLegal {
	
	/**
	 * 
	 */

	@Id
	@GeneratedValue(generator = "seq_bloqueio_cpf", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_bloqueio_cpf", sequenceName = "seq_bloqueio_cpf",allocationSize=1)
	@Column(name = "id_bloqueio_cpf")
	private Long id;
	
	@Column(name="cpf")
	private String cpf;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="cpf_adm_logado")
	private String cpfAdmLogado;
	
	@Column(name="nome_adm_logado")
	private String nomeAdmLogado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_hora_bloqueio")
	private Date dataHoraBloqueio;
	
	@Column(name="motivo_bloqueio")
	private String motivo;
	
	@Column(name="situacao")
	@Enumerated(EnumType.STRING)
	private Situacao situacao;
	
	public BloqueioCpfToLegal() {
		situacao = Situacao.ATIVO;
	}
	
	@Transient
	public boolean getAtivo() {
		
		return Situacao.ATIVO.equals(situacao);
	}
	
	@Transient
	public boolean getInativo() {
		
		return Situacao.INATIVO.equals(situacao);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCpfAdmLogado() {
		return cpfAdmLogado;
	}

	public void setCpfAdmLogado(String cpfAdmLogado) {
		this.cpfAdmLogado = cpfAdmLogado;
	}

	public Date getDataHoraBloqueio() {
		return dataHoraBloqueio;
	}

	public void setDataHoraBloqueio(Date dataHoraBloqueio) {
		this.dataHoraBloqueio = dataHoraBloqueio;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeAdmLogado() {
		return nomeAdmLogado;
	}

	public void setNomeAdmLogado(String nomeAdmLogado) {
		this.nomeAdmLogado = nomeAdmLogado;
	}
}