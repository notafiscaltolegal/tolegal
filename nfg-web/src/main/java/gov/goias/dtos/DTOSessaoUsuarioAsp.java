package gov.goias.dtos;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("autenticacao")
public class DTOSessaoUsuarioAsp {

	private String browser;

    private String horario;
	
	private String ip;
	
	private boolean secaoValida;
	
	private String versaoBrowser;
	
	private DTOUsuarioPortalAsp usuario;

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isSecaoValida() {
		return secaoValida;
	}

	public void setSecaoValida(boolean secaoValida) {
		this.secaoValida = secaoValida;
	}

	public String getVersaoBrowser() {
		return versaoBrowser;
	}

	public void setVersaoBrowser(String versaoBrowser) {
		this.versaoBrowser = versaoBrowser;
	}

	public DTOUsuarioPortalAsp getUsuario() {
		return usuario;
	}

	public void setUsuario(DTOUsuarioPortalAsp usuario) {
		this.usuario = usuario;
	}
	
}
