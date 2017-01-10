package gov.goias.util;

/**
 * Classe que representa a tag das fontes utilizada pela aplicacao. 
 * 
 * @author Henrique Hirako
 */
public class FontReport {
	/** Nome da Fonte */
	private String name;
	
	/** Fonte negrito */
	private Boolean bold;
	
	/** Fonte italico */
	private Boolean italic;
	
	/** Nome do arquivo da fonte */
	private String fileName;
	
	/**
	 * Construtor Default.
	 */
	public FontReport() {
	}

	/**
	 * Construtor com parametros.
	 * 
	 * @param name
	 * @param bold
	 * @param italic
	 * @param fileName
	 */
	public FontReport(String name, Boolean bold, Boolean italic, String fileName) {
		super();
		this.name = name;
		this.bold = bold;
		this.italic = italic;
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isBold() {
		return bold;
	}

	public void setBold(Boolean bold) {
		this.bold = bold;
	}

	public Boolean isItalic() {
		return italic;
	}

	public void setItalic(Boolean italic) {
		this.italic = italic;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
