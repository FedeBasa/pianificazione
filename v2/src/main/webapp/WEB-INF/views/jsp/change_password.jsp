<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>

	<jsp:include page="../fragments/head.jsp" />

<script>
		$(document).ready(function(){
			$("#submit").click(){
				console.log("PROVA " +$("input[name='newPw']").val());
			}
		});

</script>
		
</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 
  <div class="col-lg-4">
   <div class="container-fluid">
   <form id  = "pwForm" action = "${pageContext.request.contextPath}/saveNewPassword" method="post">
         <label >Vecchia password</label>
         <input type = "password"  class="form-control" id = "oldPw">
         <label>Nuova password</label>
         <input type = "password" class="form-control" name = "npw" id = "newPw">
         <br><br>
        &nbsp <input type = "submit" id="submit" class="btn btn-primary" value = "Salva nuova password">
     </form>    
	</div>
	</div>
     
</body>
</html>