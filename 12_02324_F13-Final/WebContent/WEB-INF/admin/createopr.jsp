<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Opret bruger</title>
</head>
<body>

	<form method="POST" action="">
		<input type="hidden" value="Opret Operat�r" name="createopr_submit">
		<h1>Opret operat�r</h1>
		Id: <input type="text" name="id"><br>
		Navn: <input type="text" name="navn"><br> 
		Initialer: <input type="text" name="init"><br> 
		CPR-nummer: <input type="text" name="cpr"><br>
		Akt�r: <select name="aktoer">
		<option value="1">Administrator</option>
		<option value="2">Farmaceut</option>
		<option value="3">V�rkf�rer</option>
		<option value="4">Operat�r</option>
		</select>
		
		<input type="submit" value="OK">
	</form>
	${succes}
	${password}


</body>
</html>