<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Opret r�vare</title>
</head>
<body>
	<form method="POST" action="">
		<h1>Opret r�vare</h1>
		Id: <input type="text" name="id"><br>
		Navn: <input type="text" name="navn"><br> 
		Leverand�r: <input type="text" name="leverandoer"><br> 
		<input type="submit" value="Opret r�vare" name="createraavare_submit">
	</form>
	${succes}

	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit" name="hovedmenu"
			value="Tilbage til hovedmenu">
	</form>
	<form action="" method="POST">
		<br><input type="submit" name="logoff"
			value="Log af">
	</form>
</body>
</html>