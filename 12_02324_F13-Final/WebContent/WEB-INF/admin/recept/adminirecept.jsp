<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Administering af recepter</title>
</head>
<body>

	<h1>Recept administration</h1>

	<h3>Du har nu f�lgende valgmuligheder:</h3>

	<form method="POST" action="">
		<input type="submit" value="Opret recept" name="createrecept">
		<br> <input type="submit" value="Vis recepter" name="showrecept">
		<br>
	</form>

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