<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="it">
<head>
<title>Pianificazione Risorse</title>
</head>
<body>
	<table>
		<tr>
			<td>Matricola :</td>
			<td>${badgeNumber}</td>
		</tr>
		<tr>
			<td>idProgetto :</td>
			<td>${idProject}</td>
		</tr>
		<tr>
			<td>Cliente :</td>
			<td>${customer}</td>
		</tr>
			<tr>
			<td>Attività :</td>
			<td>${activityType}</td>
		</tr>
		<tr>
			<td>Valuta :</td>
			<td>${currency}</td>
		</tr>
		<tr>
			<td>Tariffa :</td>
			<td>${price}</td>
		</tr>
		<tr>
			<td>cons0 :</td>
			<td>${cons0}</td>
		</tr>
		<tr>
			<td>prod0 :</td>
			<td>${prod0}</td>
		</tr>
		<tr>
			<td>cons1 :</td>
			<td>${cons1}</td>
		</tr>
		<tr>
			<td>prod1</td>
			<td>${prod1}</td>
		</tr>
		<tr>
			<td>cons2:</td>
			<td>${cons2}</td>
		</tr>
		<tr>
			<td>prod2 :</td>
			<td>${prod2}</td>
		</tr>
	</table>
</body>