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
	<div class="container">
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
				 <th>Mese</th>
				 <th>Approva</th>
				</tr>
			 </thead>
			 <c:forEach items="${v2List}" var = "v2">
			     <tr>
					<td><a href="${pageContext.request.contextPath}/edit/v2?month=${v2.month}"><c:out value="${v2.month}" /></a></td>
					<td>
						<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/approva?month=${v2.month}" onsubmit="return validate(this);">
							<button id="approva" type="submit" class="${v2.editable eq true ? 'btn btn-warning' : 'btn btn-warning disabled'}">Approva</button>
						</form:form>
					</td>
				</tr>
			 </c:forEach>
		</table>
	</div>
</body>
</html>