<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it">
	<head>	
		<jsp:include page="../fragments/head.jsp" />

<script  src= 
       "${pageContext.request.contextPath}/resources/js/mindmup-editabletable.js"></script>	
       
<script>
	$(document).ready(function() {
		 $('#table1').DataTable({
			"paging":         false
		});
		 
		 $("#edit").click(function(){
			 window.location = "${pageContext.request.contextPath}/edit/v2?month=${month}&bu=${businessUnit}";			 
		 });

	$('#table1').editableTableWidget();
	
	$('#table1').on('change', function(evt,newValue){
		
		var data = newValue;
		var colname = $(evt.target).attr('colname');
		var badgeNumber = $(evt.target).attr('badgeNumber');
		var id = $(evt.target).parent().attr('rowId');
		
		var url = "${pageContext.request.contextPath}/updateWorkload?month=${month}&bu=${businessUnit}&id="+ id +"&colname="+colname+"&value="+data+"&badgeNumber="+badgeNumber;
		
		$.ajax({
			type: 'POST',
			url: url,
			success: function(result) {
				$('#responsecode').val(result.code);
				if(result.code == 0) {
					$('#nonProj1_' + badgeNumber).html(result.nonProj1);
					$('#nonProj2_' + badgeNumber).html(result.nonProj2);
					$('#nonProj3_' + badgeNumber).html(result.nonProj3);
				}
			},
			async:false
		});
		
		if ($('#responsecode').val() < 0) {
			return false;
		}
	});
	
	});
	
 </script>
 	<style>
 		.nit {
 			background-color:yellow;
 		}
 		.nonProj {
 			background-color:yellow;
 		}
	 	.border-left {
	 		border-left: 1px solid black !important;
	 	}
	 	
	 	.text-center {
	 		text-align: center;
	 	} 		
 	</style>
	</head>
	<jsp:include page="../fragments/nav.jsp" />
<body>
	
	<input type="hidden" id="responsecode">
	
	<div class="container-custom">
		<h4>Workload Risorse</h4>
	
		<div class="row">
			<div class="col-sm-4">
				<div class="btn-group">
					<button id="edit" type="button" class="btn btn-primary" >Torna indietro</button>
				</div>
			</div>
		</div>
		<div class = "row">
			<div class="col-lg-12">
				<table id="table1" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th> </th>
							<th colspan="5" class="border-left text-center">${currentMonth} (<c:out value="${cons1}"/>)</th>
							<th colspan="5" class="border-left text-center">${nextMonth} (<c:out value="${cons2}"/>)</th>
							<th colspan="5" class="border-left text-center">${lastMonth} (<c:out value="${cons3}"/>)</th>
						</tr>
						<tr>
							<th>Risorsa</th>
							<th class="border-left">Work M</th>
							<th>Rec M</th>
							<th>NIT M</th>
							<th>Non Proj M</th>
							<th>Ferie M</th>
							<th class="border-left">Work M+1</th>
							<th>Rec M+1</th>
							<th>NIT M+1</th>
							<th>Non Proj M+1</th>
							<th>Ferie M+1</th>
							<th class="border-left">Work M+2</th>
							<th>Rec M+2</th>
							<th>NIT M+2</th>
							<th>Non Proj M+2</th>
							<th>Ferie M+2</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${list}" var="item">
						<tr>
						    <td>
							    <a href="#" onclick="javascript:window.open('${pageContext.request.contextPath}/workload/detail/${item.badgeNumber}?month=${month}', '', 'width=1000,height=400');">
							    	<c:out value="${item.employeeDesc}" />
							    </a>
						    </td>
						    
							<td colname="work1" class="border-left"><c:out value="${item.work1}" /></td>
							<td colname="recognized1"><c:out value="${item.recognized1}" /></td>
							<td colname="nit1" class="<c:out value="${item.nit1 ne 0 ? 'nit' : ''}"/>"><c:out value="${item.nit1}" /></td>
							<td id="nonProj1_${item.badgeNumber}" colname="nonProj1" class="<c:out value="${item.nonProject1 gt 0 ? 'nonProj' : ''}"/>"><c:out value="${item.nonProject1}" /></td>
							<td colname="ferie1" badgeNumber="${item.badgeNumber}"><c:out value="${item.ferie1}" /></td>
							<td colname="work2" class="border-left"><c:out value="${item.work2}" /></td>
							<td colname="recognized2"><c:out value="${item.recognized2}" /></td>
							<td colname="nit2" class="<c:out value="${item.nit2 ne 0 ? 'nit' : ''}"/>"><c:out value="${item.nit2}" /></td>
							<td id="nonProj2_${item.badgeNumber}" colname="nonProj2" class="<c:out value="${item.nonProject2 gt 0 ? 'nonProj' : ''}"/>"><c:out value="${item.nonProject2}" /></td>
							<td colname="ferie2" badgeNumber="${item.badgeNumber}"><c:out value="${item.ferie2}" /></td>
							<td colname="work3" class="border-left"><c:out value="${item.work3}" /></td>
							<td colname="recognized3"><c:out value="${item.recognized3}" /></td>
							<td colname="nit3" class="<c:out value="${item.nit3 ne 0 ? 'nit' : ''}"/>"><c:out value="${item.nit3}" /></td>
							<td id="nonProj3_${item.badgeNumber}" colname="nonProj3" class="<c:out value="${item.nonProject3 gt 0 ? 'nonProj' : ''}"/>"><c:out value="${item.nonProject3}" /></td>
							<td colname="ferie3" badgeNumber="${item.badgeNumber}"><c:out value="${item.ferie3}" /></td>
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