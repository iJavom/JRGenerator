package com.example.JasperReportGenerator.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.JasperReportGenerator.services.ReportService;

import com.example.JasperReportGenerator.commons.TipoReporteEnum;
import com.example.JasperReportGenerator.models.ReportDTO;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private ReportService reportService;
	/*
	@GetMapping(path = "/download")
	public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params)
			throws JRException, IOException, SQLException, ClassNotFoundException {
		ReportDTO dto = reportService.obtenerReport(params);

		InputStreamResource streamResource = new InputStreamResource(dto.getStream());
		MediaType mediaType = null;
		if (params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			mediaType = MediaType.APPLICATION_PDF;
		} 

		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
	}
	*/

	@GetMapping(path = "/download")
	public ResponseEntity<Resource> downloadGet(@RequestParam Map<String, Object> params,
											@RequestHeader("dataBase") String database,
                                             @RequestHeader("conex") String conex,
                                             @RequestHeader("user") String user,
                                             @RequestHeader("clave") String clave,
                                             @RequestHeader("reporte") String reporte,
                                             @RequestHeader("tipo") String tipo)
			throws JRException, IOException, SQLException, ClassNotFoundException {
		
			System.out.println(database);
        	System.out.println(conex);
        	System.out.println(user);
        	System.out.println(clave);
        	System.out.println(reporte);
        	System.out.println(tipo);
			System.out.println("Contenido del objeto requestParams:");
			System.out.println(params);

            ReportDTO dto = reportService.obtenerReport(params, reporte,tipo, conex,  database ,  user,  clave);

		InputStreamResource streamResource = new InputStreamResource(dto.getStream());
		MediaType mediaType = null;
		if (tipo.equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			mediaType = MediaType.APPLICATION_PDF;
		} 

		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
	}
	
	@PostMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestBody Map<String, Object> requestParams,
                                             @RequestHeader("dataBase") String database,
                                             @RequestHeader("conex") String conex,
                                             @RequestHeader("user") String user,
                                             @RequestHeader("clave") String clave,
                                             @RequestHeader("reporte") String reporte,
                                             @RequestHeader("tipo") String tipo) {
        try {
        	//String conex, String BaseDatos , String user, String clave
            // Llama al servicio para obtener el reporte
        	System.out.println(database);
        	System.out.println(conex);
        	System.out.println(user);
        	System.out.println(clave);
        	System.out.println(reporte);
        	System.out.println(tipo);
			System.out.println("Contenido del objeto requestParams:");
			System.out.println(requestParams);

            ReportDTO dto = reportService.obtenerReport(requestParams, reporte,tipo, conex,  database ,  user,  clave);
            
            InputStreamResource streamResource = new InputStreamResource(dto.getStream());
            MediaType mediaType = null;
            if (tipo.equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            } else {
                mediaType = MediaType.APPLICATION_PDF;
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                    .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);

        } catch (JRException | IOException | SQLException | ClassNotFoundException e) {
            // Manejo de excepciones en caso de errores
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
	}
}
