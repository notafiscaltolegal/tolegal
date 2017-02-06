package gov.to.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import gov.to.dominio.Situacao;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_cfop_to_legal")
public class CfopToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@Column(name = "id_cfop")
	private Long id;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="cpf_adm_logado")
	private String cpfAdmLogado;
	
	@Column(name="nome_adm_logado")
	private String nomeAdmLogado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_hora")
	private Date dataHora;
	
	@Column(name="situacao")
	@Enumerated(EnumType.STRING)
	private Situacao situacao;
	
	public CfopToLegal() {
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
	
	@Transient
	public String getDataHoraFormat() {
		
		String dataFormat = null;
		
		if (this.getDataHora() != null){
			
			dataFormat = formataData(this.getDataHora(), "dd/MM/yyyy");
		}
		
		return dataFormat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}