<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="it">
<head>
	<jsp:include page="../fragments/head.jsp" />
</head>
<body>
	<jsp:include page="../fragments/nav.jsp" />
	<div class="container-custom">
	<h4>Pianificazione v2</h4>
	<div class="row">
			<div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
			<c:if test="${rejected}">
				<p class="alert alert-danger">Impossibile effettuare operazione : il nuovo mese non è ancora stato abilitato alla modifica</p>
			</c:if>
	
			<table class="table table-bordered table-striped">
				<tr>
					<th>Mese</th>
					<th>Business Unit</th>
					<th>Stato</th>
				</tr>
				<c:forEach items="${lista}" var = "bean">
				<tr>
					<c:if test="${bean.stato == 50 || bean.stato == 100}">
						<td>
							<a href="${pageContext.request.contextPath}/edit/v2?month=${bean.month}&bu=${bean.businessUnit}"><c:out value="${bean.formattedMonth}" /></a>
						</td>
					</c:if>
					<c:if test="${bean.stato == 10}">
						<td>
							<c:out value="${bean.month}" />
						</td>
					</c:if>
					<td>
						<c:out value="${bean.businessUnit}" />				
					</td>
					<td>
						<c:if test="${bean.stato == 100}">
							Aperto
						</c:if>
						<c:if test="${bean.stato == 50}">
							Integrato
						</c:if>
						<c:if test="${bean.stato == 10}">
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
		</div>
		</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>