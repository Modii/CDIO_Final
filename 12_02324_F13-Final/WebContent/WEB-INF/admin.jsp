<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
</head>
<body>
<h1>ADMIN SIDE</h1>

<h3>Du har nu f�lgende valgmuligheder:</h3>

	<form method="POST" action="">
		<input type="submit" value="Opret bruger" name="createopr"> <br>
		<input type="submit" value="Vis brugere" name="showopr"> <br>
		<input type="submit" value="Slet bruger" name="removeopr"> <br>
		<input type="submit" value="Opdat�r bruger" name="updateopr"> <br>
	
		<input type="submit" value="Administrere r�varer"> <br>
		<input type="submit" value="Administrere recepter"> <br>
		<input type="submit" value="Administrere r�varebatches"> <br>
		<input type="submit" value="Administrere produktbatches"> <br>
		
		<input type="submit" value="Foretage afvejning">
			</form>
			
	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit" name="logoff"
			value="Log af">
	</form>
</body>
</html>