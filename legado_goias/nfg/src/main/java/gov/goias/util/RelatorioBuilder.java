package gov.goias.util;

//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.export.JRPdfExporter;
//import net.sf.jasperreports.engine.util.JRLoader;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author henrique-rh
 * @since 22/09/2014
 */
public class RelatorioBuilder {

//    private Map<String, Object> parametros = new HashMap<String, Object>();
//    private String path;
//    private JRDataSource dataSource;
//
//    public RelatorioBuilder(String path, List listDataSource) {
//        this.dataSource = new JRBeanCollectionDataSource(listDataSource);
//        this.path = path;
//    }
//
//    public void addParametro(String name, Object object) {
//        this.parametros.put(name, object);
//    }
//
//    public void addParametros(Map<String, Object> map) {
//        this.parametros.putAll(map);
//    }
//
//    @SuppressWarnings("rawtypes")
//    public void exportar(String destFileName) throws JRException, FileNotFoundException {
//
//        JRAbstractExporter exporter = new JRPdfExporter();
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(new FileInputStream(new File(path)), parametros, dataSource);
//
//        try {
//            exporter.setParameter(JRExporterParameter.FONT_MAP, ReportsFontMapParser.readAndParseFontMap());
//        } catch (Exception e) {
//            throw new JRException(e);
//        }
//
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFileName);
//        exporter.exportReport();
//    }
}
