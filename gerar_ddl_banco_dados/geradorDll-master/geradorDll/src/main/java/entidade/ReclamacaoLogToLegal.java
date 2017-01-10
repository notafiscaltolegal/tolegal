package entidade;

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

@Entity
@Table(name = "tb_reclamacao_log")
public class ReclamacaoLogToLegal{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_reclamacao_log", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_reclamacao_log", sequenceName = "seq_reclamacao_log",allocationSize=1)
	@Column(name = "id_reclamacao_log")
	private Long id;	
	
	@ManyToOne
	@JoinColumn(name="id_reclamacao",referencedColumnName="id_reclamacao")
	private ReclamacaoToLegal reclamacaoToLegal;
	
	@Enumerated(EnumType.STRING)
	@Column(name="perfil")
	private PerfilGeralEnum perfilGeral;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_reclamacao")
	private ReclamacaoStatusEnum statusReclamacao;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_reclamacao")
	private Date dataReclamacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReclamacaoToLegal getReclamacaoToLegal() {
		return reclamacaoToLegal;
	}

	public void setReclamacaoToLegal(ReclamacaoToLegal reclamacaoToLegal) {
		this.reclamacaoToLegal = reclamacaoToLegal;
	}

	public PerfilGeralEnum getPerfilGeral() {
		return perfilGeral;
	}

	public void setPerfilGeral(PerfilGeralEnum perfilGeral) {
		this.perfilGeral = perfilGeral;
	}

	public ReclamacaoStatusEnum getStatusReclamacao() {
		return statusReclamacao;
	}

	public void setStatusReclamacao(ReclamacaoStatusEnum statusReclamacao) {
		this.statusReclamacao = statusReclamacao;
	}

	public Date getDataReclamacao() {
		return dataReclamacao;
	}

	public void setDataReclamacao(Date dataReclamacao) {
		this.dataReclamacao = dataReclamacao;
	}	
}