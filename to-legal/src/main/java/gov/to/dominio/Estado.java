package gov.to.dominio;

import java.util.Arrays;
import java.util.List;

public enum Estado {
	
	AC("Acre"), 
	AL("Alagoas"), 
	AM("Amazonas"), 
	AP("Amapa"), 
	BA("Bahia"), 
	CE("Ceará"), 
	DF("Distrito Federal"), 
	ES("Espírito Santo"), 
	GO("Goiás"), 
	MA("Maranhão"), 
	MG("Minas Gerais"), 
	MS("Mato Grosso do Sul"), 
	MT("Mato Grosso"), 
	PA("Pará"), 
	PB("Paraíba"), 
	PE("Pernambuco"), 
	PI("Piauí"), 
	PR("Paraná"), 
	RJ("Rio de Janeiro"), 
	RN("Rio Grande do Norte"), 
	RO("Rondôia"), 
	RR("Roraima"), 
	RS("Rio Grande do Sul"), 
	SC("Santa Catarina"), 
	SE("Sergipe"), 
	SP("São Paulo"), 
	TO("Tocantins");
	
	private String label;
	
	private Estado(String label){
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public static List<Estado> list() {
		return Arrays.asList(values());
	}

	public static Estado estadoPorName(String uf) {
		
		for (Estado es : list()){
			
			if (es.name().equalsIgnoreCase(uf)){
				return es;
			}
		}
		
		return null;
	}
}
