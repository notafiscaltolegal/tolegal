package gov.to.email;


public enum EmailRemetenteEnum {
	
	REMETENTE_EMAIL_ESTOQUE("remetente");
	
	private String chave;
	
	EmailRemetenteEnum(String chave){
		this.chave = chave;
	}

	public String getChave() {
		return chave;
	}
}