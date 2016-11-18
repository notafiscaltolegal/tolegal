package gov.to.filtro;

import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.persistencia.DataFiltroBetween;
import gov.to.persistencia.EntityProperty;

public class FiltroNotaFiscalToLegal implements Filtro{

	@EntityProperty("cpf")
	private String cpf;
	
	@EntityProperty("chaveAcesso")
	private String chaveAcesso;
	
	@EntityProperty("dataEmissao")
	private DataFiltroBetween dataFiltro;
	
	@EntityProperty("situacaoPontuacaoNota")
	private SituacaoPontuacaoNota situacaoPontuacaoNota;
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public DataFiltroBetween getDataFiltro() {
		return dataFiltro;
	}

	public void setDataFiltro(DataFiltroBetween dataFiltro) {
		this.dataFiltro = dataFiltro;
	}

	public SituacaoPontuacaoNota getSituacaoPontuacaoNota() {
		return situacaoPontuacaoNota;
	}

	public void setSituacaoPontuacaoNota(SituacaoPontuacaoNota situacaoPontuacaoNota) {
		this.situacaoPontuacaoNota = situacaoPontuacaoNota;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
}