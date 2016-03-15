package it.soprasteria.pianificazione.v2.digester;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.exception.DigestException;

public class ExcelEmployeeDigester {

	private static final Logger LOG = Logger.getLogger(ExcelEmployeeDigester.class);
	
	private List<String[]> content = new ArrayList<String[]>();
	
	private List<EmployeeBean> list = new ArrayList<EmployeeBean>();
	
	private int countRows;
	private int countRejected;
	private List<String> errorMessages = new ArrayList<String>();
	
	public void load(InputStream inputStream) throws DigestException {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook (inputStream)) {
	
			// TODO
			// leggere cella di controllo per verificare che il file caricato derivi dal template
			if (!checkWorkbook(workbook)) {
				throw new DigestException("Invalid input file");
			}

			XSSFSheet sheet = workbook.getSheetAt(0);
	
			Iterator<Row> rowIterator = sheet.iterator();
			
			// salto l'intestazione
			rowIterator.next();
	
			while(rowIterator.hasNext()) {
				
				XSSFRow row = (XSSFRow)rowIterator.next();
				String badgeNumber = row.getCell(0).getStringCellValue();
				String name = row.getCell(1).getStringCellValue();
				String surname = row.getCell(2).getStringCellValue();

				LOG.info("matricola: " + badgeNumber);
				LOG.info("name: " + name);
				LOG.info("surname: " + surname);
			}
		} catch(IOException e) {
			
			throw new DigestException(e);
		}
	}
	
	private boolean checkWorkbook(XSSFWorkbook  workbook) {

		XSSFSheet sheetCheck = workbook.getSheetAt(1);
		
		String checkValue = sheetCheck.getRow(0).getCell(0).getRawValue();
		
		if ("123".equals(checkValue)) {
			return true;
		}
		return false;
	}

	public void validate() {
		
		// TODO
		// implementare la validazione del contenuto
	}
	
	
}
