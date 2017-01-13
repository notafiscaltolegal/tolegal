package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum TipoDocumentoEnum {

	MODELO1(1,"Modelo 1 - 1/A"),
	MODELO2(2,"Modelo 2 - (D-1)"),
	ECF_ANTIGO(3,"ECF - Antigo");
	
	private Integer codigo;
	private String label;
	
	private TipoDocumentoEnum(int codigo, String label){
		
		this.codigo = codigo;
		this.label = label;
	}
	
	public static TipoDocumentoEnum convertCodTipoDocumentoFiscalParaEnum(Integer codigoMotivo) {
		for (TipoDocumentoEnum mr : values()) {
			if (mr.getCodigo().equals(codigoMotivo)) {
				return mr;
			}
		}
		return null;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public static List<TipoDocumentoEnum> list() {
		return Arrays.asList(values());
	}
}