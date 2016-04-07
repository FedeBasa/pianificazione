package it.soprasteria.pianificazione.v2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat SDF_MONTH = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat SDF_EXPORT = new SimpleDateFormat("MM/yyyy");
	public static int previousMonth(int month) {
		
		try {
			Date parsedMonth = SDF_MONTH.parse(String.valueOf(month));
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsedMonth);
			cal.add(Calendar.MONTH, -1);
			
			return Integer.parseInt(SDF_MONTH.format(cal.getTime()));
			
		} catch(ParseException e) {
			return 0;
		}
	}

	public static int addMonth(int month, int num) {
		
		try {
			Date parsedMonth = SDF_MONTH.parse(String.valueOf(month));
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsedMonth);
			cal.add(Calendar.MONTH, num);
			
			return Integer.parseInt(SDF_MONTH.format(cal.getTime()));
			
		} catch(ParseException e) {
			return 0;
		}
	}

	public static int nextMonth(int month) {
		return addMonth(month, 1);
	}
	
	public static boolean checkMonth(int mese) {

		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
	
			int currentMonth = cal.get(Calendar.MONTH);
			int currentYear = cal.get(Calendar.YEAR);
	
			Date parsedMonth = SDF_MONTH.parse(String.valueOf(mese));
			Calendar calMonth = Calendar.getInstance();
			calMonth.setTime(parsedMonth);
			int month = calMonth.get(Calendar.MONTH);
			int year = calMonth.get(Calendar.YEAR);
	
			if (currentMonth == month && currentYear == year) {
				return true;
			}
		} catch(ParseException e) {
			// return false
		}
		return false;
	}
	
	
	public static String exportFormat(int month) throws ParseException{
		
		Date parsedMonth = SDF_MONTH.parse(String.valueOf(month));
		
		return (SDF_MONTH.format(SDF_MONTH.toString()));
	}
	
}