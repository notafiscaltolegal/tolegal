package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum MotivoReclamacaoEnum {

	EMPRESA_NAO_EMITIU_NOTA(1,
			"Não emitiu Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletrônica, ou emitiu com informações erradas. "), EMPRESA_EMITIU_SEM_CPF(
					2,
					"Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletrônica com meu CPF não consta no painel Minhas Notas do Portal do Cidadão."), EMPRESA_EMITIU_NOTA_COM_ERRO(
							3,
							"Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletrônica emitido sem CPF por problema com a empresa.");

	private Integer codigo;
	private String label;

	private MotivoReclamacaoEnum(int codigo, String label) {

		this.codigo = codigo;
		this.label = label;
	}

	public static MotivoReclamacaoEnum convertCodigoMotivoParaEnum(Integer codigoMotivo) {
		for (MotivoReclamacaoEnum mr : values()) {
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

	public static List<MotivoReclamacaoEnum> list() {
		return Arrays.asList(values());
	}
}