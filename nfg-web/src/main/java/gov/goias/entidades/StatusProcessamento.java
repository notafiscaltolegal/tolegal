package gov.goias.entidades;

public enum StatusProcessamento {
	
	R("recebido"),
	A("Aguardando"),
	P("Em processamento"),
	S("Processado com Sucesso"),
	E("Processado com Erro"),
	C("Corrigido");
	
	private String label;
	
	private StatusProcessamento(String label){
		this.label = label;
	}
  
	public String getLabel() {
		return label;
	}
}
