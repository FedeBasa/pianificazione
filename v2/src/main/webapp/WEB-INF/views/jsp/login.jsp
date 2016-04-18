<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it">
	<head>	
		<jsp:include page="../fragments/head.jsp" />
	</head>
<body>
	<jsp:include page="../fragments/nav.jsp" />	
	<div class="container-custom login">
			<div class="row">
				<div class="col-xs-12 col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
						<div class="form-login">
							<form:form method="POST" class="form-horizontal" modelAttribute="userbean" action="${pageContext.request.contextPath}/login">
						
								<spring:bind path="username">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">Utente</label>
										<div class="col-sm-10">
											<form:input path="username" type="text" class="form-control" placeholder="Username" />
											<form:errors path="username" class="control-label" />
										</div>
									</div>
								</spring:bind>
								
								<spring:bind path="password">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">Password</label>
										<div class="col-sm-10">
											<form:password path="password" class="form-control" placeholder="Password" />
											<form:errors path="password" class="control-label" />
										</div>
									</div>
								</spring:bind>
						
						       <button type="submit" class="btn btn-primary">Entra</button>
						       
							</form:form>							
						</div>
				</div>
			</div>
		</div>
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>