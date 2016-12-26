package gov.goias.entidades.dominio;

public enum TipoUsuario {

	CIDADAO("Cidad&#225;o",1), CONTADOR("Contador",2), EMPRESA("Empresa",3);
	
	private String label;
	private Integer tipo;
	
	private TipoUsuario(String label, Integer tipo){
		this.label = label;
		this.tipo = tipo;
	}

	public String getLabel() {
		return label;
	}

	public Integer getTipo() {
		return tipo;
	}
}
