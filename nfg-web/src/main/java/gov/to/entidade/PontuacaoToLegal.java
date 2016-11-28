package gov.to.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
 
import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.persistencia.EntidadeBasica;
import gov.to.properties.SorteioProperties;

@Entity
@Table(name = "tb_pont_nota")
public class PontuacaoToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_pont_nota", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_pont_nota", sequenceName = "seq_pont_nota",allocationSize=1)
	@Column(name = "id_pont_to_legl")
	private Long id;

	@Column(name = "qnt_ponto")
	private Integer qntPonto;
	
	@ManyToOne
	@JoinColumn(name="ID_NOTA_LEGAL",  referencedColumnName="id_nota_legal" )
	private NotaFiscalToLegal notaFiscalToLegal;
	
	@ManyToOne
	@JoinColumn(name="ID_SORTEIO", referencedColumnName="id_sorteio")
	private SorteioToLegal sorteioToLegal;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status_pontuacao")
	private SituacaoPontuacaoNota situacaoPontuacao;
	
	public PontuacaoToLegal (NotaFiscalToLegal nft){
		this.notaFiscalToLegal = nft;
		this.situacaoPontuacao = SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO;
	}
	
	public PontuacaoToLegal(){
		this.situacaoPontuacao = SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO;
	}
	
	@PrePersist
	public void calcPontuacao(){
		
		final int VALOR_MAXIMO_PONTUACAO_POR_NOTA = SorteioProperties.getValue(SorteioProperties.QNT_MAXIMA_PONTOS_POR_DOCUMENTO);
		final int VALOR_MINIMO_PONTUACAO_POR_NOTA = SorteioProperties.getValue(SorteioProperties.QNT_MINIMA_PONTOS_POR_DOCUMENTO);
		
		if (notaFiscalToLegal != null && notaFiscalToLegal.getValor() != null){
			
			if (notaFiscalToLegal.getValor() > VALOR_MAXIMO_PONTUACAO_POR_NOTA){
				
				this.qntPonto = VALOR_MAXIMO_PONTUACAO_POR_NOTA;
				
			}else if (notaFiscalToLegal.getValor() > VALOR_MINIMO_PONTUACAO_POR_NOTA){
				
				this.qntPonto = notaFiscalToLegal.getValor().intValue();
			}
		}
	}

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
	
	public void setNotaFiscalToLegal(NotaFiscalToLegal notaFiscalToLegal) {
		this.notaFiscalToLegal = notaFiscalToLegal;
	}

	public SorteioToLegal getSorteioToLegal() {
		return sorteioToLegal;
	}

	public void setSorteioToLegal(SorteioToLegal sorteioToLegal) {
		this.sorteioToLegal = sorteioToLegal;
	}

	public SituacaoPontuacaoNota getSituacaoPontuacao() {
		return situacaoPontuacao;
	}

	public void setSituacaoPontuacao(SituacaoPontuacaoNota situacaoPontuacao) {
		this.situacaoPontuacao = situacaoPontuacao;
	}
}