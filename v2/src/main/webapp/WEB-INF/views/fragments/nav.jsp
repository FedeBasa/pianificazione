<%@page import="it.soprasteria.pianificazione.v2.util.SessionHelper"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/home" var="urlHome" />
<spring:url value="/excel/upload/employee" var="urlUploadEmployee" />
<spring:url value="/excel/upload/project" var="urlUploadProjects" />
<spring:url value="/admin/gestione_mese" var="urlApprova" />
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
					<li class=""><a href="${urlLogout}">Logout</a></li>
					<%
			 		}
					%>
				</ul>
			</div>
			<%
			}
			%>	
		</div>
	</nav>
</header>