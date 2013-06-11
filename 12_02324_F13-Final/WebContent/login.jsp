<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-- <jsp:useBean id="funktionalitetsLaget" --%>
<%-- 	class="businessLogic_layer.Functionality" scope="session" /> --%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login page</title>
</head>
<body>

<h5>LOGIN</h5>

	<form method="POST" action="LogIn.jsp">
		Id: <input type="text" name="Id"><br>
		Password: <input type="text" name="Password"><br>
		<input type="submit" value="Log ind">
	</form>


</body>
</html>