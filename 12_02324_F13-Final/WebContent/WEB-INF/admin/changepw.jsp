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
		Gammelt password: <input type="text" name="gammeltpassword"><br>
		Nyt Password: <input type="text" name="nytpassword1"><br>
		Nyt Password (igen): <input type="text" name="nytpassword2"><br>
		<br> <input type="submit" value="Ændr password"
			name="changepw_submit">
	</form>
	<br>
		${succes} ${fail}<br><br><br>
		Din nye adgangskode skal opfylde følgende regler:
<br><br>
Adgangskoden skal indeholde mindst 6 tegn af mindst tre af de følgende fire kategorier: små bogstaver ('a' til 'z'), store bogstaver ('A' til 'Z'), cifre ('0' til '9') og specialtegn (som defineret herunder).
<br><br>
Undgå at bruge dit fornavn, efternavn eller bruger-ID som en del af din adgangskode, da dette vil medføre problemer med at logge ind på nogle systemer og tjenester på DTU, især Windows-tjenester.
<br><br>
Anvend blot følgende special-tegn: {'.', '-', '_', '+', '!', '?', '='}
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