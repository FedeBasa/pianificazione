package it.soprasteria.pianificazione.v2.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.dao.Dao;
import it.soprasteria.pianificazione.v2.util.DateUtil;

public class ExportV2Service {

	@Autowired
	private Dao dao;
	
	public byte[] export(List<RecordV2Bean> record) throws InvalidFormatException, IOException, ParseException {

		ClassLoader classLoader = this.getClass().getClassLoader();
		
		XSSFWorkbook workbook = new XSSFWorkbook(classLoader.getResourceAsStream("excel/template.xlsx"));

		XSSFSheet sheet = workbook.getSheet("Foglio1");

		int i = 1;
		
		for(RecordV2Bean bean : record) {

			EmployeeBean employee = dao.getEmployee(bean.getBadgeNumber());
			if (employee != null) {
			
				Row row = sheet.createRow(i);
				
				Cell cell = row.createCell(0);
				cell.setCellValue(DateUtil.convertExportFormat(bean.getMonth()));
				Cell cell2 = row.createCell(1);
				cell2.setCellValue(org.apache.commons.lang.StringUtils.leftPad(bean.getBusinessUnit().toString(), 4, "0"));
				Cell cell3 = row.createCell(2);
				cell3.setCellValue(org.apache.commons.lang.StringUtils.leftPad(bean.getBadgeNumber().toString(), 6, "0"));
				Cell cell4 = row.createCell(3);
				cell4.setCellValue(bean.getCognome());
			
				Cell cell5 = row.createCell(4);
				if ("f".equalsIgnoreCase(bean.getActivityType())) {
					cell5.setCellValue("F");
				} else if ("r".equalsIgnoreCase(bean.getActivityType())) {
					cell5.setCellValue("R");
				}
			
				Cell cell6 = row.createCell(5);
				cell6.setCellValue(bean.getProjectDesc() + "(" + bean.getIdProject() + ")");
				Cell cell7 = row.createCell(6);
				cell7.setCellValue(bean.getPrice());
				Cell cell8 = row.createCell(7);
				cell8.setCellValue(bean.getCurrency());
				Cell cell9 = row.createCell(8);
				cell9.setCellValue(bean.getCons0());
				Cell cell10 = row.createCell(9);
				cell10.setCellValue(bean.getProd0());
				Cell cell11 = row.createCell(10);
				cell11.setCellValue(bean.getCons1());
				Cell cell12 = row.createCell(11);
				cell12.setCellValue(bean.getProd1());
				Cell cell13 = row.createCell(12);
				cell13.setCellValue(bean.getCons2());
				Cell cell14 = row.createCell(13);
				cell14.setCellValue(bean.getProd2());
				
				i++;
			}
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		return output.toByteArray();
	}

	public byte[] exportTerzeParti(List<RecordV2Bean> record) throws InvalidFormatException, IOException, ParseException {

		ClassLoader classLoader = this.getClass().getClassLoader();
		
		XSSFWorkbook workbook = new XSSFWorkbook(classLoader.getResourceAsStream("excel/template_terzeparti.xlsx"));

		XSSFSheet sheet = workbook.getSheet("Foglio1");

		int i = 1;
		
		for(RecordV2Bean bean : record) {

			EmployeeBean employee = dao.getEmployee(bean.getBadgeNumber());
			if (employee == null) {

				Row row = sheet.createRow(i);
				
				Cell cell = row.createCell(0);
				cell.setCellValue(DateUtil.convertExportFormat(bean.getMonth()));
				Cell cell2 = row.createCell(1);
				cell2.setCellValue(org.apache.commons.lang.StringUtils.leftPad(bean.getBusinessUnit().toString(), 4, "0"));
				Cell cell3 = row.createCell(2);
				cell3.setCellValue(org.apache.commons.lang.StringUtils.leftPad(bean.getBadgeNumber().toString(), 6, "0"));
				Cell cell4 = row.createCell(3);
				cell4.setCellValue(bean.getCognome());
			
				Cell cell5 = row.createCell(4);
				if ("f".equalsIgnoreCase(bean.getActivityType())) {
					cell5.setCellValue("F");
				} else if ("r".equalsIgnoreCase(bean.getActivityType())) {
					cell5.setCellValue("R");
				}
			
				Cell cell6 = row.createCell(5);
				cell6.setCellValue(bean.getProjectDesc() + "(" + bean.getIdProject() + ")");
				Cell cell7 = row.createCell(6);
				cell7.setCellValue(bean.getPrice());
				Cell cell8 = row.createCell(7);
				cell8.setCellValue(bean.getCurrency());
				Cell cell9 = row.createCell(8);
				cell9.setCellValue(bean.getCons0());
				Cell cell10 = row.createCell(9);
				cell10.setCellValue(bean.getProd0());
				Cell cell11 = row.createCell(10);
				cell11.setCellValue(bean.getCons1());
				Cell cell12 = row.createCell(11);
				cell12.setCellValue(bean.getProd1());
				Cell cell13 = row.createCell(12);
				cell13.setCellValue(bean.getCons2());
				Cell cell14 = row.createCell(13);
				cell14.setCellValue(bean.getProd2());
				Cell cell15 = row.createCell(14);
				cell15.setCellValue(bean.getCost());
				
				i++;
			}
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		return output.toByteArray();
	}

}
