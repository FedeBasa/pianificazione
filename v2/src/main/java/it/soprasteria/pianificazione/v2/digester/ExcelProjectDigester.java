package it.soprasteria.pianificazione.v2.digester;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.exception.DigestException;

public class ExcelProjectDigester implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String[]> content = new ArrayList<String[]>();
	
	private List<ProjectBean> projectList = new ArrayList<ProjectBean>();
	
	private List<String> infoMessages = new ArrayList<String>();
	
	private int countRows; 
	
	public void load(InputStream inputStream) throws DigestException {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook (inputStream)) {

			XSSFSheet sheet = workbook.getSheetAt(0);
	
			Iterator<Row> rowIterator = sheet.iterator();
			
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
	
			while(rowIterator.hasNext()) {
				
				XSSFRow row = (XSSFRow)rowIterator.next();
				String bu = row.getCell(1).getStringCellValue().substring(0, 3);
				String customer = row.getCell(2).getStringCellValue();
				String project =  row.getCell(3).getStringCellValue();
				String idProject  = row.getCell(42).getRawValue();
				
				if(row.getCell(20).getStringCellValue().equals("Y")){
					
				String[] rowContent = new String[4];
				
				rowContent[0] = bu;
				rowContent[1] = customer;
				rowContent[2] = project;
				rowContent[3] = idProject;

				this.content.add(rowContent);
				
				this.countRows++;
				}
			}
		} catch(IOException e) {
			
			throw new DigestException(e);
		}
	}
	
	public void validate() {

		this.projectList.clear();

		for(String[] rowContent : this.content) {
			
			this.projectList.add(ProjectBean.build(Long.parseLong(rowContent[3]),rowContent[2],rowContent[1],Integer.parseInt(rowContent[0])));
		}
		
		this.infoMessages.add(this.projectList.size() + " elementi pronti per il salvataggio");
		
	}
	
	public List<ProjectBean> getList() {
		return projectList;
	}
	
	public List<String> getInfoMessages() {
		return infoMessages;
	}
}