<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it">
	<head>	
		<jsp:include page="../fragments/head.jsp" />

<script>
	$(document).ready(function() {
		 $('#table1').DataTable({
			"paging":         false
		});
		 
	});
 </script>
 	<style>
 		.nit {
 			background-color:yellow;
 		}
 		.nonProj {
 			background-color:orange;
 		}
 	</style>
	</head>
<body>
	
	<div class="container-fluid">
		<div class = "row">
			<div class="col-lg-12">
				<table id="table1" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Risorsa</th>
							<th>Progetto</th>
							<th>Cons M</th>
							<th>Prod M</th>
							<th>Cons M+1</th>
							<th>Prod M+1</th>
							<th>Cons M+2</th>
							<th>Prod M+2</th>
							<th>Utente Ins</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${list}" var="item">
						<tr>
						    <td><c:out value="${item.employeeDesc}" /></td>
							<td><c:out value="${item.descProgetto}" /></td>
							<td><c:out value="${item.cons1}" /></td>
							<td><c:out value="${item.prod1}" /></td>
							<td><c:out value="${item.cons2}" /></td>
							<td><c:out value="${item.prod2}" /></td>
							<td><c:out value="${item.cons3}" /></td>
							<td><c:out value="${item.prod3}" /></td>
							<td><c:out value="${item.usernameIns}" /></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>

</html>