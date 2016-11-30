package entidade;

import java.util.Arrays;
import java.util.List;

public enum SituacaoUsuario {

	INATIVO(1,"Aguardando confirma��o no e-mail"),
	ATIVO(2,"Ativo");
	
	private int codigo;
	private String label;
	
	private SituacaoUsuario(int codigo, String label){
		
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

	public static List<SituacaoUsuario> list() {
		return Arrays.asList(values());
	}
}