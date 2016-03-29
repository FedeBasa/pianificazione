<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<jsp:include page="../fragments/head.jsp" />
<title>Approva mese</title>

<script>
function validate(form) {
	if(confirm("Vuoi approvare questo mese?")) {
		return true;
	}
	else {
		return false;
	}
}

</script>
</head>
<jsp:include page="../fragments/nav.jsp" />
<body>
	<div class="container-fluid">
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
				 <th>Mese</th>
				 <th>Azioni</th>
				</tr>
			 </thead>
			 <c:forEach items="${v2List}" var = "v2">
			     <tr>
					<td><c:out value="${v2.month}" /></td>
					<td>
						<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/admin/chiudi?month=${v2.month}" style="float:left; padding-right:20px" onsubmit="return validate(this);">
							<button id="chiudi" type="submit" class="${v2.stato == 100 ? 'btn btn-warning' : 'btn btn-warning disabled'}">Chiudi</button>
						</form:form>
						<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/admin/apri?month=${v2.month}" onsubmit="return validate(this);">
							<button id="apri" type="submit" class="${v2.stato == 10 ? 'btn btn-warning' : 'btn btn-warning disabled'}">Apri</button>
						</form:form>
					</td>
				</tr>
			 </c:forEach>
		</table>
		
		<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/addConfigMonth">
	       <button type="submit" class="btn btn-primary">Aggiungi Mese</button>       
		</form:form>
	</div>
</body>
</html>