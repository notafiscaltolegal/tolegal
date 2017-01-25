package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum SituacaoBilhete {

	VALIDO(1,"V�lido"),
	AGUARDANDO_PROXIMO_SORTEIO(3,"Limite m�ximo do �ltimo sorteio atingido."),
	INVALIDO(2,"Inv�lido");
	
	private int codigo;
	private String label;
	
	private SituacaoBilhete(int codigo, String label){
		
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

	public static List<SituacaoBilhete> list() {
		return Arrays.asList(values());
	}
}