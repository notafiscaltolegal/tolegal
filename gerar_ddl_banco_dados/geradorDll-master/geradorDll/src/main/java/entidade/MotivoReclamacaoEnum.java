package entidade;

import java.util.Arrays;
import java.util.List;

public enum MotivoReclamacaoEnum {

	EMPRESA_NAO_EMITIU_NOTA(1,"N�o emitiu Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletr�nica, ou emitiu com informa��es erradas. "),
	EMPRESA_EMITIU_SEM_CPF(2,"Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletr�nica com meu CPF n�o consta no painel Minhas Notas do Portal do Cidad�o."),
	EMPRESA_EMITIU_NOTA_COM_ERRO(3,"Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletr�nica emitido sem CPF por problema com a empresa.");
	
	private int codigo;
	private String label;
	
	private MotivoReclamacaoEnum(int codigo, String label){
		
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

	public static List<MotivoReclamacaoEnum> list() {
		return Arrays.asList(values());
	}
}