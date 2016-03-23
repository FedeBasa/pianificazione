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
			"paging":         false}
		);
		function getUrlVars() {
		    var vars = {};
		    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		        vars[key] = value;
		    });
		    return vars;
		}
		
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
				var sel = document.getElementById("bu");
				var selected = sel.options[sel.selectedIndex].value;
				console.log(selected);
				return "${pageContext.request.contextPath}/autocomplete/progetto?bu="+selected;
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
			
			var url = "${pageContext.request.contextPath}/update?id="+ id +"&colname="+colname+"&value="+data;
			
			//$.post(url, function( data ) {
			//	console.log(data);
			//});
			
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

	});
	 
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
					console.log(data.businessUnit);
					$('#bu').val(data.businessUnit).change();
				});
	}

 </script>
	</head>
	<jsp:include page="../fragments/nav.jsp" />
<body style="font-size:12px">
	
	<input type="hidden" id="responsecode">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-3">
				<div class="row">
					<div class="col-sm-12">
						<div class="btn-group">
				 			<c:if test="${editable}">
								<button id="download" type="button" class="btn btn-primary" onclick="tableToExcel('v2','v2')">Export</button>
								<button id="aggiorna" type="button" class="btn btn-primary">Aggiorna</button>
								<button id="delete" type="button" class="btn btn-primary">Elimina</button>
								<button id="aggiungi" type="button" class="btn btn-primary">Aggiungi</button>
				 			</c:if>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="panel panel-default" style="margin-top:6px">
							<div class="panel-heading">
								<h3 class="panel-title"></h3>
							</div>
							<div class="panel-body">
								<form:form method="POST" class="form-horizontal" modelAttribute="v2Form" action="${pageContext.request.contextPath}/send/data">
								    <form:hidden path="month"/>
								    <form:hidden path="idRecord"/>
								    <form:hidden path="idProject"/>
									<form:hidden path="badgeNumber"/>
							
									<spring:bind path="employeeDesc">
										<div class="form-group ${status.error ? 'has-error' : ''}">
											<label class="col-lg-1">Risorsa</label>
											<div class="col-lg-11">
												<form:input path="employeeDesc" type="text" class="form-control" placeholder="Risorsa" />
												<form:errors path="employeeDesc" class="control-label" />
											</div>
										</div>
									</spring:bind>
						
									<div class="form-group">
										<label class="col-lg-1">BU</label>
										<div class="col-lg-11">
											<select id="bu" class="form-control">
												<option value ="791">791</option>
												<option value = "792">792</option>
											</select>
										</div>
									</div>
									
									<spring:bind path="projectDesc">
										<div class="form-group ${status.error ? 'has-error' : ''}">
											<label class="col-lg-1">Progetto</label>
											<div class="col-lg-11">
												<form:input path="projectDesc" type="text" class="form-control" placeholder="Progetto" />
												<form:errors path="projectDesc" class="control-label" />
											</div>
										</div>
									</spring:bind>
							
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-9">
				<table id="v2" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Risorsa</th>
							<th>Attivita'</th>
							<th>Progetto</th>
							<th>Tariffa</th>
							<th>Valuta</th>
							<th>Cons 1</th>
							<th>Prod 1</th>
							<th>Cons 2</th>
							<th>Prod 2</th>
							<th>Cons 3</th>
							<th>Prod 3</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${list}" var="item">
						<tr onclick="detail(${item.idRecord})" rowId="${item.idRecord}">
						    <td><c:out value="${item.employeeDesc}" /></td>
							<td><c:out value="${item.activityType}" /></td>
							<td><c:out value="${item.projectDesc}" /></td>
							<td colname="price"><c:out value="${item.price}" /></td>
							<td><c:out value="${item.currency}" /></td>
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