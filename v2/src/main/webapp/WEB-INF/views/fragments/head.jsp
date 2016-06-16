<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

	<spring:url value="/resources/images/soprafavicon.ico" var="ico1" />

	<spring:url value="/resources/css/bootstrap.min.css" var="css1" />
	<spring:url value="/resources/css/dataTables.bootstrap.min.css" var="css2" />
	<spring:url value="/resources/css/jquery.dataTables.min.css" var="css3" />
	<spring:url value="/resources/EasyAutocomplete-1.3.3/easy-autocomplete.min.css" var="css4" />
	<spring:url value="/resources/css/main.css" var="css5" />
	<spring:url value="/resources/FixedHeader-3.1.2/css/fixedHeader.dataTables.min.css" var="css6" />
	<spring:url value="/resources/perfect-scrollbar/css/perfect-scrollbar.min.css" var="css7" />
	
	<spring:url value="/webjars/jquery/1.11.1/jquery.min.js" var="js1" />
	<spring:url value="/resources/js/jquery.dataTables.min.js" var="js2"/>
	<spring:url value="/resources/js/dataTables.bootstrap.min.js" var="js3" />
	<spring:url value="/resources/js/table.js" var="js4" />
	<spring:url value="/resources/EasyAutocomplete-1.3.3/jquery.easy-autocomplete.min.js" var="js5" />
	<spring:url value="/resources/js/bootstrap.min.js" var="js6" />
	<spring:url value="/resources/FixedHeader-3.1.2/js/dataTables.fixedHeader.js" var="js7" />
	<spring:url value="/resources/perfect-scrollbar/js/perfect-scrollbar.jquery.js" var="js8" />
	<spring:url value="/resources/js/common.js" var="js9" />
	<spring:url value="/resources/js/v2.js" var="js10" />
	
	<%--<script src="./v2_files/mindmup-editabletable.js"></script> --%>

	<title>Pianificazione Risorse</title>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<link rel="shortcut icon" type="image/png" href="${ico1}"/>
	<link href="${css1}" rel="stylesheet" >
	<link href="${css2}" rel="stylesheet" >
	<link href="${css4}" rel="stylesheet" >
	<link href="${css5}" rel="stylesheet" >
	<link href="${css6}" rel="stylesheet" >
	<link href="${css7}" rel="stylesheet" >
	
	
	<script src="${js1}"></script>
	<script src="${js2}"></script>
	<script src="${js3}"></script>
	<script src="${js4}"></script>
	<script src="${js5}"></script>
	<%--
	<script>
		$(document).ready(function(){
		    $("#bottone").click(function(){
		        $(".dropdown-menu").toggle();
		    });
		});
	</script>
	--%>
	<script src="${js6}"></script>
	<script src="${js7}"></script>
	<script src="${js8}"></script>
	<script src="${js9}"></script>
	<script src="${js10}"></script>
	<style>
	
		body {
			font-size: 11px;
		}
	</style>