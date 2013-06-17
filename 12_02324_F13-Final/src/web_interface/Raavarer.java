package web_interface;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import dto.RaavareDTO;

public class Raavarer {


	public void handleAdminiVare(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/raavare/adminivare.jsp").forward(request, response);
	}
	public void handleCreateVare(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/raavare/createvare.jsp").forward(request, response);
	}
	public void handleShowVare(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		String html = "<table border=1>";
		html += "<tr><td>ID</td><td>Navn</td><td>Leverandør</td></tr>";
		int id;
		String navn,leverandoer;
		List<RaavareDTO> temp = funktionalitetsLaget.getRaavareDAO().getRaavareList(); 
		for (int i=0; i < temp.size(); i++) {
			id = temp.get(i).getRaavareId();
			navn = temp.get(i).getRaavareNavn();
			leverandoer = temp.get(i).getLeverandoer();
			html += "<tr><td>"+id+"</td><td>"+navn+"</td><td>"+leverandoer+"</td></tr>";
		}
		html +="</table>";
		request.setAttribute("list", html);
		request.getRequestDispatcher("/WEB-INF/admin/raavare/showvare.jsp").forward(request, response);
	}
	public void handleUpdateVare(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int vareId;
		String vareNavn;
		List<RaavareDTO> oprList = funktionalitetsLaget.getRaavareDAO().getRaavareList();
		String html = "<select name='id'>";
		for (int i=0; i < oprList.size(); i++) {
			vareId = oprList.get(i).getRaavareId();
			vareNavn = oprList.get(i).getRaavareNavn();
			html += "<option value='"+vareId+"'>"+vareId+" - "+vareNavn+"</option>";
		}
		html +="</select>";
		request.setAttribute("vareList", html);
		request.getRequestDispatcher("/WEB-INF/admin/raavare/updatevare.jsp").forward(request, response);
	}
	public void handleCreateVareSubmit(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int id = Integer.parseInt(request.getParameter("id"));
		String navn = request.getParameter("navn");
		String leverandoer = request.getParameter("leverandoer");
		funktionalitetsLaget.getRaavareDAO().createRaavare(new RaavareDTO(id, navn, leverandoer));
		request.setAttribute("succes", "Råvare oprettet!");
		request.getRequestDispatcher("/WEB-INF/admin/raavare/createvare.jsp").forward(request, response);
	}
	public void handleUpdateVareSubmit(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int id = Integer.parseInt(request.getParameter("id"));
		String navn = request.getParameter("navn");
		String leverandoer = request.getParameter("leverandoer");
		funktionalitetsLaget.getRaavareDAO().updateRaavare(new RaavareDTO(id, navn, leverandoer));
		
		int vareId;
		String vareNavn;
		List<RaavareDTO> oprList = funktionalitetsLaget.getRaavareDAO().getRaavareList();
		String html = "<select name='id'>";
		for (int i=0; i < oprList.size(); i++) {
			vareId = oprList.get(i).getRaavareId();
			vareNavn = oprList.get(i).getRaavareNavn();
			html += "<option value='"+vareId+"'>"+vareId+" - "+vareNavn+"</option>";
		}
		html +="</select>";
		request.setAttribute("vareList", html);
		request.setAttribute("succes", "Råvare opdateret!");
		request.getRequestDispatcher("/WEB-INF/admin/raavare/updatevare.jsp").forward(request, response);
	}
	
}
