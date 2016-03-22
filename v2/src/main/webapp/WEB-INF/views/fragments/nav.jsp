<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<spring:url value="/home" var="urlHome" />
<spring:url value="/excel/upload/employee" var="urlUpload" />
<spring:url value="/approvaPage" var="urlApprova" />
<spring:url value="/logout" var="urlLogout" />

<script>
$(document).ready(function(){
    $("#bottone").click(function(){
        $(".dropdown-menu").toggle();
    });
});
</script>

<nav class="navbar navbar-inverse ">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="${urlHome}">Home</a>
			<div class="btn-group">
			  <button type="button" id="bottone" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
			    Apri menu <span class="caret"></span>
			  </button>
				
			  <ul class="dropdown-menu" role="menu">
			    <li><a class="" href="${urlUpload}">Carica</a></li>
			    <li><a class="" href="${urlApprova}">Approva</a></li>
			  </ul>
			</div>
			<a class="navbar-brand" href="${urlLogout}">Logout</a>
			
		</div>
	</div>
	

</nav>