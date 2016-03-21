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
<c:if test="${rejected}">
<%-- 	<spring:message code="rejected.month"></spring:message> --%>
	<p class="alert alert-danger">Impossibile aggiungere il mese</p>
</c:if>
	<table class="table table-bordered table-striped">
	<thead>
	<tr>
	 <th>Mese</th>
	 </tr>
	 </thead>
	 <c:forEach items="${lista}" var = "month">
	     <tr>
			<td><a href="${pageContext.request.contextPath}/edit/v2?month=${month}"><c:out value="${month}" /></a></td>
		</tr>
	 </c:forEach>
	</table>
	
	<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/addMonth">
       <button type="submit" class="btn btn-primary">Aggiungi Mese</button>       
	</form:form>
	
</body>
</html>