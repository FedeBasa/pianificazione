package it.soprasteria.pianificazione.v2.bean.upload;

import org.springframework.web.multipart.MultipartFile;

public class UploadEmployeeBean {

	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
