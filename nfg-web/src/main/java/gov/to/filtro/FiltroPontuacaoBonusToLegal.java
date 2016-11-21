package gov.to.filtro;

import gov.to.dominio.SituacaoBonusPontuacao;
import gov.to.persistencia.EntityProperty;

public class FiltroPontuacaoBonusToLegal implements Filtro{

	@EntityProperty("cpf")
	private String cpf;
	
	@EntityProperty(pesquisaExata=true,value="situacaoBonusPontuacao")
	private SituacaoBonusPontuacao situacaoBonus;
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public SituacaoBonusPontuacao getSituacaoBonus() {
		return situacaoBonus;
	}

	public void setSituacaoBonus(SituacaoBonusPontuacao situacaoBonus) {
		this.situacaoBonus = situacaoBonus;
	}
}