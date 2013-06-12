<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Opret råvare</title>
</head>
<body>
	<form method="POST" action="">
		<h1>Opret råvare</h1>
		Nummer: <input type="text" name="id"><br>
		Navn: <input type="text" name="navn"><br> 
		Leverandør: <input type="text" name="init"><br> 
		<input type="submit" value="Opret råvare" name="createraavare_submit">
	</form>
	${succes}

	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit" name="logoff"
			value="Log af">
	</form>
</body>
</html>