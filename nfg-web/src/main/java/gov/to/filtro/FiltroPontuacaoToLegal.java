package gov.to.filtro;

import gov.to.persistencia.DataFiltroBetween;
import gov.to.persistencia.EntityProperty;

public class FiltroPontuacaoToLegal implements Filtro{

	@EntityProperty("notaFiscalToLegal.cpf")
	private String cpf;
	
	@EntityProperty("notaFiscalToLegal.dataEmissao")
	private DataFiltroBetween dataFiltro;
	
	@EntityProperty("sorteioToLegal.id")
	private Long idSorteio;
	
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

	public Long getIdSorteio() {
		return idSorteio;
	}

	public void setIdSorteio(Long idSorteio) {
		this.idSorteio = idSorteio;
	}
}