import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import db_connection.Connector;
import dto.OperatoerDTO;

public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Servlet(){
		super();
	}

	public void init(ServletConfig config) throws ServletException{
		try { new Connector(); } 
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e) { e.printStackTrace(); }
		catch (SQLException e) { e.printStackTrace(); }
	}

	public void destroy(){

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Functionality funktionalitetsLaget = new Functionality();

		if (request.getParameter("login") != null && request.getParameter("login").equals("Log ind")) 
			handleLogIn(request, response, funktionalitetsLaget);
		if (request.getParameter("logoff") != null && request.getParameter("logoff").equals("Log af"))
			handleLogOff(request, response);
		if (request.getParameter("createopr") != null && request.getParameter("createopr").equals("Opret bruger"))
			handleCreateOpr(request, response, funktionalitetsLaget);
		if (request.getParameter("showopr") != null && request.getParameter("showopr").equals("Vis brugere"))
			handleShowOpr(request, response, funktionalitetsLaget);
		if (request.getParameter("removeopr") != null && request.getParameter("removeopr").equals("Slet bruger"))
			handleRemoveOpr(request, response, funktionalitetsLaget);
		if (request.getParameter("updateopr") != null && request.getParameter("updateopr").equals("Opdatér bruger"))
			handleUpdateOpr(request, response, funktionalitetsLaget);
		if (request.getParameter("createopr_submit") != null && request.getParameter("createopr_submit").equals("Opret Operatør")){
			try {
				handleCreateOprSubmit(request, response, funktionalitetsLaget);
			} catch (DALException e) {
				e.getMessage();
			}
			request.setAttribute("succes", "Operatør oprettet!");
			request.getRequestDispatcher("/WEB-INF/admin/createopr.jsp").forward(request, response);
		}

	}

	private void handleLogIn(ServletRequest request, ServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("Id"));
		String pw = request.getParameter("Password");
		try {
			if (funktionalitetsLaget.testId(id)) { // checker om ID findes i DB.
				if(funktionalitetsLaget.testPassword(id, pw)){
					if(funktionalitetsLaget.getDataLaget().getOperatoer(id).getAktoer() == 1)
						request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
					else if(funktionalitetsLaget.getDataLaget().getOperatoer(id).getAktoer() == 2)
						request.getRequestDispatcher("/WEB-INF/pharmacist.jsp").forward(request, response);
					else if(funktionalitetsLaget.getDataLaget().getOperatoer(id).getAktoer() == 3)
						request.getRequestDispatcher("/WEB-INF/supervisor.jsp").forward(request, response);
					else
						request.getRequestDispatcher("/WEB-INF/operator.jsp").forward(request, response);
				}
			}
			else {
				request.setAttribute("error", "Unknown user, please try again");
				request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
			}
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void handleLogOff(ServletRequest request, ServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}
	private void handleCreateOpr(ServletRequest request, ServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/createopr.jsp").forward(request, response);
	}
	private void handleShowOpr(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/showopr.jsp").forward(request, response);
	}
	private void handleRemoveOpr(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/removeopr.jsp").forward(request, response);
	}
	private void handleUpdateOpr(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/updateopr.jsp").forward(request, response);
	}

	private void handleCreateOprSubmit(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int id = Integer.parseInt(request.getParameter("id"));
		String navn = request.getParameter("navn");
		String init = request.getParameter("init");
		String cpr = request.getParameter("cpr");
		String newPw = funktionalitetsLaget.generatePassword();
		int aktoer = Integer.parseInt(request.getParameter("aktoer"));
		funktionalitetsLaget.getDataLaget().createOperatoer(new OperatoerDTO(id, navn, init, cpr, newPw, aktoer));
		request.setAttribute("password", "<br>Brugerens password er: " + newPw);

	}

}
