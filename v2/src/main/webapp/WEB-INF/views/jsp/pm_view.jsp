<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
 
<head>

	<jsp:include page="../fragments/head.jsp" />

</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 
   
   <div class="container-fluid">
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
				 <th>ProjectManager</th>
				 <th>Business Unit</th>
				 <th>Stato V2</th>
				</tr>
			 </thead>
			 <c:forEach items="${list}" var = "stato">
			     <tr>
					<td><c:out value="${stato.profilo}" /></td>
					<td>
					<c:out value = "${stato.bu}"/>
					</td>
					<td>
					<c:out value = "${stato.active}"/>
					</td>
				</tr>
			 </c:forEach>
		</table>
	</div>
   
</body>
</html>