package it.soprasteria.pianificazione.v2.digester;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.exception.DigestException;

public class ExcelEmployeeDigester implements Serializable {

	private static final Logger LOG = Logger.getLogger(ExcelEmployeeDigester.class);
	
	private List<String[]> content = new ArrayList<>();
	
	private List<EmployeeBean> list = new ArrayList<>();
	
	private List<String> infoMessages = new ArrayList<>();
	
	public void load(InputStream inputStream) throws DigestException {
		
		try (HSSFWorkbook workbook = new HSSFWorkbook (inputStream)) {
	
			HSSFSheet sheet = workbook.getSheetAt(0);
	
			Iterator<Row> rowIterator = sheet.iterator();
			
			// salto l'intestazione
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
	
			while(rowIterator.hasNext()) {
				
				HSSFRow row = (HSSFRow)rowIterator.next();
				String surname = row.getCell(0).getStringCellValue();
				String name = row.getCell(1).getStringCellValue();
				String badgeNumber = row.getCell(2).getStringCellValue();
				
				if (badgeNumber != null && badgeNumber.length() > 0) {

					String[] rowContent = new String[3];
					rowContent[0] = surname;
					rowContent[1] = name;
					rowContent[2] = badgeNumber;
	
					this.content.add(rowContent);
				}				
			}
		} catch(IOException e) {
			
			throw new DigestException(e);
		}
	}
	
	public void validate(String username) {

		this.list.clear();

		for(String[] rowContent : this.content) {
			
			// TODO
			// implementare la validazione del contenuto
			this.list.add(EmployeeBean.build(rowContent[2], rowContent[1], rowContent[0], username));
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