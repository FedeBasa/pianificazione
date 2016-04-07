<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it">
<head>
<!-- CSS file -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/EasyAutocomplete-1.3.3/easy-autocomplete.min.css">
<script
	src="${pageContext.request.contextPath}/webjars/jquery/1.11.1/jquery.js">
</script>
<script
	src="${pageContext.request.contextPath}/resources/EasyAutocomplete-1.3.3/jquery.easy-autocomplete.js"></script>
<script>
	$(document).ready(function() {
		
		function getUrlVars() {
		    var vars = {};
		    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		        vars[key] = value;
		    });
		    return vars;
		}
		
		$("#bottone").click(function(){
			var path="${pageContext.request.contextPath}/send/insert";
			console.log(path);
			$("#v2Form").attr("action",path);
		});
		$("#delete").click(function(){
			var path = "${pageContext.request.contextPath}/send/delete";
			console.log(path);
			$("#v2Form").attr("action",path);
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
				return "${pageContext.request.contextPath}/autocomplete/progetto";
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
				});
	}

</script>
<title>Spring MVC</title>
</head>
<body>
<button id="download" onclick="tableToExcel('v2','v2')">Export</button>
	<table id="v2">
		<tr>
			<th>Risorsa</th>
			<th>Attività</th>
			<th>Progetto</th>
			<th>Tariffa</th>
			<th>Valuta</th>
			<th>Consolidato mese 1</th>
			<th>Prodotto mese 1</th>
			<th>Consolidato mese 2</th>
			<th>Prodotto mese 2</th>
			<th>Consolidato mese 3</th>
			<th>Prodotto mese 3</th>
		</tr>

		<c:forEach items="${list}" var="item">
			<tr>
			<td><a href="#" onclick="detail(${item.idRecord})"><c:out
							value="${item.employeeDesc}" /></a></td>
				<td><c:out value="${item.activityType}" /></td>
				<td><c:out value="${item.projectDesc}" /></td>
				<td><c:out value="${item.price}" /></td>
				<td><c:out value="${item.currency}" /></td>
				<td><c:out value="${item.cons0}" /></td>
				<td><c:out value="${item.prod0}" /></td>
				<td><c:out value="${item.cons1}" /></td>
				<td><c:out value="${item.prod1}" /></td>
				<td><c:out value="${item.cons2}" /></td>
				<td><c:out value="${item.prod2}" /></td>
			</tr>
		</c:forEach>
	</table>
	
	<form:form method="POST" modelAttribute="v2Form" action="${pageContext.request.contextPath}/send/data">
	    <form:hidden path="month"/>
	    <form:hidden path="idRecord"/>
	    <form:hidden path="idProject"/>
		<form:hidden path="badgeNumber"/>
		Risorsa :<input id="employeeDesc" type="text" /><br>
		<form:hidden path="idProject"/>
		Progetto : <input type="text" id= "projectDesc" size="30" /><br>
		Cliente: <form:input path="customer" type="text"  size="30" readonly="true" cssStyle="background-color:lightgrey"/><br>
		Attività : <form:input path="activityType" type="text"  size="30" readonly="true" cssStyle="background-color:lightgrey"/><br>
		Valuta : <form:input path="currency" type="text"  size="30" readonly="true" cssStyle="background-color:lightgrey"/><br>
		Tariffa : <form:input path="price" type="text" size="30" onfocus="this.value=''"/><br>
		Consolidato mese 1: <form:input path="cons0" type="text" size="30" onfocus="this.value=''"/><br>
		Prodotto mese 1: <form:input path="prod0" type="text" size="30" onfocus="this.value=''"/><br>
		Consolidato mese 2: <form:input path="cons1" type="text" size="30" onfocus="this.value=''"/><br>
		Prodotto mese 2: <form:input path="prod1" type="text" size="30" onfocus="this.value=''"/><br>
		Consolidato mese 3: <form:input path="cons2" type="text" size="30" onfocus="this.value=''" /><br>
		Prodotto mese 3: <form:input path="prod2" type="text" size="30" onfocus="this.value=''" /><br>
       <input type="submit" value="Aggiorna V2" /><input type ="submit" id ="delete" value ="cancella record"/><input type="submit" id="bottone" value = "Inserisci record"/>
	</form:form>
</body>
</html>