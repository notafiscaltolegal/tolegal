package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum SerieEnum {

	UNICA(1,"UNICA"),
	D(2,"D"),
	D_UNICA(3,"D - UNICA");
	
	private Integer codigo;
	private String label;
	
	private SerieEnum(int codigo, String label){
		
		this.codigo = codigo;
		this.label = label;
	}
	
	public static SerieEnum convertCodTipoDocumentoFiscalParaEnum(Integer codigoMotivo) {
		for (SerieEnum mr : values()) {
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

	public static List<SerieEnum> list() {
		return Arrays.asList(values());
	}
}