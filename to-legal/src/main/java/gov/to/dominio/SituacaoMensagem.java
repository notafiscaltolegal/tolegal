package gov.to.dominio;

public enum SituacaoMensagem {

	ENVIADA("Enviada"),
	
	LIDA("Lida"),
	
	AGUARDANDO_ENVIO("Aguardando Envio"), 
	
	AGUARDANDO_LEITURA("Aguardando Leitura");
	
	private String descricao;
	
	private SituacaoMensagem(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}