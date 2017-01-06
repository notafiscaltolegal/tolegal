package gov.to.filtro;

import java.util.Date;

import gov.to.dominio.SerieEnum;
import gov.to.persistencia.EntityProperty;

public class FiltroNotaEmpresaToLegal implements Filtro{

	@EntityProperty("numeroDocumento")
	private String numeroDocumento;	
	
	@EntityProperty("serie")
	private SerieEnum serieEnum;
	
	@EntityProperty("dataEmissao")
	private Date dataEmissao;
	
	@EntityProperty("inscricaoEstadual")
	private String inscricaoEstadual;
	
	

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public SerieEnum getSerieEnum() {
		return serieEnum;
	}

	public void setSerieEnum(SerieEnum serieEnum) {
		this.serieEnum = serieEnum;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	

	
}