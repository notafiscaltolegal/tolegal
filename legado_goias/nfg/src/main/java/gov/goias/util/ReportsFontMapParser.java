package gov.goias.util;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

//import net.sf.jasperreports.engine.export.FontKey;
//import net.sf.jasperreports.engine.export.PdfFont;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Classe esponsável por fazer o parser do arquivo .XML mapeado com as fontes
 * para um mapa de fontes que pode ser compreendido pelo Jasper Reports 3.0.0
 * 
 * @author Henrique Hirako
 */
public abstract class ReportsFontMapParser {

//	static final String FILE_REPORTS_FONTS_MAP = "/WEB-INF/jasper/fontes/jasperFontes.xml";
//	static final String DIRETORIO_FONTES = "/WEB-INF/jasper/fontes/";
//
//	public ReportsFontMapParser() {
//	}
//
//	/**
//	 * Le o arquivo XML de mapeamento das fontes e retorna o map que o jasper
//	 * reports pode ler.
//	 *
//	 *
//	 * @return the map.
//	 * @throws Exception
//	 *             Caso alguma excessao ocorra.
//	 */
//	public static Map<FontKey, PdfFont> readAndParseFontMap() throws Exception {
//		Map<FontKey, PdfFont> fontMap = null;
//
//		File xmlFile = new File(ReportsFontMapParser.class.getResource(FILE_REPORTS_FONTS_MAP).toURI());
//
//		ReportsFontXmlMirror rp = parseXmlFileReportFont(xmlFile);
//		fontMap = parseRpFileToMapReport(rp);
//
//		return fontMap;
//	}
//
//	/**
//	 * Faz o parser o aquivo XMl para a classe ReportsFont
//	 *
//	 * @param xmlFile
//	 * @return Parsed Class.
//	 * @throws Exception
//	 */
//	private static ReportsFontXmlMirror parseXmlFileReportFont(File xmlFile) throws Exception {
//		XStream xstream = new XStream(new DomDriver());
//		xstream.alias("reports-used-fonts", ReportsFontXmlMirror.class);
//		xstream.alias("font-report", FontReport.class);
//		xstream.aliasAttribute(ReportsFontXmlMirror.class, "fontsReport", "fonts");
//
//		ReportsFontXmlMirror rf = null;
//		rf = (ReportsFontXmlMirror) xstream.fromXML(new FileReader(xmlFile));
//
//		return rf;
//	}
//
//	/**
//	 * Método que faz o parser da classe do sistema para o mapa que o jasper
//	 * reports interpreta.
//	 *
//	 * @param parserClass
//	 * @return
//	 */
//	private static Map<FontKey, PdfFont> parseRpFileToMapReport(ReportsFontXmlMirror parserClass) {
//		final Map<FontKey, PdfFont> fontMap = new HashMap<FontKey, PdfFont>();
//
//		for (FontReport fontReport : parserClass.getFonts()) {
//			FontKey fontKey = new FontKey(fontReport.getName(), fontReport.isBold(), fontReport.isItalic());
//			PdfFont pdfFont = new PdfFont(DIRETORIO_FONTES + fontReport.getFileName(), "Cp1252", false);
//
//			fontMap.put(fontKey, pdfFont);
//		}
//
//		return fontMap;
//	}
}

