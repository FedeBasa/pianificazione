<%@page import="it.soprasteria.pianificazione.v2.util.SessionHelper"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/home" var="urlHome" />
<spring:url value="/admin/excel/upload/employee" var="urlUploadEmployee" />
<spring:url value="/admin/excel/upload/project" var="urlUploadProjects" />
<spring:url value="/admin/gestione_mese" var="urlApprova" />
<spring:url value="/admin/impersonate/stop" var="urlStopImpersonificazione" />
<spring:url value="/changepw" var="urlChangepwd" />
<spring:url value="/logout" var="urlLogout" />

<spring:url value="/resources/images/soprasterialogo.png" var="logo" />

<header>
	<nav class="navbar-custom">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="/v2/home"><img class="img-responsive" width="150px" src="${logo}"/></a>
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
		          <span class="icon-bar"></span>
		          <span class="icon-bar"></span>
		          <span class="icon-bar"></span>
		        </button>
			</div>			
			<%
			if (SessionHelper.getUser(session) != null) {
			%>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
		           	<%if(SessionHelper.getUser(session).getProfilo().equals("admin")){ %>
						    <li><a class="" href="${urlUploadEmployee}">Caricamento Risorse</a></li>
						    <li><a class="" href="${urlUploadProjects}">Caricamento Progetti</a></li>
						    <li><a class="" href="${urlApprova}">Gestione V2</a></li>
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
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Aiuto<span class="caret"></span></a>
			 			<ul class="dropdown-menu">
			 				<li><a class="" href="/v2/resources/ppt/guida_veloce.pptx">Guida Veloce</a></li>
			 			</ul>						
					</li>
				</ul>
			</div>
			<%
			}
			%>	
		</div>
	</nav>
	
</header>