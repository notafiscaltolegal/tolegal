package gov.to.email;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.goias.exceptions.NFGException;

public class EmailParametro {

	private String chaveParametro;
	private String valorParametro;
	
	private List<EmailParametro> listParametros;
	
	public EmailParametro(){}
	
	public EmailParametro(String chaveParametro, String valorParametro){
		
		parametrosObrigatorios(chaveParametro, valorParametro);
		
		this.chaveParametro = chaveParametro;
		this.valorParametro = valorParametro;
	}
	
	public void addParametro(String chaveParametro, String valorParametro) {
		
		parametrosObrigatorios(chaveParametro, valorParametro);
		
		if (listParametros == null || listParametros.isEmpty()){
			listParametros = new ArrayList<EmailParametro>();
		}
		
		listParametros.add(new EmailParametro(chaveParametro, valorParametro));
	}

	private void parametrosObrigatorios(String chaveParametro,String valorParametro) {
		if (StringUtils.isBlank(chaveParametro) || StringUtils.isBlank(valorParametro)){
			throw new NFGException("erro no parse dos parametros do e-mail");
		}
	}

	public List<EmailParametro> getListParametros() {
		return listParametros;
	}

	public String getChaveParametro() {
		return chaveParametro;
	}

	public String getValorParametro() {
		return valorParametro;
	}
}
