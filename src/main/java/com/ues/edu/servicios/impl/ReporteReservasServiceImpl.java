package com.ues.edu.servicios.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.ues.edu.repositorios.IReservaRepo;
import com.ues.edu.servicios.IReporteReservasService;
import com.ues.edu.dto.ReservasDTO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteReservasServiceImpl implements IReporteReservasService {

	@Autowired
	private IReservaRepo reservaRepo;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public List<ReservasDTO> obtenerDatosParaReporte() {
		try {
			List<Object[]> resultados = reservaRepo.listadoReservarNative();
			System.out.println("Resultados raw: " + resultados.size() + " registros");

			List<ReservasDTO> reservas = new ArrayList<>();

			for (Object[] resultado : resultados) {
				System.out.println("Fila: " + Arrays.toString(resultado));

				ReservasDTO dto = new ReservasDTO((java.sql.Date) resultado[0], // fecha_salida - mantener como
																				// java.sql.Date
						(String) resultado[1], // barco
						(String) resultado[2], // puerto_salida
						(java.sql.Date) resultado[3], // fecha_regreso - mantener como java.sql.Date
						((Number) resultado[4]).intValue(), // camarotes_disponibles
						((Number) resultado[5]).longValue() // total_reservas
				);

				reservas.add(dto);
			}

			System.out.println("Datos mapeados: " + reservas.size() + " registros");
			return reservas;

		} catch (Exception e) {
			System.err.println("Error al mapear datos: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public byte[] generarReporteReservasPDF() {
		try {
			System.out.println("=== INICIANDO GENERACIÓN DE PDF DESDE .JRXML ===");

			// 1. Obtener datos
			List<ReservasDTO> reservas = obtenerDatosParaReporte();
			System.out.println("Datos obtenidos: " + reservas.size() + " registros");

			// Mostrar datos para debug
			if (!reservas.isEmpty()) {
				System.out.println("Primer registro:");
				ReservasDTO primer = reservas.get(0);
				System.out.println(" - Fecha Salida: " + primer.getFechaSalida());
				System.out.println(" - Barco: " + primer.getBarco());
				System.out.println(" - Puerto: " + primer.getPuertoSalida());
				System.out.println(" - Fecha Regreso: " + primer.getFechaRegreso());
				System.out.println(" - Camarotes: " + primer.getCamarotesDisponibles());
				System.out.println(" - Total Reservas: " + primer.getTotalReservas());
			}

			// 2. Cargar y compilar el .jrxml
			Resource resource = resourceLoader.getResource("classpath:reportes/reservas.jrxml");
			System.out.println("Archivo .jrxml existe: " + resource.exists());

			if (!resource.exists()) {
				throw new RuntimeException("Archivo reservas.jrxml no encontrado en: classpath:reportes/");
			}

			InputStream jrxmlStream = resource.getInputStream();
			System.out.println("Compilando .jrxml...");

			// Compilar el reporte
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
			System.out.println("Reporte compilado correctamente");

			// 3. Crear datasource
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reservas);
			System.out.println("DataSource creado con " + reservas.size() + " registros");

			// 4. Parámetros
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("tituloReporte", "Reporte de Reservas - CRUISEMANAGER");
			parameters.put("fechaGeneracion", new java.util.Date());
			parameters.put("totalRegistros", reservas.size());

			// 5. Llenar reporte
			System.out.println("Llenando reporte...");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			System.out.println("Reporte llenado correctamente");

			// 6. Exportar a PDF
			System.out.println("Exportando a PDF...");
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("PDF generado: " + pdf.length + " bytes");

			return pdf;

		} catch (Exception e) {
			System.err.println("ERROR en generarReporteReservasPDF: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
		}
	}
}