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

	<h3>Hej ${sessionScope.operatoerNavn}, du har nu f�lgende
		valgmuligheder:</h3>

	<form method="POST" action="">
		<input type="submit" value="Administrere r�varer" name="adminivare">
		<br> <input type="submit" value="Administrer recepter"
			name="adminirecept"> <br> <input type="submit"
			value="Administrer r�varebatches" name="adminiraavarebatch">
		<br> <input type="submit" value="Administrer produktbatches"
			name="adminiproduktbatch"><br>
			<input type="submit" value="�ndr password" name="changepw">
	</form>

	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit"
			name="logoff" value="Log af">
	</form>
</body>
</html>