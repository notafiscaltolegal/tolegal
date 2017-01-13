package gov.to.dominio;

public enum SituacaoSorteio {

	ATIVO("Ativo"),
	AGUARDANDO_SORTEIO("Aguardando Sorteio"),
	SORTEADO("Sorteado"),
	INATIVO("Inativo");
	
	private String descricao;
	
	private SituacaoSorteio(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}