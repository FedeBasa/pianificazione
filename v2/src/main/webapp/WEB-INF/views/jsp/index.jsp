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
			"scrollY":        "200px",
			"scrollCollapse": true,
			"paging":         false}
		);
		 
		function getUrlVars() {
		    var vars = {};
		    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		        vars[key] = value;
		    });
		    return vars;
		}
		
		$("#bottone").click(function(){
			var path="${pageContext.request.contextPath}/send/insert";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		});
		$("#delete").click(function(){
			var path = "${pageContext.request.contextPath}/send/delete";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		});
		$("#aggiorna").click(function(){
			var path = "${pageContext.request.contextPath}/send/data";
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
	});
	 
	function detail(id) {
		$.get("${pageContext.request.contextPath}/table/edit?id=" + id,
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
		
 		$('#v2').editableTableWidget();
 		
 		$('#price').on('change', function(evt,newValue){
 			var colname = "tariffa"
 			var data = newValue;
 			console.log(evt);
 			console.log(newValue);
  			$.ajax({
  				  type: "POST",
  				  url: "${pageContext.request.contextPath}/update?id=3&colname="+colname+"&value="+newValue,
  				  data: "",
  				  success : console.log("success"),
  				  dataType : "text"
			});
 		});
	}
</script>
	</head>
	<jsp:include page="../fragments/nav.jsp" />
<body>
	
	<div class="container">
		<div class="btn-group">
		
			<button id="download" type="button" class="btn btn-primary" onclick="tableToExcel('v2','v2')">Export</button>
			<button id="aggiorna" type="button" class="btn btn-primary">Aggiorna</button>
			<button id="delete" type="button" class="btn btn-primary">Elimina</button>
			<button id="bottone" type="button" class="btn btn-primary">Aggiungi</button>
			
		</div>

	
		<table id="v2" class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>Risorsa</th>
					<th>Attività</th>
					<th>Progetto</th>
					<th>Tariffa</th>
					<th>Valuta</th>
					<th>Consolidato 1</th>
					<th>Prodotto 1</th>
					<th>Consolidato 2</th>
					<th>Prodotto 2</th>
					<th>Consolidato 3</th>
					<th>Prodotto 3</th>
				</tr>
			</thead>
			<tbody>	
			<c:forEach items="${list}" var="item">
				<tr onclick="detail(${item.idRecord})">
				    <td><c:out value="${item.employeeDesc}" /></td>
					<td><c:out value="${item.activityType}" /></td>
					<td><c:out value="${item.projectDesc}" /></td>
					<td id = "price"><c:out value="${item.price}" /></td>
					<td><c:out value="${item.currency}" /></td>
					<td><c:out value="${item.cons0}" /></td>
					<td><c:out value="${item.prod0}" /></td>
					<td><c:out value="${item.cons1}" /></td>
					<td><c:out value="${item.prod1}" /></td>
					<td><c:out value="${item.cons2}" /></td>
					<td><c:out value="${item.prod2}" /></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<form:form method="POST" class="form-horizontal" modelAttribute="v2Form" action="${pageContext.request.contextPath}/send/data">
		    <form:hidden path="month"/>
		    <form:hidden path="idRecord"/>
		    <form:hidden path="idProject"/>
			<form:hidden path="badgeNumber"/>
			<form:hidden path="idProject"/>
	
			<spring:bind path="employeeDesc">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Risorsa</label>
					<div class="col-sm-6">
						<form:input path="employeeDesc" type="text" class="form-control" placeholder="Risorsa" />
						<form:errors path="employeeDesc" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<div class="form-group">
				<label class="col-sm-2 control-label">BU</label>
				<div class="col-sm-6">
					<select id="bu" class="form-control">
						<option value ="791">791</option>
						<option value = "792">792</option>
					</select>
				</div>
			</div>
			
			<spring:bind path="projectDesc">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Progetto</label>
					<div class="col-sm-6">
						<form:input path="projectDesc" type="text" class="form-control" placeholder="Progetto" />
						<form:errors path="projectDesc" class="control-label" />
					</div>
				</div>
			</spring:bind>
	
			<div class="form-group">
				<label class="col-sm-2 control-label">Cliente</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" id="customer" readonly> 
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Attività</label>
				<div class="col-sm-6">
					<input  type="text" class="form-control" id="activityType" readonly> 
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Valuta</label>
				<div class="col-sm-6">
					<input  type="text" class="form-control" id="currency" readonly> 
				</div>
			</div>
		
			<spring:bind path="price">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Tariffa</label>
					<div class="col-sm-6">
						<form:input path="price" type="text" class="form-control" placeholder="Tariffa" />
						<form:errors path="price" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="cons0">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Consolidato 1</label>
					<div class="col-sm-6">
						<form:input path="cons0" type="text" class="form-control" placeholder="Consolidato 1" />
						<form:errors path="cons0" class="control-label" />
					</div>
				</div>
			</spring:bind>
			<spring:bind path="prod0">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Prodotto 1</label>
					<div class="col-sm-6">
						<form:input path="prod0" type="text" class="form-control" placeholder="Prodotto 1" />
						<form:errors path="prod0" class="control-label" />
					</div>
				</div>
			</spring:bind>
	
			<spring:bind path="cons1">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Consolidato 2</label>
					<div class="col-sm-6">
						<form:input path="cons1" type="text" class="form-control" placeholder="Consolidato 2" />
						<form:errors path="cons1" class="control-label" />
					</div>
				</div>
			</spring:bind>
			<spring:bind path="prod1">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Prodotto 2</label>
					<div class="col-sm-6">
						<form:input path="prod1" type="text" class="form-control" placeholder="Prodotto 2" />
						<form:errors path="prod1" class="control-label" />
					</div>
				</div>
			</spring:bind>
	
			<spring:bind path="cons2">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Consolidato 3</label>
					<div class="col-sm-6">
						<form:input path="cons2" type="text" class="form-control" placeholder="Consolidato 3" />
						<form:errors path="cons2" class="control-label" />
					</div>
				</div>
			</spring:bind>
			<spring:bind path="prod2">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Prodotto 3</label>
					<div class="col-sm-6">
						<form:input path="prod2" type="text" class="form-control" placeholder="Prodotto 3" />
						<form:errors path="prod2" class="control-label" />
					</div>
				</div>
			</spring:bind>
	
	       
		</form:form>
	
	</div>	
	<jsp:include page="../fragments/footer.jsp" />	
</body>

</html>