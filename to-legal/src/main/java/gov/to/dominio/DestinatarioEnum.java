package gov.to.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum DestinatarioEnum {

	EMPRESA("Empresas"),
	
	CIDADAO("Cidadão");
	
	private String descricao;
	
	private DestinatarioEnum(String descricao){
		this.descricao = descricao;
	}
	
	public static List<SelectItem> selectItems(){
		
		List<SelectItem> list = new ArrayList<SelectItem>();
		
		for (DestinatarioEnum des : values()){
			
			list.add(new SelectItem(des, des.descricao));
		}
		
		return list;
	}

	public String getDescricao() {
		return descricao;
	}
}