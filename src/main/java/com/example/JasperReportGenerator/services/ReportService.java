package com.example.JasperReportGenerator.services;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JasperReportGenerator.commons.*;
//import com.example.demospringbootjasper.enums.TipoReporteEnum;
import com.example.JasperReportGenerator.models.*;


import net.sf.jasperreports.engine.JRException;


@Service
public class ReportService {

	@Autowired
	private JasperReportManager reportManager;

	@Autowired
	private DataSource dataSource;
	
	//ReportDTO obtenerReport(Map<String, Object> params);
	
	public ReportDTO obtenerReport(Map<String, Object> params, String report, String tipo, String conex, String BaseDatos , String user, String clave)
			throws JRException, IOException, SQLException, ClassNotFoundException {
		String fileName = report;
		ReportDTO dto = new ReportDTO();
		String extension = tipo.equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx"
				: ".pdf";
		dto.setFileName(fileName + extension);
		//System.out.println(dataSource.getConnection());
		ByteArrayOutputStream stream = reportManager.export(fileName, tipo, params, report,  conex,  BaseDatos ,  user,  clave
				);
		
		byte[] bs = stream.toByteArray();
		dto.setStream(new ByteArrayInputStream(bs));
		dto.setLength(bs.length);

		return dto;
	}
}
