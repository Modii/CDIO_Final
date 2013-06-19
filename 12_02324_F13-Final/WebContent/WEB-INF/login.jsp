<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="funktionalitetsLaget"
	class="businessLogic_layer.Functionality" scope="session" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login page</title>
</head>
<body style="background-size: 100% 100%; background-repeat: no-repeat;">
	<div id="middlebox" style="width: 150px; height: 150px;
	background-color: white;
-webkit-border-radius: 5px;
border-radius: 5px;
-webkit-box-shadow: 0px 0px 15px 2px rgba(0, 0, 0, 0.13);
box-shadow: 0px 0px 15px 2px rgba(0, 0, 0, 0.13);
padding: 30px;
padding-top: 5px;
margin-left: auto;
margin-right: auto;
margin-top: 160px;
border-style: solid;
border-color: #c0c0c0;
border-width: 1px;
text-align: left;">
	<h3>LOGIN</h3>

	<form method="POST" action="">
		Id: <br><input type="text" name="Id"><br>
		Password: <br><input
			type="password" name="Password">
			<br> <input
			type="submit" value="Log ind" name="login">
	</form>
	${error}
	</div>
</body>
</html>