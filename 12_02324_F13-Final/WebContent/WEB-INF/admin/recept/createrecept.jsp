<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Opret recept</title>
</head>
<body>
	${succes}
	<form method="POST" action="">
		<h1>Opret recept</h1>
		ReceptId: <input type="text" name="receptid"><br>
		Navn: <input type="text" name="receptnavn"><br> 
		<h2>Receptkomponenter</h2>
		RåvareId:<input type="text" name="raavareid1"><br>
		Nominel netto vægt (kg): <input type="text" name="nomnetto1"><br>
		Tolerance (0.1-10%): <input type="text" name="tolerance1"><br><br>
		
		RåvareId:<input type="text" name="raavareid2"><br>
		Nominel netto vægt (kg): <input type="text" name="nomnetto2"><br>
		Tolerance (0.1-10%): <input type="text" name="tolerance2"><br><br>
		
		RåvareId:<input type="text" name="raavareid3"><br>
		Nominel netto vægt (kg): <input type="text" name="nomnetto3"><br>
		Tolerance (0.1-10%): <input type="text" name="tolerance3"><br><br>
		
		RåvareId:<input type="text" name="raavareid4"><br>
		Nominel netto vægt (kg): <input type="text" name="nomnetto4"><br>
		Tolerance (0.1-10%): <input type="text" name="tolerance4"><br><br>
		
		RåvareId:<input type="text" name="raavareid5"><br>
		Nominel netto vægt (kg): <input type="text" name="nomnetto5"><br>
		Tolerance (0.1-10%): <input type="text" name="tolerance5"><br>
		<input type="submit" value="Opret recept" name="createrecept_submit">
	</form>

	<form action="" method="POST">
		<br> <br> <br> <br> <input type="submit" name="logoff"
			value="Log af">
	</form>

</body>
</html>