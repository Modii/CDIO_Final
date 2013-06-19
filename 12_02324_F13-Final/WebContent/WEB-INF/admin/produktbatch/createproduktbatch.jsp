<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Opret produktbatch</title>
</head>
<body>
	<form method="POST" action="">
		<h1>Opret produktbatch</h1>
		Produktbatch ID: <input type="text" name="produktbatchid"><br>
		Recept ID: ${receptList}<br>
		Status: <select name="status">
			<option value="0">Startet</option>
			<option value="1">Under produktion</option>
			<option value="2">Afsluttet</option>
		</select><br> <input type="submit" value="Opret produktbatch"
			name="createproduktbatch_submit">
	</form>
	${succes} ${fail}

	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit"
			name="hovedmenu" value="Tilbage til hovedmenu">
	</form>
	<form action="" method="POST">
		<br>
		<input type="submit" name="logoff" value="Log af">
	</form>
</body>
</html>