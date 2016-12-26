package gov.to.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SorteioProperties {

	private static final String ARQUIVO_CONFIGURACAO = "/sorteio.properties";
	
	public static final String NUMERO_SORTEIO = "numero_sorteio";
	public static final String QNT_MAXIMA_PONTOS = "qnt_max_pont";
	public static final String QNT_MAXIMA_PONTOS_POR_DOCUMENTO = "qnt_max_pont_por_documento";
	public static final String QNT_MINIMA_PONTOS_POR_DOCUMENTO = "qnt_min_pont_por_documento";
	public static final String QNT_MAX_DOCUMENTO = "qnt_max_documento";
	public static final String QNT_MAX_NOTA_POR_CNPJ = "qnt_max_nota_por_cnpj";
	public static final String QNT_PONTOS_POR_BILHETE = "qnt_pontos_por_bilhete";

	public static final String QNT_PONTOS_BONUS_CADASTRO = "qnt_pontos_bonus_cadastro";
	
	private static Properties properties;
	
	private SorteioProperties(){
		//Evitando instanciação
	}

	static {
		InputStream inStream;
		properties = new Properties();
		inStream = SorteioProperties.class.getResourceAsStream(ARQUIVO_CONFIGURACAO);
		try {
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Properties getProperties() {
		return properties;
	}
	
	public static int getValue(String param) {
		return Integer.parseInt(properties.getProperty(param));
	}
}
