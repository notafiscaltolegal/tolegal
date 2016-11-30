package entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tb_pont_nota")
public class PontuacaoToLegal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_pont_nota", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "seq_pont_nota", sequenceName = "seq_pont_nota",allocationSize=1)
	@Column(name = "id_pont_to_legl")
	private Long id;

	@Column(name = "qnt_ponto")
	private Integer qntPonto;
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id_nota_legal")
	private NotaFiscalToLegal notaFiscalToLegal;
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id_sorteio")
	private SorteioToLegal sorteioToLegal;
	
	public PontuacaoToLegal (NotaFiscalToLegal nft){
		this.notaFiscalToLegal = nft;
	}
	
	public PontuacaoToLegal(){}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQntPonto() {
		return qntPonto;
	}

	public NotaFiscalToLegal getNotaFiscalToLegal() {
		return notaFiscalToLegal;
	}

	public SorteioToLegal getSorteioToLegal() {
		return sorteioToLegal;
	}

	public void setSorteioToLegal(SorteioToLegal sorteioToLegal) {
		this.sorteioToLegal = sorteioToLegal;
	}
}