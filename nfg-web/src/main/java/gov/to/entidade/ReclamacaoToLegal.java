package gov.to.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cadastro")
	private Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="id_usuario",referencedColumnName="id_usr_to_legl")
	private UsuarioToLegal usuarioToLegal;
	
	@ManyToOne
	@JoinColumn(name="id_motivo",referencedColumnName="id_motivo_recl")
	private MotivoReclamacaoToLegal motivoReclamacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioToLegal getUsuarioToLegal() {
		return usuarioToLegal;
	}

	public void setUsuarioToLegal(UsuarioToLegal usuarioToLegal) {
		this.usuarioToLegal = usuarioToLegal;
	}

	public MotivoReclamacaoToLegal getMotivoReclamacao() {
		return motivoReclamacao;
	}

	public void setMotivoReclamacao(MotivoReclamacaoToLegal motivoReclamacao) {
		this.motivoReclamacao = motivoReclamacao;
	}
}