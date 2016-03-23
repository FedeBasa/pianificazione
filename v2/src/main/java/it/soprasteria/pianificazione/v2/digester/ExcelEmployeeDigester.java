package it.soprasteria.pianificazione.v2.digester;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

public class ExcelEmployeeDigester implements Serializable {

	private static final Logger LOG = Logger.getLogger(ExcelEmployeeDigester.class);
	
	private List<String[]> content = new ArrayList<String[]>();
	
	private List<EmployeeBean> list = new ArrayList<EmployeeBean>();
	
	private int countRows;
	private int countRejected;
	
	private List<String> infoMessages = new ArrayList<String>();
	private List<String> errorMessages = new ArrayList<String>();
	
	public void load(InputStream inputStream) throws DigestException {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook (inputStream)) {
	
			// TODO
			// leggere cella di controllo per verificare che il file caricato derivi dal template
//			if (!checkWorkbook(workbook)) {
//				throw new DigestException("Invalid input file");
//			}

			XSSFSheet sheet = workbook.getSheetAt(0);
	
			Iterator<Row> rowIterator = sheet.iterator();
			
			// salto l'intestazione
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
	
			while(rowIterator.hasNext()) {
				
				XSSFRow row = (XSSFRow)rowIterator.next();
				String surname = row.getCell(0).getStringCellValue();
				String name = row.getCell(1).getStringCellValue();
				String badgeNumber = row.getCell(2).getRawValue();

				String[] rowContent = new String[3];
				rowContent[0] = surname;
				rowContent[1] = name;
				rowContent[2] = badgeNumber;

				this.content.add(rowContent);
				
				this.countRows++;
			}
		} catch(IOException e) {
			
			throw new DigestException(e);
		}
	}
	
	public void validate() {

		this.list.clear();

		for(String[] rowContent : this.content) {
			
			// TODO
			// implementare la validazione del contenuto
			this.list.add(EmployeeBean.build(rowContent[2], rowContent[1], rowContent[0]));
		}
		
		this.infoMessages.add(this.list.size() + " elementi pronti per il salvataggio");
		
	}
	
	public List<EmployeeBean> getList() {
		return list;
	}
	
	public List<String> getInfoMessages() {
		return infoMessages;
	}
}
