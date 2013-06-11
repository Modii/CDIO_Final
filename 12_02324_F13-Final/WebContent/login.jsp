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
<body>

	<h5>LOGIN</h5>

	<form method="POST" action="login.jsp">
		Id: <input type="text" name="Id"><br> Password: <input
			type="text" name="Password"><br> <input type="submit"
			value="Log ind">
	</form>
	<%@ page import="db_connection.Connector"%>
	<%
		new Connector();
		String fejlMsg = "";
		int id = 0;
		String password = "";
		int loggedInAs;
		
		if (request.getMethod().equals("POST")) { // brugeren har tastet på submit
		
				id = Integer.parseInt(request.getParameter("Id"));
				password = request.getParameter("Password");

				if (funktionalitetsLaget.testId(id)) { // checker om ID findes i DB.
					if (funktionalitetsLaget.testPassword(id, password)) { // tjekker om PW matcher ID
						if(funktionalitetsLaget.getDataLaget().getOperatoer(id).getAktoer() == 1)
							response.sendRedirect("admin.jsp");
						else if(funktionalitetsLaget.getDataLaget().getOperatoer(id).getAktoer() == 2)
							response.sendRedirect("pharmacist.jsp");
						else if(funktionalitetsLaget.getDataLaget().getOperatoer(id).getAktoer() == 3)
							response.sendRedirect("supervisor.jsp");
						else
							response.sendRedirect("operator.jsp");
				}
			}
		}
	%>


</body>
</html>