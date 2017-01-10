package gov.to.filtro;

import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.persistencia.DataFiltroBetween;
import gov.to.persistencia.EntityProperty;

public class FiltroPontuacaoToLegal implements Filtro{

	@EntityProperty("notaFiscalToLegal.cpf")
	private String cpf;
	
	@EntityProperty("notaFiscalToLegal.dataEmissao")
	private DataFiltroBetween dataFiltro;
	
	@EntityProperty("sorteioToLegal.id")
	private Long idSorteio;
	
	@EntityProperty(pesquisaExata=true,value="situacaoPontuacao",ignoraCaseSensitive=false)
	private SituacaoPontuacaoNota situacaoPontuacaoNota;
	
	@EntityProperty("notaFiscalEmpresaToLegal.id")
	private Long idNotaEmpresa;
	
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

	public SituacaoPontuacaoNota getSituacaoPontuacaoNota() {
		return situacaoPontuacaoNota;
	}

	public void setSituacaoPontuacaoNota(SituacaoPontuacaoNota situacaoPontuacaoNota) {
		this.situacaoPontuacaoNota = situacaoPontuacaoNota;
	}

	public Long getIdNotaEmpresa() {
		return idNotaEmpresa;
	}

	public void setIdNotaEmpresa(Long idNotaEmpresa) {
		this.idNotaEmpresa = idNotaEmpresa;
	}
}