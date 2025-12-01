package com.ues.edu.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ues.edu.servicios.IReporteBarcosService;
import com.ues.edu.servicios.IReporteReservasService;

import java.time.LocalDate;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private IReporteReservasService reporteReservasService;
    
    @Autowired
    private IReporteBarcosService reporteBarcosService;

    // Página principal de reportes
    @GetMapping("")
    public String mostrarPaginaReportes(Model model) {
        model.addAttribute("titulo", "Reportes - CRUISEMANAGER");
        model.addAttribute("modulo", "Reportes");
        return "reportes/index";
    }

    @GetMapping("/reservas")
    public ResponseEntity<?> generarReporteReservas() {
        try {
            System.out.println("=== SOLICITUD DE REPORTE RECIBIDA ===");
            
            byte[] pdfBytes = reporteReservasService.generarReporteReservasPDF();
            
            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: El PDF generado está vacío");
            }
            
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            String filename = "reporte_reservas_" + LocalDate.now() + ".pdf";
            
            System.out.println("=== PDF ENVIADO AL CLIENTE ===");
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);
                    
        } catch (Exception e) {
            System.err.println("ERROR en controlador: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/barcos")
    public ResponseEntity<ByteArrayResource> generarReporteBarcos() {
        try {
            System.out.println("=== SOLICITUD REPORTE BARCOS ===");
            
            byte[] pdfBytes = reporteBarcosService.generarReporteBarcosPDF();
            
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            String filename = "reporte_barcos_" + LocalDate.now() + ".pdf";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);
                    
        } catch (Exception e) {
            System.err.println("ERROR en controlador barcos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cruceros")
    public ResponseEntity<String> generarReporteCruceros() {
        return ResponseEntity.badRequest().body("Reporte de cruceros en desarrollo");
    }
}