package it.soprasteria.pianificazione.v2.digester;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.exception.DigestException;

public class ExcelProjectDigester implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String[]> content = new ArrayList<>();

	private List<ProjectBean> projectList = new ArrayList<>();

	private List<String> infoMessages = new ArrayList<>();

	public void load(InputStream inputStream) throws DigestException {

		try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {

			HSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				String project;
				HSSFRow row = (HSSFRow) rowIterator.next();
				String bu = row.getCell(1).getStringCellValue().substring(0, 3);

				String customer = row.getCell(2).getStringCellValue();

				project = row.getCell(3).getStringCellValue();

				HSSFCell idProjectCell = row.getCell(42);
				if (idProjectCell != null) {

					String idProject = String.valueOf((int)idProjectCell.getNumericCellValue());
	
					if ("Y".equals(row.getCell(20).getStringCellValue())) {
	
						String[] rowContent = new String[4];
	
						rowContent[0] = bu;
						rowContent[1] = customer;
						rowContent[2] = project;
						rowContent[3] = idProject;
	
						this.content.add(rowContent);
	
					}
				}
			}
		} catch (IOException e) {

			throw new DigestException(e);

		}
	}

	public void validate(String username) {

		this.projectList.clear();

		for (String[] rowContent : this.content) {

			this.projectList.add(ProjectBean.build(Long.parseLong(rowContent[3]), rowContent[2], rowContent[1], Integer.parseInt(rowContent[0]), username));
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