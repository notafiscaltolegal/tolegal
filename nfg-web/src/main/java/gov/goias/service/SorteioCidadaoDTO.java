package gov.goias.service;

import gov.goias.entidades.RegraSorteio;

public class SorteioCidadaoDTO {

	private RegraSorteio sorteio;
	private int totalDocs;
	private int totalPontos;
	private Long totalBilhetes;
	private String dataFimRegra;

	public RegraSorteio getSorteio() {
		return sorteio;
	}

	public void setSorteio(RegraSorteio sorteio) {
		this.sorteio = sorteio;
	}

	public int getTotalDocs() {
		return totalDocs;
	}

	public void setTotalDocs(int totalDocs) {
		this.totalDocs = totalDocs;
	}

	public int getTotalPontos() {
		return totalPontos;
	}

	public void setTotalPontos(int totalPontos) {
		this.totalPontos = totalPontos;
	}

	public Long getTotalBilhetes() {
		return totalBilhetes;
	}

	public void setTotalBilhetes(Long totalBilhetes) {
		this.totalBilhetes = totalBilhetes;
	}

	public String getDataFimRegra() {
		return dataFimRegra;
	}

	public void setDataFimRegra(String dataFimRegra) {
		this.dataFimRegra = dataFimRegra;
	}
}