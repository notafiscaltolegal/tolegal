package gov.to.filtro;

import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.persistencia.EntityProperty;

public class FiltroPontuacaoBonusToLegal implements Filtro{

	@EntityProperty("cpf")
	private String cpf;
	
	@EntityProperty(pesquisaExata=true,value="situacaoPontuacaoNota")
	private SituacaoPontuacaoNota situacaoPontuacaoNota;
	
	@EntityProperty("sorteio.id")
	private Long idSorteio;
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public SituacaoPontuacaoNota getSituacaoPontuacaoNota() {
		return situacaoPontuacaoNota;
	}

	public void setSituacaoPontuacaoNota(SituacaoPontuacaoNota situacaoPontuacaoNota) {
		this.situacaoPontuacaoNota = situacaoPontuacaoNota;
	}

	public Long getIdSorteio() {
		return idSorteio;
	}

	public void setIdSorteio(Long idSorteio) {
		this.idSorteio = idSorteio;
	}
}