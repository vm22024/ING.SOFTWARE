package com.ues.edu.servicios.impl.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.edu.interfaces.excel.IListaReservasCruceroDTO;
import com.ues.edu.repositorios.ICruceroProgramadoRepo;
import com.ues.edu.servicios.excel.IReporteCruceroProgramadoExcel;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class ReporteCruceroProgramadoExcelImp implements IReporteCruceroProgramadoExcel{

	@Autowired
	private ICruceroProgramadoRepo listaCrucero;
	
	@Override
	public void generarExcelCruceroProgramado(HttpServletResponse response) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet =hssfWorkbook.createSheet("Registro de Cruceros Programados");
		
		Row filaTitulo = sheet.createRow(0);
		Cell celda = filaTitulo.createCell(0);
		celda.setCellValue("LISTADO DE CRUCEROS PROGRAMADOS");


		String[] columnas = { "ID", "BARCO", "NAVIERO", "PUERTO ORIGEN", "PUERTO DESTINO", "FECHA SALIDA", "FECHA REGRESO", "CAMAROTES DISPONIBLES", "PASAJEROS REGISTRADOS",
				"CAPACIDAD DE PASAJEROS", "OCUPACION (%)"};

		Row headerRow = sheet.createRow(2);
		for (int i = 0; i < columnas.length; i++) {
			headerRow.createCell(i).setCellValue(columnas[i]);
		}
		
		List<IListaReservasCruceroDTO> registros = listaCrucero.consultaCruceroProgramado();
		int rowldx =3;
		for(IListaReservasCruceroDTO r: registros) {
			Row row = sheet.createRow(rowldx++);
			row.createCell(0).setCellValue(r.getIdCrucero());
			row.createCell(1).setCellValue(r.getNombreBarco());
			row.createCell(2).setCellValue(r.getNaviera());
			row.createCell(3).setCellValue(r.getPuertoOrigen());
			row.createCell(4).setCellValue(r.getPuertoDestino());
			row.createCell(5).setCellValue(r.getFechaSalida());
			row.createCell(6).setCellValue(r.getFechaRegreso());
			row.createCell(7).setCellValue(r.getCamarotesDisponibles());
			row.createCell(8).setCellValue(r.getPasajerosRegistrados());
			row.createCell(9).setCellValue(r.getCapacidadPasajeros());
			row.createCell(10).setCellValue(r.getOcupacionPorcentaje());
			
		}
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachement; filename=crucerosProgramados.xls");
		
		ServletOutputStream ops = response.getOutputStream();
		hssfWorkbook.write(ops);
		hssfWorkbook.close();
		ops.close();
	}

}
