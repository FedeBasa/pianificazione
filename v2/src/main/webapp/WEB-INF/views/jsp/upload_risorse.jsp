<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
 
<head>

	<jsp:include page="../fragments/head.jsp" />
</head>
<jsp:include page="../fragments/nav.jsp" />
<body> 
 
    <div class="container">
        <form:form method="POST" modelAttribute="uploadBean" enctype="multipart/form-data" class="form-horizontal">
         
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
     
            <div class="row">
                <div class="form-actions floatRight">
                    <input type="submit" value="Upload" class="btn btn-primary btn-sm">
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
        				<th>Matricola</th>
        				<th>Nome</th>
        				<th>Cognome</th>
        			</tr>
        		</thead>
        		<tbody>
        			<c:forEach items="${digester.list}" var="row">
        			<tr>
						<td><c:out value="${row.badgeNumber}"/></td>
						<td><c:out value="${row.name}"/></td>
						<td><c:out value="${row.surname}"/></td>
        			</tr>
        			</c:forEach>
        		</tbody>
        	</table>
        </c:if>
    </div>
</body>
</html>