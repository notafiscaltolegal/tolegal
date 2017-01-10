package gov.goias.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe que representa o xml das fontes utilizadas pela aplicação.
 * 
 * @author Henrique Hirako 
 */
public class ReportsFontXmlMirror {
	/**
	 * Conjunto das fontes inseridas no XML de configuração.
	 */
	private Set<FontReport> fontsReport = new HashSet<FontReport>();
	
	/**
	 * Construtor padrao da classe.
	 */
	public ReportsFontXmlMirror() {
	}
	
	public void add(FontReport fonte) {
		this.fontsReport.add(fonte);
	}
	
	public Set<FontReport> getFonts() {
		return fontsReport;
	}

	public void setFonts(Set<FontReport> fonte) {
		this.fontsReport = fonte;
	}
}
