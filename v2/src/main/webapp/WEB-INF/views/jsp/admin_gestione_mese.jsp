<%@page import="java.util.List"%>
<%@page import="it.soprasteria.pianificazione.v2.util.SessionHelper"%>
<%--@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<jsp:include page="../fragments/head.jsp" />

<script>
function validate(form) {
	if(confirm("Sei sicuro di voler procedere con l'operazione?")) {
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
	<div class="container-custom" style="overflow:visible">
	<h4>Gestione V2</h4>
	<div class="row">
			<div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
						 <th>Mese</th>
						 <th>Business Unit</th>
						 <th>Azioni</th>
						</tr>
					 </thead>
					 <c:forEach items="${v2List}" var = "v2">
					     <tr>
							<td><a href = "/v2/admin/detail?month=${v2.month}"><c:out value="${v2.formattedMonth}" /></a></td>
							<td>${v2.businessUnit}</td>
							<td>
								<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/admin/chiudi?month=${v2.month}&bu=${v2.businessUnit}" style="float:left; padding-right:20px" onsubmit="return validate(this);">
									<button id="chiudi" type="submit" class="${v2.stato == 100 ? 'btn btn-warning' : 'btn btn-warning disabled'}">Chiudi</button>
								</form:form>
								<form:form method="POST" class="form-horizontal" style="float:left; padding-right:20px" action="${pageContext.request.contextPath}/admin/apri?month=${v2.month}&bu=${v2.businessUnit}" onsubmit="return validate(this);">
									<button id="apri" type="submit" class="${v2.stato == 10 ? 'btn btn-warning' : 'btn btn-warning disabled'}">Apri</button>
								</form:form>
								<div class="btn-group">
									<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
									Stampe<span class="caret"></span>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li><a href="${pageContext.request.contextPath}/admin/export/v2?month=${v2.month}&bu=${v2.businessUnit}">Stampa V2</a></li>
										<li><a href="${pageContext.request.contextPath}/admin/export/v2_terzeparti?month=${v2.month}&bu=${v2.businessUnit}">Stampa V2 Risorse Esterne</a></li>
										<li><a href="${pageContext.request.contextPath}/admin/export/v2_ferie?month=${v2.month}&bu=${v2.businessUnit}">Stampa Ferie</a></li>
									</ul>
								</div>
							</td>
						</tr>
					 </c:forEach>
				</table>
		
				<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/admin/addConfigMonth">
			       <button type="submit" class="btn btn-primary">Aggiungi Mese</button>       
				</form:form>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>