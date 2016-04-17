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
		 $('#v2').DataTable({
			"paging":         false
		});
		 $("#download").click(function(){
			 window.location = "${pageContext.request.contextPath}/export/v2?month=${v2Bean.month}&bu=${v2Bean.businessUnit}";			 
		 });
		 $("#workload").click(function(){
			 window.location = "${pageContext.request.contextPath}/workload?month=${v2Bean.month}&bu=${v2Bean.businessUnit}";			 
		 });
		 
		<c:if test="${v2Bean.stato == 100}">
		
		$("#aggiungi").click(function(){
			var path="${pageContext.request.contextPath}/record/insert";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		});
		$("#delete").click(function(){
			var path = "${pageContext.request.contextPath}/record/delete";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		});
		$("#aggiorna").click(function(){
			var path = "${pageContext.request.contextPath}/record/update";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		});
		
		var options = {
			url : function(phrase) {
				return "${pageContext.request.contextPath}/autocomplete/risorse";
			},
			getValue : "nameSurname",
			list : {
				onSelectItemEvent: function() {
					var value = $("#employeeDesc").getSelectedItemData().badgeNumber;
					$("#badgeNumber").val(value).trigger("change");
				},
				maxNumberOfElements : 10,
				match : {
					enabled : true
				},
			}
		}
		$("#employeeDesc").easyAutocomplete(options);
		var options2 = {
			url : function(phrase) {
				return "${pageContext.request.contextPath}/autocomplete/progetto?bu=${v2Bean.businessUnit}";
			},
			getValue : function(result) {
				return result.description;
			},
			
			list : {
				
				onSelectItemEvent: function() {
					var first = getUrlVars()["month"];
					var value= [$("#projectDesc").getSelectedItemData().customer,
					           $("#projectDesc").getSelectedItemData().currency,
					           $("#projectDesc").getSelectedItemData().type,
					           $("#projectDesc").getSelectedItemData().idProject,
					            first];
					console.log(value[4]);
					$("#customer").val(value[0]).trigger("change");
					$("#currency").val(value[1]).trigger("change");
					$("#activityType").val(value[2]).trigger("change");
					$("#idProject").val(value[3]).trigger("change");
					$("#month").val(value[4]).trigger("change");
				},
				
				maxNumberOfElements : 10,
				match : {
					enabled : true
				},
			}
		}
		$("#projectDesc").easyAutocomplete(options2);
		
		$('#v2').editableTableWidget();
		
		$('#v2').on('change', function(evt,newValue){
			
			var data = newValue;
			var colname = $(evt.target).attr('colname');
			var id = $(evt.target).parent().attr('rowId');
			
			var url = "${pageContext.request.contextPath}/update?month=${v2Bean.month}&bu=${v2Bean.businessUnit}&id="+ id +"&colname="+colname+"&value="+data;
			
			$.ajax({
				type: 'POST',
				url: url,
				success: function(result) {
					$('#responsecode').val(result.code);
				},
				async:false
			});
			
			if ($('#responsecode').val() < 0) {
				return false;
			}
		});
		
		</c:if>
	});
	
	<c:if test="${v2Bean.stato == 100}">
	function detail(id) {
		$.get("${pageContext.request.contextPath}/record/detail/" + id,
				function(data) {
					console.log(data);
					for ( var key in data) {
						var value = data[key];
						$('#' + key).val(value);
					}
					$('#employeeDesc').val(data.employeeDesc).change();
					$('#projectDesc').val(data.projectDesc).change();
				});
	}
	function validate(form) {
		if(confirm("Vuoi approvare questo mese?")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	function getUrlVars() {
	    var vars = {};
	    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
	        vars[key] = value;
	    });
	    return vars;
	}
	</c:if>
 </script>
	</head>
	<jsp:include page="../fragments/nav.jsp" />
<body>
	
	<input type="hidden" id="responsecode">
	<div class="container-fluid">
		<c:if test="${v2Bean.stato == 100}">
		<div class="row">
			<div class="col-sm-4">
				<div class="btn-group">
					<button id="download" type="button" class="btn btn-primary" >Export</button>
					<button id="workload" type="button" class="btn btn-primary" >Workload</button>
					<button id="aggiorna" type="button" class="btn btn-primary">Aggiorna</button>
					<button id="delete" type="button" class="btn btn-primary">Elimina</button>
					<button id="aggiungi" type="button" class="btn btn-primary">Aggiungi</button>
				</div>
				
				<div class="btn-group">
			 		<form:form method="POST" class="form-horizontal" action="${pageContext.request.contextPath}/valida?month=${month}&bu=${businessUnit}" onsubmit="return validate(this);">
			 			<button id="valida" type="submit" class="${v2Bean.stato == 100 ? 'btn btn-warning' : 'btn btn-warning disabled'}">Valida</button>
			 		</form:form>
					</div>
			</div>
			<div class="col-sm-8">
				<form:form method="POST" class="form-horizontal" modelAttribute="v2Form" action="${pageContext.request.contextPath}/send/data">
				    <form:hidden path="month"/>
				    <form:hidden path="businessUnit"/>
				    <form:hidden path="idRecord"/>
				    <form:hidden path="idProject"/>
					<form:hidden path="badgeNumber"/>
			
					<spring:bind path="employeeDesc">
							<div class="col-lg-4">
							<label>Risorsa :</label>
								<form:input path="employeeDesc" type="text" class="form-control" placeholder="Risorsa" />
								<form:errors path="employeeDesc" class="control-label" />
							</div>
					</spring:bind>
		
					
					<spring:bind path="projectDesc">
							<div class="col-lg-4">
							<label>Progetto :</label>
								<form:input path="projectDesc" type="text" class="form-control" placeholder="Progetto" />
								<form:errors path="projectDesc" class="control-label" />
						</div>
					</spring:bind>
			
				</form:form>
			</div>
		</div>
		</c:if>
		<div class = "row">
			<div class="col-lg-12">
				<table id="v2" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Risorsa</th>
							<th>Attivita'</th>
							<th>Progetto</th>
							<th>Tariffa</th>
							<th>Valuta</th>
							<th>Cons M (<c:out value="${cons1}"/>)</th>
							<th>Prod M</th>
							<th>Cons M+1 (<c:out value="${cons2}"/>)</th>
							<th>Prod M+1</th>
							<th>Cons M+2 (<c:out value="${cons3}"/>)</th>
							<th>Prod M+2</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${list}" var="item">
						<tr onclick="detail(${item.idRecord})" rowId="${item.idRecord}">
						    <td><c:out value="${item.employeeDesc}" /></td>
							<td colname="activity"><c:out value="${item.activityType}" /></td>
							<td><c:out value="${item.projectDesc}" /></td>
							<td colname="price"><c:out value="${item.price}" /></td>
							<td colname="currency"><c:out value="${item.currency}" /></td>
							<td colname="cons0"><c:out value="${item.cons0}" /></td>
							<td colname="prod0"><c:out value="${item.prod0}" /></td>
							<td colname="cons1"><c:out value="${item.cons1}" /></td>
							<td colname="prod1"><c:out value="${item.prod1}" /></td>
							<td colname="cons2"><c:out value="${item.cons2}" /></td>
							<td colname="prod2"><c:out value="${item.prod2}" /></td>
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