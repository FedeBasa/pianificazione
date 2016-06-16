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

		 <c:if test="${v2Bean.stato == 100}">
		
		$("#aggiungi").click(function(){
			var path="${pageContext.request.contextPath}/record/insert";
			$("#v2Form").attr("action",path);
			$("#v2Form").submit();
		});
		$("#delete").click(function(){
			if (confirm('Stai per eliminare un record. Sei sicuro di voler continuare?')) {				
				var path = "${pageContext.request.contextPath}/record/delete";
				$("#v2Form").attr("action",path);
				$("#v2Form").submit();
			}
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
					$("#customer").val(value[0]).trigger("change");
					$("#currency").val(value[1]).trigger("change");
					$("#activityType").val(value[2]).trigger("change");
					$("#idProject").val(value[3]).trigger("change");
				},
				
				maxNumberOfElements : 10,
				match : {
					enabled : true
				},
			}
		}
		$('#employeeDesc').focus(function() {
			$('#badgeNumber').val('');
		});
		$("#employeeDesc").easyAutocomplete(options);
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
	
	function allineaProduzione() {
		
		if (confirm('Stai per allineare il prodotto con il lavorato su tutte le risorse. Sei sicuro di voler continuare?')) {
			
			window.location = "${pageContext.request.contextPath}/allineaproduzione?month=${month}&bu=${businessUnit}";
		}
		
	}
	
	function valida(month, bu) {
		var path = "${pageContext.request.contextPath}/valida?month=" + month + "&bu=" + bu;
		window.location.href=path;
	};

	</c:if>
	
	function workload(month, bu) {
		var path = "${pageContext.request.contextPath}/workload?month=" + month + "&bu=" + bu;
		window.location.href=path;
	};

	function riapri(month, bu) {
		var path = "${pageContext.request.contextPath}/riapri?month=" + month + "&bu=" + bu;
		window.location.href=path;
	};
	
 </script>
	</head>
	<jsp:include page="../fragments/nav.jsp" />
<body>
	
	<input type="hidden" id="responsecode">
	<div class="container-custom v2">

		<c:if test="${not empty errorMessage}">
			<div class="alert alert-danger">
				<span>${errorMessage}</span>
			</div>
		</c:if>	

		<c:if test="${v2Bean.stato != 10}">
		<div id="button-wrapper" class="row">
			<div class="col-xs-12">
				<div class="collapse-custom hide">🔧 Actions</div>
				<div class="row button-container">
					<div class="col-xs-12 col-sm-3">
						<div class="btn-group custom btn-action btn-4">
							<c:if test="${v2Bean.stato == 100}">
							<div class="btn-group">
								<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
								Azioni <span class="caret"></span></button>
								<ul class="dropdown-menu" role="menu">
									<li><a href="javascript:allineaProduzione();">Allinea Produzione</a></li>
									<li><a href="${pageContext.request.contextPath}/export/v2?month=${month}&bu=${businessUnit}">Scarica Excel</a></li>
								</ul>
							</div>
							<button id="aggiungi" type="button" class="btn btn-primary">Aggiungi</button>
							<button id="delete" type="button" class="btn btn-primary">Elimina</button>
							</c:if>
						</div>
					</div>
					<div class="col-xs-12 col-sm-3">
						<button id="worklad" type="button" class="btn btn-default" onclick="workload('${month}', '${businessUnit}')">Workload</button>
						<c:choose>
							<c:when test="${v2Bean.stato == 100}">						
								<button id="valida" type="button" class="btn btn-warning" onclick="valida('${month}', '${businessUnit}')">Consolida</button>
							</c:when>
							<c:otherwise>
								<button id="riapri" type="button" class="btn btn-primary" onclick="riapri('${month}', '${businessUnit}')">Apri</button>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="col-xs-12 col-sm-6">
						<form:form method="POST" class="form-horizontal" modelAttribute="v2Form" action="${pageContext.request.contextPath}/send/data">
						    <form:hidden path="month"/>
						    <form:hidden path="businessUnit"/>
						    <form:hidden path="idRecord"/>
						    <form:hidden path="idProject"/>
							<form:hidden path="badgeNumber"/>
					
							<spring:bind path="employeeDesc">
									<div class="col-xs-12 col-sm-6">
										<form:input path="employeeDesc" type="text" class="form-control" placeholder="Risorsa" />
										<form:errors path="employeeDesc" class="control-label" />
									</div>
							</spring:bind>
				
							
							<spring:bind path="projectDesc">
									<div class="col-xs-12 col-sm-6">
										<form:input path="projectDesc" type="text" class="form-control" placeholder="Progetto" />
										<form:errors path="projectDesc" class="control-label" />
								</div>
							</spring:bind>
					
						</form:form>
					</div>
				</div>
				<div class="back-top hide"></div>
			</div>
		</div>
		</c:if>
		<div class="row table-container">
			<div class="col-xs-12">
				<div id="v2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
					<div class="row table-container">
						<div class="col-sm-12 table-custom">
							<table id="v2" class="table table-bordered table-striped dataTable no-footer" role="grid" aria-describedby="v2_info">
								<thead>
									<tr>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th colspan="2" class="border-left text-center">${currentMonth} (<c:out value="${cons1}"/>)</th>
										<th colspan="2" class="border-left text-center">${nextMonth} (<c:out value="${cons2}"/>)</th>
										<th colspan="2" class="border-left text-center">${lastMonth} (<c:out value="${cons3}"/>)</th>
									</tr>
									<tr>
										<th>Risorsa</th>
										<th>Attivita' (R/F)</th>
										<th>Progetto</th>
										<th>Descrizione</th>
										<th>Costo</th>
										<th>Tariffa</th>
										<th>Valuta</th>
										<th class="border-left">Cons M</th>
										<th>Prod M</th>
										<th class="border-left">Cons M+1</th>
										<th>Prod M+1</th>
										<th class="border-left">Cons M+2</th>
										<th>Prod M+2</th>
									</tr>
								</thead>
								<tbody>	
								<c:forEach items="${list}" var="item">
									<tr onclick="detail(${item.idRecord})" rowId="${item.idRecord}">
									    <td><c:out value="${item.employeeDesc}" /></td>
										<td colname="activity"><c:out value="${item.activityType}" /></td>
										<td><c:out value="${item.projectDesc}" /></td>
										<td colname="custom_desc"><c:out value="${item.customDesc}" /></td>
										<td colname="cost"><c:out value="${item.cost}" /></td>
										<td colname="price"><c:out value="${item.price}" /></td>
										<td colname="currency"><c:out value="${item.currency}" /></td>
										<td colname="cons0" class="border-left"><c:out value="${item.cons0}" /></td>
										<td colname="prod0"><c:out value="${item.prod0}" /></td>
										<td colname="cons1" class="border-left"><c:out value="${item.cons1}" /></td>
										<td colname="prod1"><c:out value="${item.prod1}" /></td>
										<td colname="cons2" class="border-left"><c:out value="${item.cons2}" /></td>
										<td colname="prod2"><c:out value="${item.prod2}" /></td>
									</tr>
								</c:forEach>
								</tbody>
								<tfoot>
									<tr>
										<th>Risorsa</th>
										<th>Attivita' (R/F)</th>
										<th>Progetto</th>
										<th>Descrizione</th>
										<th>Costo</th>
										<th>Tariffa</th>
										<th>Valuta</th>
										<th class="border-left">Cons M</th>
										<th>Prod M</th>
										<th class="border-left">Cons M+1</th>
										<th>Prod M+1</th>
										<th class="border-left">Cons M+2</th>
										<th>Prod M+2</th>
									</tr>								
									<tr>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th> </th>
										<th colspan="2" class="border-left text-center">${currentMonth} (<c:out value="${cons1}"/>)</th>
										<th colspan="2" class="border-left text-center">${nextMonth} (<c:out value="${cons2}"/>)</th>
										<th colspan="2" class="border-left text-center">${lastMonth} (<c:out value="${cons3}"/>)</th>
									</tr>
								</tfoot>								
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />	
</body>

</html>