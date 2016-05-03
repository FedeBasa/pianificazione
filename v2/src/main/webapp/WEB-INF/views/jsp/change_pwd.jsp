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
			$("#form1").submit(function(event) {
				var pwd1 = $("#newPw").val();
				var pwd2 = $("#repeatPw").val();
				if (pwd1 != pwd2) {
					alert("Controlla le password inserite, non coincidono");
					event.preventDefault();
				}
			})
		});

</script>
		
</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 

	<div class="alert alert-info">
		<span>Non sono presenti restrizioni legate alla complessità della password</span>
	</div>
	<c:if test="${not empty errorMessage}">
		<div class="alert alert-danger">
			<span>${errorMessage}</span>
		</div>
	</c:if>
 	<div class="col-lg-4">
		<div class="container-custom">
			<form:form id="form1" action="${pageContext.request.contextPath}/changepw" method="POST">
				<label class="control-label">Nuova password</label>
				<input type = "password"  class="form-control" name="npw" id="newPw" maxlength="45">
				<label class="control-label">Ripeti password</label>
				<input type = "password" class="form-control" name = "repeatNpw" id="repeatPw" maxlength="45">
				<br><br>
				<input type = "submit" id="submit" class="btn btn-primary" value = "Salva nuova password">
			</form:form>    
		</div>
	</div>     
</body>
</html>