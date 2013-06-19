package web_interface;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import dto.RaavareBatchDTO;
import dto.RaavareDTO;

public class RaavareBatches {
	
	public void handleAdminiRaavareBatch(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/raavarebatch/adminiraavarebatch.jsp").forward(request, response);
	}
	public void handleCreateRaavareBatch(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int vareId;
		String vareNavn;
		List<RaavareDTO> oprList = funktionalitetsLaget.getRaavareDAO().getRaavareList();
		String html = "<select name='raavareid'>";
		for (int i=0; i < oprList.size(); i++) {
			vareId = oprList.get(i).getRaavareId();
			vareNavn = oprList.get(i).getRaavareNavn();
			html += "<option value='"+vareId+"'>"+vareId+" - "+vareNavn+"</option>";
		}
		html +="</select>";
		request.setAttribute("vareList", html);
		request.getRequestDispatcher("/WEB-INF/admin/raavarebatch/createraavarebatch.jsp").forward(request, response);
	}
	public void handleShowRaavareBatch(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		String html = "<table border=1>";
		html += "<tr><td>Råvarebatch ID</td><td>Råvare ID</td><td>Mængde</td></tr>";
		int raavarebatchid, raavareid;
		double maengde;
		List<RaavareBatchDTO> raavarebatch = funktionalitetsLaget.getRaavareBatchDAO().getRaavareBatchList(); 
		for (int i=0; i < raavarebatch.size(); i++) {
			raavarebatchid = raavarebatch.get(i).getRbId();
			raavareid = raavarebatch.get(i).getRaavareId();
			maengde = raavarebatch.get(i).getMaengde();
			html += "<tr><td>"+raavarebatchid+"</td><td>"+raavareid+"</td><td>"+maengde+"</td></tr>";
		}
		html +="</table>";
		request.setAttribute("list", html);
		request.getRequestDispatcher("/WEB-INF/admin/raavarebatch/showraavarebatch.jsp").forward(request, response);
	}
	
	public void handleCreateRaavareBatchSubmit(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int raavarebatchid = 0, raavareid = 0;
		double maengde = 0;
		String errorMsg ="";
		try {
			raavarebatchid = Integer.parseInt(request.getParameter("raavarebatchid"));
			raavareid = Integer.parseInt(request.getParameter("raavareid"));
			maengde = Double.parseDouble(request.getParameter("maengde"));
		}
		catch (NumberFormatException e) {
			errorMsg = "NumberFormatException i råvarebatchid, råvareid eller mængden! ";
		}
		if (funktionalitetsLaget.testRaavareBatchId(raavarebatchid))
			errorMsg = "RåvareBatch ID findes i forvejen!";
		if (errorMsg.length() == 0) {
			funktionalitetsLaget.getRaavareBatchDAO().createRaavareBatch(new RaavareBatchDTO(raavarebatchid, raavareid, maengde));
			request.setAttribute("succes", "Råvarebatch oprettet!");
		}
		else {
			request.setAttribute("fail", errorMsg);
		}
		int vareId;
		String vareNavn;
		List<RaavareDTO> oprList = funktionalitetsLaget.getRaavareDAO().getRaavareList();
		String html = "<select name='raavareid'>";
		for (int i=0; i < oprList.size(); i++) {
			vareId = oprList.get(i).getRaavareId();
			vareNavn = oprList.get(i).getRaavareNavn();
			html += "<option value='"+vareId+"'>"+vareId+" - "+vareNavn+"</option>";
		}
		html +="</select>";
		request.setAttribute("vareList", html);
		request.getRequestDispatcher("/WEB-INF/admin/raavarebatch/createraavarebatch.jsp").forward(request, response);
	}

}
