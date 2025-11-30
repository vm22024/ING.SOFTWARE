package com.ues.edu.servicios.impl.excel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.edu.interfaces.excel.IListaReservaCruceroDTO;
import com.ues.edu.repositorios.IReservaRepo;
import com.ues.edu.servicios.excel.IReporteReservaCrucero;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReporteReservaCruceroExcel implements IReporteReservaCrucero {

	@Autowired
	private IReservaRepo reservaCrucero;

	@Override
	public void generarReservaCrucero(HttpServletResponse response) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Registros de Reservas");

		Row filaTitulo = sheet.createRow(0);
		Cell celda = filaTitulo.createCell(0);
		celda.setCellValue("LISTADO GENERAL DE RESERVAS");

		String[] columnas = { "ID", "PASAJERO", "EMAIL", "TELEFONO", "CRUCERO", "BARCO", "FECHA RESERVA",
				"CANTIDAD PERSONAS", "TOTAL CAMAROTES" };

		Row headerRow = sheet.createRow(2);
		for (int i = 0; i < columnas.length; i++) {
			headerRow.createCell(i).setCellValue(columnas[i]);
		}

		List<IListaReservaCruceroDTO> registros = reservaCrucero.consultaCrucero();
		int rowldx = 3;

		for (IListaReservaCruceroDTO r : registros) {
			Row row = sheet.createRow(rowldx++);
			row.createCell(0).setCellValue(r.getIdReserva());
			row.createCell(1).setCellValue(r.getPasajero());
			row.createCell(2).setCellValue(r.getEmail());
			row.createCell(3).setCellValue(r.getTelefono());
			row.createCell(4).setCellValue(r.getIdCrucero());
			row.createCell(5).setCellValue(r.getBarco());
			row.createCell(6).setCellValue(r.getFechaReserva());
			row.createCell(7).setCellValue(r.getCantidadPersonas());
			row.createCell(8).setCellValue(r.getCantidadCamarotes());
			
		}
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachement; filename=ReservasPasajeros.xls");
		
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}

}
