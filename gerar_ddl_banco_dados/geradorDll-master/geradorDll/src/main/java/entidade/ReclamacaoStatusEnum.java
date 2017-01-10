package entidade;

import java.util.Arrays;
import java.util.List;

public enum ReclamacaoStatusEnum {

	EMPRESA_NO_PRAZO(1,"No prazo"),
	EMPRESA_FORA_PRAZO(2,"Fora do prazo"),
	RECLAMACAO_CANCELADA(3,"Cancelada"),
	EMPRESA_RESPONDEU(4,"Respondeu"),
	RECLAMACAO_FINALIZADA(5,"Finalizada");

	
	private Integer codigo;
	private String label;
	
	private ReclamacaoStatusEnum(int codigo, String label){
		
		this.codigo = codigo;
		this.label = label;
	}
	
	public static ReclamacaoStatusEnum convertCodProblemaEmpresaEnumParaEnum(Integer codigoMotivo) {
		for (ReclamacaoStatusEnum mr : values()) {
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

	public static List<ReclamacaoStatusEnum> list() {
		return Arrays.asList(values());
	}
}