<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Administering af r�varer</title>
</head>
<body>

	<h1>R�vare administration</h1>

	<h3>Du har nu f�lgende valgmuligheder:</h3>

	<form method="POST" action="">
		<input type="submit" value="Opret r�vare" name="createvare"> <br>
		<input type="submit" value="Vis r�vare" name="showvare"> <br>
		<input type="submit" value="Opdater r�vare" name="updatevare">
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