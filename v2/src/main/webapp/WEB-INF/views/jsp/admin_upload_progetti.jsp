<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
 
<head>

	<jsp:include page="../fragments/head.jsp" />

<script>
	$(document).ready(function() {
		
		$("#upload").click(function(){
			var path="${pageContext.request.contextPath}/admin/excel/upload/project";
			$("#form2").attr("action",path);
			$("#form2").submit();
		});
		$("#save").click(function(){
			var path="${pageContext.request.contextPath}/admin/excel/save/project";
			$("#form2").attr("action",path);
			$("#form2").submit();
		});
		$("#replace").click(function(){
			var path="${pageContext.request.contextPath}/admin/excel/replace/project";
			$("#form2").attr("action",path);
			$("#form2").submit();
		});
	});
</script>
</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 
    <div class="container-custom">
    
		<div class="btn-group">
		
			<button id="upload" type="button" class="btn btn-primary">Carica File</button>
			<button id="save" type="button" class="btn btn-primary">Salva</button>
			<button id="replace" type="button" class="btn btn-primary">Sostituisci tutto</button>
			
		</div>
    
        <form:form id="form2" method="POST" modelAttribute="uploadProjectBean" enctype="multipart/form-data" class="form-horizontal">
         
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="file">Upload a file</label>
                    <div class="col-md-7">
                        <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                        <div class="has-error">
                            <form:errors path="file" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
     
        </form:form>
        
        <div class="alert alert-info">
        
        	<c:forEach items="${digester.infoMessages}" var="message">
        		<span><c:out value="${message}"/></span>
        	</c:forEach>
        </div>
        
        <c:if test="${not empty digester}">
        	<table class="table table-bordered table-striped">
        		<thead>
        			<tr>
        				<th>IdProgetto</th>
        				<th>Descrizione</th>
        				<th>Cliente</th>
        				<th>Business Unit</th>
        			</tr>
        		</thead>
        		<tbody>
        			<c:forEach items="${digester.list}" var="row">
        			<tr>
						<td><c:out value="${row.idProject}"/></td>
						<td><c:out value="${row.description}"/></td>
						<td><c:out value="${row.customer}"/></td>
						<td><c:out value="${row.businessUnit}"/></td>
        			</tr>
        			</c:forEach>
        		</tbody>
        	</table>
        </c:if>
    </div>
</body>
</html>