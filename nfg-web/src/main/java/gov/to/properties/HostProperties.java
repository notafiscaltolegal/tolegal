package gov.to.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class HostProperties {
	
	private static final String ARQUIVO_CONFIGURACAO = "/host.properties";
	
	private static Properties properties;
	
	private HostProperties(){
	}

	static {
		InputStream inStream;
		properties = new Properties();
		inStream = HostProperties.class.getResourceAsStream(ARQUIVO_CONFIGURACAO);
		try {
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Properties getProperties() {
		return properties;
	}
	
	public static String homeUrl() {
		return properties.getProperty("urlHome");
	}
}