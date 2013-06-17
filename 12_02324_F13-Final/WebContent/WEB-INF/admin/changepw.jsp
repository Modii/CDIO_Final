<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ændr password</title>
</head>
<body>

	<form method="POST" action="">
		<h1>Ændr password</h1>
		Password: <input type="text" name="password"><br>
		<br> <input type="submit" value="Ændr password"
			name="changepw_submit">
	</form>
	<br>
		${succes} ${fail}

</body>
</html>