package gov.to.dominio;

public enum Situacao {

	ATIVO("Ativo"),
	SORTEADO("SORTEADO"),
	INATIVO("Inativo");
	
	private String descricao;
	
	private Situacao(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}