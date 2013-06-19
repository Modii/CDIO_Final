package web_interface;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import dto.ProduktBatchDTO;
import dto.ReceptDTO;

public class ProduktBatches {
	public void handleAdminiProduktBatch(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/admin/produktbatch/adminiproduktbatch.jsp").forward(request, response);
	}
	public void handleCreateProduktBatch(ServletRequest request, ServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int receptId;
		String receptNavn;
		List<ReceptDTO> receptList = funktionalitetsLaget.getReceptDAO().getReceptList();
		String html = "<select name='receptid'>";
		for (int i=0; i < receptList.size(); i++) {
			receptId = receptList.get(i).getReceptId();
			receptNavn = receptList.get(i).getReceptNavn();
			html += "<option value='"+receptId+"'>"+receptId+" - "+receptNavn+"</option>";
		}
		html +="</select>";
		request.setAttribute("receptList", html);
		request.getRequestDispatcher("/WEB-INF/admin/produktbatch/createproduktbatch.jsp").forward(request, response);
	}
	public void handleShowProduktBatch(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		String html = "<table border=1>";
		html += "<tr><td>Produktbatch ID</td><td>Recept ID</td><td>Dato</td><td>Status</td></tr>";
		int produktbatchid, receptid, status;
		String dato;
		List<ProduktBatchDTO> produktbatch = funktionalitetsLaget.getProduktBatchDAO().getProduktBatchList(); 
		for (int i=0; i < produktbatch.size(); i++) {
			produktbatchid = produktbatch.get(i).getPbId();
			receptid = produktbatch.get(i).getReceptId();
			dato = produktbatch.get(i).getStartDato();
			status = produktbatch.get(i).getStatus();
			html += "<tr><td>"+produktbatchid+"</td><td>"+receptid+"</td><td>"+dato+"</td><td>"+statusToString(status)+"</td></tr>";
		}
		html +="</table>";
		request.setAttribute("list", html);
		request.getRequestDispatcher("/WEB-INF/admin/produktbatch/showproduktbatch.jsp").forward(request, response);
	}
	private String statusToString(int id) {
		String status;
		switch(id) {
		case 0:
			status = "Startet";
			break;
		case 1:
			status = "Under produktion";
			break;
		case 2:
			status = "Afsluttet";
			break;
		default:
			status = "Invalid status";
			break;
		}
		return status;
	}
	
	public void handleCreateProduktBatchSubmit(HttpServletRequest request, HttpServletResponse response, Functionality funktionalitetsLaget) throws ServletException, IOException, DALException {
		int produktbatchid = 0, receptid, status;
		String errorMsg = "", slutDato = null,startDato;
		try {
			produktbatchid = Integer.parseInt(request.getParameter("produktbatchid"));
		}
		catch (NumberFormatException e) {
			errorMsg = "NumberFormatException i produktbatchid! ";
		}
		receptid = Integer.parseInt(request.getParameter("receptid"));
		status = Integer.parseInt(request.getParameter("status"));
		Calendar d = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("00");
		startDato = d.get(Calendar.YEAR) + "-"
		+ df.format(d.get(Calendar.MONTH) + 1) + "-"
		+ df.format(d.get(Calendar.DATE)) + " "
		+ df.format(d.get(Calendar.HOUR_OF_DAY)) + ":"
		+ df.format(d.get(Calendar.MINUTE)) + ":"
		+ df.format(d.get(Calendar.SECOND));
		if (produktbatchid != 0 && funktionalitetsLaget.testPbId(produktbatchid))
			errorMsg = "RåvareBatch ID findes i forvejen!";
		if (errorMsg.length() == 0) {
			funktionalitetsLaget.getProduktBatchDAO().createProduktBatch(new ProduktBatchDTO(produktbatchid,receptid,startDato, slutDato, status));
			request.setAttribute("succes", "Produktbatch oprettet!");
		}
		else {
			request.setAttribute("fail", errorMsg);
		}
		
		int receptId;
		String receptNavn;
		List<ReceptDTO> receptList = funktionalitetsLaget.getReceptDAO().getReceptList();
		String html = "<select name='receptid'>";
		for (int i=0; i < receptList.size(); i++) {
			receptId = receptList.get(i).getReceptId();
			receptNavn = receptList.get(i).getReceptNavn();
			html += "<option value='"+receptId+"'>"+receptId+" - "+receptNavn+"</option>";
		}
		html +="</select>";
		request.setAttribute("receptList", html);
		request.getRequestDispatcher("/WEB-INF/admin/produktbatch/createproduktbatch.jsp").forward(request, response);
	}
	
}
