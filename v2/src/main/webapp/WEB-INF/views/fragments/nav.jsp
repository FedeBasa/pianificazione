<%@page import="it.soprasteria.pianificazione.v2.util.SessionHelper"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/home" var="urlHome" />
<spring:url value="/excel/upload/employee" var="urlUploadEmployee" />
<spring:url value="/excel/upload/project" var="urlUploadProjects" />
<spring:url value="/admin/gestione_mese" var="urlApprova" />
<spring:url value="/logout" var="urlLogout" />

  
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Project Name</a>
		</div>
		<%
		if (SessionHelper.getUser() != null) {
		%>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="${urlHome}">Home</a></li>
	           	<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Amministrazione <span class="caret"></span></a>
					<ul class="dropdown-menu open">
					    <li><a class="" href="${urlUploadEmployee}">Caricamento Risorse</a></li>
					    <li><a class="" href="${urlUploadProjects}">Caricamento Progetti</a></li>
					    <li><a class="" href="${urlApprova}">Gestione V2</a></li>
					</ul>
				</li>
				<li class=""><a href="${urlLogout}">Logout</a></li>
			</ul>
		</div>
		<%
		}
		%>
	</div>
</nav>