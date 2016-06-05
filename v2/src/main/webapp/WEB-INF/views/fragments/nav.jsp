<%@page import="it.soprasteria.pianificazione.v2.util.SessionHelper"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/home" var="urlHome" />
<spring:url value="/admin/excel/upload/employee" var="urlUploadEmployee" />
<spring:url value="/admin/excel/upload/project" var="urlUploadProjects" />
<spring:url value="/admin/gestione_mese" var="urlApprova" />
<spring:url value="/admin/impersonate/stop" var="urlStopImpersonificazione" />
<spring:url value="/changepw" var="urlChangepwd" />
<spring:url value="/logout" var="urlLogout" />

<header>
	<nav class="navbar-custom">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Pianificazione Risorse</a>
			</div>
			<%
			if (SessionHelper.getUser(session) != null) {
			%>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="${urlHome}">Home</a></li>
		           	<li class="dropdown">
		           	<%if(SessionHelper.getUser(session).getProfilo().equals("admin")){ %>
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Amministrazione <span class="caret"></span></a>
						<ul class="dropdown-menu open">
						    <li><a class="" href="${urlUploadEmployee}">Caricamento Risorse</a></li>
						    <li><a class="" href="${urlUploadProjects}">Caricamento Progetti</a></li>
						    <li><a class="" href="${urlApprova}">Gestione V2</a></li>
						</ul>
					</li>
			 		<%
			 		} 
			 		if (request.getUserPrincipal() == null) { 
			 		%>
			 		<li class="dropdown">
			 			<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Il mio profilo <span class="caret"></span></a>
			 			<ul class="dropdown-menu">
			 				<li><a class="" href="${urlChangepwd}">Cambia Password</a></li>
			 			</ul>
			 		</li>
					<li class=""><a href="${urlLogout}">Logout</a></li>
					<%
			 		}
					%>
				</ul>
				<%
				if (SessionHelper.getImpersonateUser(session) != null) {
				%>				
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Impersonificazione (<%= SessionHelper.getImpersonateUser(session).getUser().getSurname()%>)<span class="caret"></span></a>
			 			<ul class="dropdown-menu">
			 				<li><a class="" href="${urlStopImpersonificazione}">Termina</a></li>
			 			</ul>						
					</li>
				</ul>
				<%
				} else {
				%>
				<ul class="nav navbar-nav navbar-right">
					<li class="active">
						<a href="#">Utente Collegato (<%= SessionHelper.getUser(session).getSurname()%>)</a>
					</li>
				</ul>
				<%
				}
				%>				
			</div>
			<%
			}
			%>	
		</div>
	</nav>
</header>