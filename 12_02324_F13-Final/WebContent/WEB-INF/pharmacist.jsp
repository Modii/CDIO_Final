<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pharma</title>
</head>
<body>
<h1>Farmaceut side</h1>

<h3>Du har nu følgende valgmuligheder:</h3>

	<form method="POST" action="pharmacist.jsp">
		<input type="submit" value="Administrere råvarer"> <br>
		<input type="submit" value="Administrere recepter"> <br>
		<input type="submit" value="Administrere råvarebatches"> <br>
		<input type="submit" value="Administrere produktbatches"> <br>
		<input type="submit" value="Foretage afvejning">
			</form>
			
	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit" name="logoff"
			value="Log af">
	</form>
</body>
</html>