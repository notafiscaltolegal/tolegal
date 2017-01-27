package entidade;

public enum SituacaoMensagem {

	ENVIADA("Enviada"),
	
	AGENDADA("Agendada"),
	
	AGUARDANDO_ENVIO("Aguardando Envio");
	
	private String descricao;
	
	private SituacaoMensagem(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}