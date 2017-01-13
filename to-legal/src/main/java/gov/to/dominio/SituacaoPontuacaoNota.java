package gov.to.dominio;

public enum SituacaoPontuacaoNota {

	PONTUADO(1,"Nota fiscal já processada"),
	AGUARDANDO_PROCESSAMENTO(2, "Aguardando processamento da nota");
	
	private int valor;
	private String label;
	
	private SituacaoPontuacaoNota(int valor, String label){
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