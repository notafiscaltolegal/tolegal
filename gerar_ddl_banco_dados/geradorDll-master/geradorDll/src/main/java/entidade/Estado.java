package entidade;

import java.util.Arrays;
import java.util.List;

public enum Estado {
	
	AC("Acre"), 
	AL("Alagoas"), 
	AM("Amazonas"), 
	AP("Amapa"), 
	BA("Bahia"), 
	CE("Cear�"), 
	DF("Distrito Federal"), 
	ES("Esp�rito Santo"), 
	GO("Goi�s"), 
	MA("Maranh�o"), 
	MG("Minas Gerais"), 
	MS("Mato Grosso do Sul"), 
	MT("Mato Grosso"), 
	PA("Par�"), 
	PB("Para�ba"), 
	PE("Pernambuco"), 
	PI("Piau�"), 
	PR("Paran�"), 
	RJ("Rio de Janeiro"), 
	RN("Rio Grande do Norte"), 
	RO("Rond�ia"), 
	RR("Roraima"), 
	RS("Rio Grande do Sul"), 
	SC("Santa Catarina"), 
	SE("Sergipe"), 
	SP("S�o Paulo"), 
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
