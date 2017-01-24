package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum ProblemaEmpresaEnum {

	EMPRESA_NAO_PERGUNTOU(1,"Não perguntou se eu desejava incluir o CPF na nota/cupom fiscal.  "),
	EMPRESA_EXIGIU_ELABORACAO(2,"Exigiu a elaboração de um cadastro para a colocação do CPF.  "),
	EMPRESA_ALEGOU_PROBLEMA(3,"Alegou problema com equipamento ou sistema"),
	EMPRESA_DIFICUTOU_FORNECIMENTO(4,"Dificultou o fornecimento do documento fiscal com CPF.  "),
	EMPRESA_ALEGOU_NAO_PARTICIPAR(5,"Alegou não participar do programa.");

	
	private Integer codigo;
	private String label;
	
	private ProblemaEmpresaEnum(int codigo, String label){
		
		this.codigo = codigo;
		this.label = label;
	}
	
	public static ProblemaEmpresaEnum convertCodProblemaEmpresaEnumParaEnum(Integer codigoMotivo) {
		for (ProblemaEmpresaEnum mr : values()) {
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

	public static List<ProblemaEmpresaEnum> list() {
		return Arrays.asList(values());
	}
}