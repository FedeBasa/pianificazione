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
  <div class="col-lg-12">
   <div class="container-fluid">
		<table id = "projectManager" class="table table-bordered table-striped">
			<thead>
				<tr>
				 <th>ProjectManager</th>
				 <th>Business Unit</th>
				 <th>Stato V2</th>
				</tr>
			 </thead>
			 <c:forEach items="${list}" var = "stato">
			     <tr>
					<td><c:out value="${stato.pm}" /></td>
					<td>
					<c:out value = "${stato.mese}"/>
					</td>
					<td>
					<c:out value = "${stato.stato}"/>
					</td>
				</tr>
			 </c:forEach>
		</table>
	</div>
	</div>
   
</body>
</html>