package entidade;

import java.util.Arrays;
import java.util.List;

public enum PerfilGeralEnum {

	SISTEMA(1,"Sistema"),
	EMPRESA(2,"Empresa"),
	CIDADAO(3,"Cidad�o");
	
	private Integer codigo;
	private String label;
	
	private PerfilGeralEnum(int codigo, String label){
		
		this.codigo = codigo;
		this.label = label;
	}
	
	public static PerfilGeralEnum convertCodProblemaEmpresaEnumParaEnum(Integer codigoMotivo) {
		for (PerfilGeralEnum mr : values()) {
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

	public static List<PerfilGeralEnum> list() {
		return Arrays.asList(values());
	}
}