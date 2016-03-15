package it.soprasteria.pianificazione.v2.util;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.digester.ExcelEmployeeDigester;

public class SessionHelper {

	private static final String USER_SESSION_KEY = "session.user";
	private static final String EMPLOYEE_DIGESTER_KEY = "session.digester.employee";
	
	public static void storeUser(UserBean user) {
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();
	    session.setAttribute(USER_SESSION_KEY, user);
	}
	
	public static UserBean getUser(HttpSession session) {
		
		return (UserBean)session.getAttribute(USER_SESSION_KEY);	
	}
	
	public static UserBean getUser() {
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();
	    
	    return getUser(session);
	}

	public static void logout() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();
	    session.invalidate();
	}
	
	public static void storeEmployeeDigester(ExcelEmployeeDigester digester) {
	
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();
	    session.setAttribute(EMPLOYEE_DIGESTER_KEY, digester);
	}
	
	public static ExcelEmployeeDigester getEmployeeDigester() {
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();

	    return (ExcelEmployeeDigester)session.getAttribute(EMPLOYEE_DIGESTER_KEY);
	}

	public static void clearEmployeeDigester() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();

		session.removeAttribute(EMPLOYEE_DIGESTER_KEY);
	}
}
