package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum TipoDocumentoFiscalEnum {

	NOTA_FISCAL(1,"Nota Fiscal"),
	NOTA_FISCAL_ELETRONICA(2,"Nota Fiscal Eletrônica"),
	CUPOM_FISCAL(3,"Cupom Fiscal");
	
	private int codigo;
	private String label;
	
	private TipoDocumentoFiscalEnum(int codigo, String label){
		
		this.codigo = codigo;
		this.label = label;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public static List<TipoDocumentoFiscalEnum> list() {
		return Arrays.asList(values());
	}
}