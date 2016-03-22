<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="it">
<head>
	<title>Spring MVC</title>
	<jsp:include page="../fragments/head.jsp" />
</head>
<jsp:include page="../fragments/nav.jsp" />
<body>
	<div class="container-fluid">
		<c:if test="${rejected}">
			<p class="alert alert-danger">Impossibile aggiungere il mese</p>
		</c:if>

		<table class="table table-bordered table-striped">
			<tr>
				<th>Mese</th>
				<th>Stato</th>
			</tr>
			<c:forEach items="${lista}" var = "bean">
			<tr>
				<td>
					<a href="${pageContext.request.contextPath}/edit/v2?month=${bean.month}"><c:out value="${bean.month}" /></a>
				</td>
				<td>
					<c:if test="${bean.editable}">
						Aperto
					</c:if>
					<c:if test="${!bean.editable}">
						Chiuso
					</c:if>
				</td>
			</tr>
			</c:forEach>
		</table>
		
		<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/addMonth">
	       <button type="submit" class="btn btn-primary">Aggiungi Mese</button>       
		</form:form>
	</div>
</body>
</html>