import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import db_connection.Connector;

public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Servlet(){
		super();
	}

	public void init(ServletConfig config) throws ServletException{

	}
	
	public void destroy(){
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try { new Connector(); } 
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e) { e.printStackTrace(); }
		catch (SQLException e) { e.printStackTrace(); }
		Functionality funktionalitetsLaget = new Functionality();
		if (request.getParameter("login") != null && request.getParameter("login").equals("Log ind")) {
			int id = Integer.parseInt(request.getParameter("Id"));
			try {
				if (funktionalitetsLaget.testId(id)) { // checker om ID findes i DB.
					System.out.println("Succes!");
					request.getRequestDispatcher("admin.jsp").forward(request, response);
				}
				else {
					request.setAttribute("error", "Unknown user, please try again");
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
			} catch (DALException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (request.getParameter("blalbla") != null && request.getParameter("blalbla").equals("blabla")) {
			request.getRequestDispatcher("/operator.jsp").forward(request, response);
		}
	}

}
