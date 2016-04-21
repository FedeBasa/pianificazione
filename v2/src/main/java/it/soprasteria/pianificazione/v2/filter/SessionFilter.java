package it.soprasteria.pianificazione.v2.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

public class SessionFilter implements Filter {
	
	private static final Logger LOG = Logger.getLogger(SessionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String requestURI = httpServletRequest.getRequestURI();
		
		if (requestURI.endsWith("/login") || requestURI.endsWith("/userprincipal") || requestURI.endsWith(".css") || requestURI.endsWith(".js")) {
			
			chain.doFilter(request, response);
			return;
		}
		
		UserBean user = SessionHelper.getUser(httpServletRequest.getSession());
		if (user == null) {
			
			Principal userPrincipal = httpServletRequest.getUserPrincipal();
			if (userPrincipal != null) {

				LOG.info("###### USER PRINCIPAL: " + userPrincipal.getName());
				// sso ok
				// verifico internamente se l'utenza ha un profilo
				
				((HttpServletResponse)response).sendRedirect("/v2/userprincipal");
				return;				
				
			} else {
			
				// l'utente non si è mai loggato
				// lo rimando sulla schermata di login
				
				((HttpServletResponse)response).sendRedirect("/v2/login");
				return;
			} 
		}
		
				
		if(requestURI.endsWith("changepw")||requestURI.endsWith("logout")||requestURI.endsWith("saveNewPassword")){

			chain.doFilter(request, response);
			return;
		}
		
		if (requestURI.startsWith("/v2/admin/")) {
			
			if (!"admin".equalsIgnoreCase(user.getProfilo())) {
				((HttpServletResponse)response).sendRedirect("/v2/home");
				return;
			}
		}
		
		if(user.getFirstlogin()==0){
			LOG.debug("USERSTATUS :" + user.getFirstlogin());
			((HttpServletResponse)response).sendRedirect("/v2/changepw");
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
	
}