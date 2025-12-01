package com.ues.edu.servicios.impl.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.edu.interfaces.excel.IListaInventarioBarcosDTO;
import com.ues.edu.repositorios.IBarcoRepo;
import com.ues.edu.servicios.excel.IReporteInventarioBarcoExcel;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReporteTotalBarcosExcel implements IReporteInventarioBarcoExcel{

	@Autowired
	private IBarcoRepo totalBarco;
	@Override
	public void generarInventarioBarco(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Registros de Inventario de Barcos");

		Row filaTitulo = sheet.createRow(0);
		Cell celda = filaTitulo.createCell(0);
		celda.setCellValue("LISTADO GENERAL DE INVENTARIO TOTAL DE BARCOS");

		String[] columnas = { "ID", "NOMBRE BARCO", "AÑO CONSTRUCCION", "NAVIERA", "MODELO", "CAPACIDAD DE PASAJEROS", "TOTAL CAMAROTES"};

		Row headerRow = sheet.createRow(2);
		for (int i = 0; i < columnas.length; i++) {
			headerRow.createCell(i).setCellValue(columnas[i]);
		}
		List<IListaInventarioBarcosDTO> registros = totalBarco.consultaTotalBarcos();
		int rowldx = 3;
		for (IListaInventarioBarcosDTO r : registros) {
			Row row = sheet.createRow(rowldx++);
			row.createCell(0).setCellValue(r.getIdBarco());
			row.createCell(1).setCellValue(r.getNombreBarco());
			row.createCell(2).setCellValue(r.getAnioConstruccion());
			row.createCell(3).setCellValue(r.getNaviera());
			row.createCell(4).setCellValue(r.getModelo());
			row.createCell(5).setCellValue(r.getCapacidadPasajeros());
			row.createCell(6).setCellValue(r.getTotalCamarotes());
		}
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachement; filename=totalBarcos.xls");
		
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}

}
