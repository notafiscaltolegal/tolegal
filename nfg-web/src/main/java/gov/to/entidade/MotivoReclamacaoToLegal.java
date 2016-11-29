package gov.to.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import gov.to.dominio.MotivoReclamacaoEnum;
import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_motivo_recl")
public class MotivoReclamacaoToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_motivo_recl", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_motivo_recl", sequenceName = "seq_motivo_recl",allocationSize=1)
	@Column(name = "id_motivo_recl")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="descricao")
	private MotivoReclamacaoEnum motivoDescricao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public MotivoReclamacaoEnum getMotivoDescricao() {
		return motivoDescricao;
	}

	public void setMotivoDescricao(MotivoReclamacaoEnum motivoDescricao) {
		this.motivoDescricao = motivoDescricao;
	}
}