<%--@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>

	<jsp:include page="../fragments/head.jsp" />
	<script>
	$(document).ready(function() {
		$("#projectManager").DataTable({
			"paging":         false,
			fixedHeader: {
	        header: false,
	        footer: true
	    	}
		});
	});
	
	</script>
</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 
   <div class="container-custom">
   <h4>Dettaglio mese</h4>
   <div class="row">
			<div class="col-xs-12 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6">
				<table id = "projectManager" class="table table-bordered table-striped">
					<thead>
						<tr>
						 <th>ProjectManager</th>
						 <th>Stato V2</th>
						 <th>Divisione</th>
						</tr>
					 </thead>
					 <c:forEach items="${list}" var = "item">
					     <tr>
							<td>
								<a href="${pageContext.request.contextPath}/admin/impersonate?username=${item.username}">
									<c:out value="${item.surname}"/> <c:out value="${item.name}"/>
								</a>
							</td>
							<td>
								<c:choose>
									<c:when test="${validatelist.contains(item.username)}">
										<span style="background-color:yellow;color:darkgreen">INTEGRATO</span>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${checklist.contains(item.username)}">
												<span style="color:darkgreen">Compilato</span>
											</c:when>
											<c:otherwise>
												<span style="color:red">Non Compilato</span>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:out value="${item.divisione}"/>
							</td>
						</tr>
					 </c:forEach>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>