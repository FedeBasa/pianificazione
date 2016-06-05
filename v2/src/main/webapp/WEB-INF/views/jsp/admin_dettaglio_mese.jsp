<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>

	<jsp:include page="../fragments/head.jsp" />

</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 
   <div class="container-custom">
		<table id = "projectManager" class="table table-bordered table-striped">
			<thead>
				<tr>
				 <th>ProjectManager</th>
				 <th>Stato V2</th>
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
							<c:when test="${checklist.contains(item.username)}">
								<span style="color:darkgreen">Compilato</span>
							</c:when>
							<c:otherwise>
								<span style="color:red">Non Compilato</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			 </c:forEach>
		</table>
	</div>
</body>
</html>