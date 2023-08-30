package com.example.JasperReportGenerator.commons;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JasperReportManager {

	private static final String REPORT_FOLDER = "reports";

	private static final String JASPER = ".jasper";

	public ByteArrayOutputStream export(String fileName, String tipoReporte, Map<String, Object> params,
			String report, String conex, String BaseDatos , String user, String clave) throws JRException, IOException, ClassNotFoundException, SQLException {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ClassPathResource resource = new ClassPathResource(REPORT_FOLDER + File.separator + fileName + JASPER);
        String url = "jdbc:sqlserver://"+conex+";databaseName="+BaseDatos+";encrypt=true;trustServerCertificate=true";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection connection = DriverManager.getConnection(url, user, clave);
		InputStream inputStream = resource.getInputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, connection);
		if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.EXCEL.toString())) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.PDF.toString())) {
			JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		} else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.CSV.toString())) {
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
			exporter.exportReport();
		// } else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.HTML.toString())) {
		// 	JRHtmlExporter exporter = new JRHtmlExporter();
		// 	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		// 	exporter.setExporterOutput(new SimpleHtmlExporterOutput(stream));
		// 	exporter.exportReport();
		} else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.RTF.toString())) {
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
			exporter.exportReport();
		} else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.DOCX.toString())) {
			JRDocxExporter exporter = new JRDocxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
			exporter.exportReport();
		} else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.ODT.toString())) {
			JROdtExporter exporter = new JROdtExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
			exporter.exportReport();
		} else if (tipoReporte.equalsIgnoreCase(TipoReporteEnum.PPTX.toString())) {
			JRPptxExporter exporter = new JRPptxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
			exporter.exportReport();
		} else {
			// Tipo de informe no reconocido, manejo de error o acción por defecto
			JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		}
		connection.close();
		return stream;
	}
	
	

}
