package gov.to.email;

public enum EmailEnum {
	
	RECUPERAR_SENHA("emails/recuperar_senha/emailRecuperarSenha.html","Nova senha TOLegal","{cpf}","{senha}"),
	CONFIRMAR_CADASTRO("emails/cadastro/emailConfirmarCadastro.html","Confirmar cadastrado To Legal!","{cpf}","{link}");
	
	/** The arquivo. */
	private String arquivo;
	
	/** The assunto. */
	private String assunto;
	
	/** The parametros. */
	private String[] parametros;
	
	/**
	 * Instantiates a new email enum.
	 *
	 * @param arquivo the arquivo
	 * @param assunto the assunto
	 * @param parametro the parametro
	 */
	EmailEnum(String arquivo, String assunto, String... parametro){
		this.arquivo = arquivo;
		this.parametros = parametro;
		this.assunto = assunto;
	}

	/**
	 * Gets the parametros.
	 *
	 * @return the parametros
	 */
	public String[] getParametros() {
		return parametros;
	}

	/**
	 * Gets the arquivo.
	 *
	 * @return the arquivo
	 */
	public String getArquivo() {
		return arquivo;
	}

	/**
	 * Gets the assunto.
	 *
	 * @return the assunto
	 */
	public String getAssunto() {
		return assunto;
	}

	public String assunto(EmailParametro parametro) {
		return EmailUtils.substituiParametroAssunto(assunto, parametro, parametros);
	}
}