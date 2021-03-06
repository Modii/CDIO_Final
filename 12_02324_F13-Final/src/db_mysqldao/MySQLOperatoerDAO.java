package db_mysqldao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import db_connection.Connector;

import dao_interfaces.DALException;
import dao_interfaces.IOperatoerDAO;
import dto.OperatoerDTO;

public class MySQLOperatoerDAO implements IOperatoerDAO {
	
	
	public OperatoerDTO getOperatoer(int oprId) throws DALException {
	    try {
			ResultSet rs = Connector.doQuery("SELECT * FROM Operatoer WHERE oprId = " + oprId);	    	
	    	
	    	if (!rs.first()) throw new DALException("Operatoeren " + oprId + " findes ikke");
	    	return new OperatoerDTO (rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
	    	
	    }
	    catch (SQLException e) {e.printStackTrace(); throw new DALException(e); }
	    catch (Exception e) {throw new DALException(e); }
	}
	public OperatoerDTO getHighestOprID() throws DALException {
		try {
			ResultSet rs = Connector.doQuery("SELECT * FROM Operatoer ORDER BY Operatoer.oprId DESC LIMIT 0,1");
			if (!rs.first()) throw new DALException("Der findes ingen operatør");
	    	return new OperatoerDTO (rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
	    	
	    }
	    catch (SQLException e) {e.printStackTrace(); throw new DALException(e); }
	    catch (Exception e) {throw new DALException(e); }
	}
	
	public void createOperatoer(OperatoerDTO opr) throws DALException {		
			Connector.doUpdate(
				"INSERT INTO Operatoer(oprId, oprNavn, ini, cpr, password, aktoer) VALUES " +
				"(" + opr.getOprId() + ", '" + opr.getOprNavn() + "', '" + opr.getIni() + "', '" + 
				opr.getCpr() + "', '" + opr.getPassword() +  "', '" + opr.getAktoer() + "')"
			);
	}
	
	public void updateOperatoer(OperatoerDTO opr) throws DALException {
		Connector.doUpdate(
				"UPDATE Operatoer SET  oprNavn = '" + opr.getOprNavn() + "', ini =  '" + opr.getIni() + 
				"', cpr = '" + opr.getCpr() + "', password = '" + opr.getPassword() + "', aktoer = '" + opr.getAktoer() + "' WHERE oprId = " +
				opr.getOprId()
		);
	}
	
	
	public void removeOperatoer(OperatoerDTO opr) throws DALException {
		Connector.doUpdate(
				"DELETE FROM Operatoer WHERE oprId = " + opr.getOprId());
	}
	
	
	public List<OperatoerDTO> getOperatoerList() throws DALException {
		List<OperatoerDTO> list = new ArrayList<OperatoerDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM Operatoer ORDER BY oprId ASC");
		try
		{
			while (rs.next()) 
			{
				list.add(new OperatoerDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}
		
		
}
	
