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
		 $('#v2').DataTable({
			"paging":         false
		});
		 
		 $("#edit").click(function(){
			 window.location = "${pageContext.request.contextPath}/edit/v2?month=${month}&bu=${businessUnit}";			 
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
	<jsp:include page="../fragments/nav.jsp" />
<body>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-4">
				<div class="btn-group">
					<button id="edit" type="button" class="btn btn-primary" >Edit</button>
				</div>
			</div>
		<div class = "row">
			<div class="col-lg-12">
				<table id="v2" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Risorsa</th>
							<th>Work M</th>
							<th>Rec M</th>
							<th>NIT M</th>
							<th>Non Proj M</th>
							<th>Work M+1</th>
							<th>Rec M+1</th>
							<th>NIT M+1</th>
							<th>Non Proj M+1</th>
							<th>Work M+2</th>
							<th>Rec M+2</th>
							<th>NIT M+2</th>
							<th>Non Proj M+2</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${list}" var="item">
						<tr>
						    <td><c:out value="${item.employeeDesc}" /></td>
							<td><c:out value="${item.work1}" /></td>
							<td><c:out value="${item.recognized1}" /></td>
							<td class="<c:out value="${item.nit1 gt 0 ? 'nit' : ''}"/>"><c:out value="${item.nit1}" /></td>
							<td class="<c:out value="${item.nonProject1 gt 0 ? 'nonProj' : ''}"/>"><c:out value="${item.nonProject1}" /></td>
							<td><c:out value="${item.work2}" /></td>
							<td><c:out value="${item.recognized2}" /></td>
							<td class="<c:out value="${item.nit2 gt 0 ? 'nit' : ''}"/>"><c:out value="${item.nit2}" /></td>
							<td class="<c:out value="${item.nonProject2 gt 0 ? 'nonProj' : ''}"/>"><c:out value="${item.nonProject2}" /></td>
							<td><c:out value="${item.work2}" /></td>
							<td><c:out value="${item.recognized3}" /></td>
							<td class="<c:out value="${item.nit3 gt 0 ? 'nit' : ''}"/>"><c:out value="${item.nit3}" /></td>
							<td class="<c:out value="${item.nonProject3 gt 0 ? 'nonProj' : ''}"/>"><c:out value="${item.nonProject3}" /></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />	
</body>

</html>