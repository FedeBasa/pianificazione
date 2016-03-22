<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="it">
<head>
<title>Spring MVC</title>

<link href="${pageContext.request.contextPath}/resources/core/css/select2.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resources/core/js/jquery-2.2.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/core/js/select2.full.js"></script>

<script>

	$(document).ready(function () {
		
		$(".js-data-example-ajax").select2({
			ajax : {
				url : "${pageContext.request.contextPath}/autocomplete/employee",
				dataType : 'json',
				delay : 250,
				data : function(params) {
					return {
						q : params.term, // search term
						page : params.page
					};
				},
				processResults : function(data, params) {
					console.log(data);
					// parse the results into the format expected by Select2
					// since we are using custom formatting functions we do not need to
					// alter the remote JSON data, except to indicate that infinite
					// scrolling can be used
					params.page = params.page || 1;
	
					return {
						results : data,
						pagination : {
							more : (params.page * 30) < data.total_count
						}
					};
				},
				cache : true
			},
			escapeMarkup : function(markup) {
				return markup;
			}, // let our custom formatter work
			minimumInputLength : 1,
			templateResult: formatResult,
			templateSelection: formatSelection
		});
	})
	
	function formatResult(repo) {
		return repo.name + " " + repo.surname;
	}
	
	function formatSelection(repo) {
		return repo.name;
	}
	
</script>
</head>
<body>

	<select class="js-data-example-ajax" style="width:300px">
		<option value="1">&nbsp;</option>
	</select>

</body>
</html>