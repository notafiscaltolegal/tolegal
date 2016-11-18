package gov.to.dominio;

public enum SituacaoBonusPontuacao {

	ATIVO(1,"Bonus sendo contabilizado"),
	INATIVO(2, "Bonus não gerará bilhetes");
	
	private int valor;
	private String label;
	
	private SituacaoBonusPontuacao(int valor, String label){
		this.valor = valor;
		this.label = label;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}