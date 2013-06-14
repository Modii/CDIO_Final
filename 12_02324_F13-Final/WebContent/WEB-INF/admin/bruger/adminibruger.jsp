<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Administering af brugere</title>
</head>
<body>

	<h1>Bruger administration</h1>

	<h3>Du har nu følgende valgmuligheder:</h3>

	<form method="POST" action="">
		<input type="submit" value="Opret bruger" name="createopr"> <br>
		<input type="submit" value="Vis brugere" name="showopr"> <br>
		<input type="submit" value="Opdatér bruger" name="updateopr"><br>
		<input type="submit" value="Slet bruger" name="removeopr">

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