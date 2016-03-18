<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/home" var="urlHome" />
<spring:url value="/excel/upload/employee" var="urlUpload" />
<spring:url value="/logout" var="urlLogout" />

<nav class="navbar navbar-inverse ">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="${urlHome}">Home</a>
			<a class="navbar-brand" href="${urlUpload}">Carica</a>
			<a class="navbar-brand" href="${urlLogout}">Logout</a>
		</div>
	</div>
</nav>