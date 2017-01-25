package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum ReclamacaoStatusEnum {

	EMPRESA_NO_PRAZO(1,"No prazo",1),
	EMPRESA_FORA_PRAZO(2,"Fora do prazo",1),
	RECLAMACAO_CANCELADA(3,"Cancelada",11),
	EMPRESA_RESPONDEU(4,"Respondeu",1),
	RECLAMACAO_FINALIZADA(5,"Finalizada",1);

	
	private Integer codigo;
	private String label;
	private Integer codigoEnumGoias;
	
	private ReclamacaoStatusEnum(int codigo, String label, int codigoEnumGoias){
		
		this.codigo = codigo;
		this.label = label;
		this.codigoEnumGoias = codigoEnumGoias;
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

	public Integer getCodigoEnumGoias() {
		return codigoEnumGoias;
	}

	public void setCodigoEnumGoias(Integer codigoEnumGoias) {
		this.codigoEnumGoias = codigoEnumGoias;
	}

	public static ReclamacaoStatusEnum convertCodEnumGoiasParaEnumToLegal(Integer novoCodgTipoCompl) {
		for (ReclamacaoStatusEnum mr : values()) {
			if (mr.getCodigoEnumGoias().equals(novoCodgTipoCompl)) {
				return mr;
			}
		}
		return null;
	}
}