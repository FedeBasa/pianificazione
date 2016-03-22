<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

	<spring:url value="/resources/css/bootstrap.min.css" var="css1" />
	<spring:url value="/resources/css/dataTables.bootstrap.min.css" var="css2" />
	<spring:url value="/resources/css/jquery.dataTables.min.css" var="css3" />
	<spring:url value="/resources/EasyAutocomplete-1.3.3/easy-autocomplete.min.css" var="css4" />
	
	<spring:url value="/webjars/jquery/1.11.1/jquery.js" var="js1" />
	<spring:url value="/resources/js/jquery.dataTables.min.js" var="js2"/>
	<spring:url value="/resources/js/dataTables.bootstrap.min.js" var="js3" />
	<spring:url value="/resources/js/table.js" var="js4" />
	<spring:url value="/resources/EasyAutocomplete-1.3.3/jquery.easy-autocomplete.min.js" var="js5" />

	<link href="${css1}" rel="stylesheet" >
	<link href="${css2}" rel="stylesheet" >
	<link href="${css4}" rel="stylesheet" >
	
	<script src="${js1}"></script>
	<script src="${js2}"></script>
	<script src="${js3}"></script>
	<script src="${js4}"></script>
	<script src="${js5}"></script>
	