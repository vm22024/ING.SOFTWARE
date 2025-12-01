package com.ues.edu.servicios.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.ues.edu.repositorios.IBarcoRepo;
import com.ues.edu.servicios.IReporteBarcosService;
import com.ues.edu.dto.BarcosDTO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteBarcosServiceImpl implements IReporteBarcosService {

    @Autowired
    private IBarcoRepo barcoRepo;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public List<BarcosDTO> obtenerDatosParaReporte() {
        try {
            List<Object[]> resultados = barcoRepo.listadoBarcosNative();
            System.out.println("Resultados raw: " + resultados.size() + " registros");
            
            List<BarcosDTO> barcos = new ArrayList<>();
            
            for (Object[] resultado : resultados) {
                System.out.println("Fila: " + Arrays.toString(resultado));
                
                BarcosDTO dto = new BarcosDTO(
                    (String) resultado[0], // barco
                    ((Number) resultado[1]).intValue(), // construccion - como Integer
                    (String) resultado[2], // modelo
                    (String) resultado[3], // naviera
                    (String) resultado[4]  // pais
                );
                
                barcos.add(dto);
            }
            
            System.out.println("Barcos mapeados: " + barcos.size() + " registros");
            
            if (!barcos.isEmpty()) {
                System.out.println("Primer barco:");
                BarcosDTO primer = barcos.get(0);
                System.out.println(" - Barco: " + primer.getBarco());
                System.out.println(" - Año Construcción: " + primer.getConstruccion());
                System.out.println(" - Modelo: " + primer.getModelo());
                System.out.println(" - Naviera: " + primer.getNaviera());
                System.out.println(" - País: " + primer.getPais());
            }
            
            return barcos;
        } catch (Exception e) {
            System.err.println("Error al obtener datos de barcos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public byte[] generarReporteBarcosPDF() {
        try {
            System.out.println("=== GENERANDO REPORTE DE BARCOS ===");
            
            // 1. Obtener datos
            List<BarcosDTO> barcos = obtenerDatosParaReporte();
            System.out.println("Datos para reporte: " + barcos.size() + " barcos");

            // 2. Cargar y compilar el .jrxml
            Resource resource = resourceLoader.getResource("classpath:reportes/barcos.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("Archivo barcos.jrxml no encontrado en classpath:reportes/");
            }
            
            InputStream jrxmlStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            // 3. Crear datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(barcos);

            // 4. Parámetros
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("tituloReporte", "Reporte de Barcos - CRUISEMANAGER");
            parameters.put("fechaGeneracion", new java.util.Date());
            parameters.put("totalBarcos", barcos.size());

            // 5. Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // 6. Exportar a PDF
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            System.out.println("PDF de barcos generado: " + pdf.length + " bytes");
            
            return pdf;

        } catch (Exception e) {
            System.err.println("ERROR en generarReporteBarcosPDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al generar reporte de barcos: " + e.getMessage(), e);
        }
    }
}