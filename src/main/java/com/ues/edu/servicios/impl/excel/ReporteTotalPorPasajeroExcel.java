package com.ues.edu.servicios.impl.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.edu.interfaces.excel.IListaTotalPasajerosDTO;
import com.ues.edu.repositorios.IPasajeroRepo;
import com.ues.edu.servicios.excel.IReporteTotalPorPasajero;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReporteTotalPorPasajeroExcel implements IReporteTotalPorPasajero{

	@Autowired
	private IPasajeroRepo totalPasajero;
	@Override
	public void GenerarTotalporPasajeros(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Registros de Total Viajes por Pasajero");

		Row filaTitulo = sheet.createRow(0);
		Cell celda = filaTitulo.createCell(0);
		celda.setCellValue("LISTADO GENERAL DE REGISTRO DE VIAJES POR PASAJERO");

		String[] columnas = { "PASAJERO", "TOTAL VIAJES", "TOTAL PERSONAS ATENDIAS"};
		
		Row headerRow = sheet.createRow(2);
		for (int i = 0; i < columnas.length; i++) {
			headerRow.createCell(i).setCellValue(columnas[i]);
		}
		
		List<IListaTotalPasajerosDTO> registros = totalPasajero.consultaTotalPasajeros();
		int rowldx = 3;
		for(IListaTotalPasajerosDTO r: registros) {
			Row row = sheet.createRow(rowldx++);
			row.createCell(0).setCellValue(r.getPasajero());
			row.createCell(1).setCellValue(r.getTotalViajes());
			row.createCell(2).setCellValue(r.getTotalPersonasAtendidas());
		}
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachement; filename=TotalPasajeros.xls");
		
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
		
				
	}

}
