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
		System.out.println("1");
		
		System.out.println("2");
	
	    try {
			ResultSet rs = Connector.doQuery("SELECT * FROM Operatoer WHERE oprId = " + oprId);

	    	System.out.println(rs.getInt(1));
	    	System.out.println(rs.getString(2));
	    	System.out.println(rs.getString(3));
	    	System.out.println(rs.getString(4));
	    	System.out.println(rs.getString(5));
	    	System.out.println(rs.getInt(6));

	    	
	    	
	    	if (!rs.first()) throw new DALException("Operatoeren " + oprId + " findes ikke");
	    	return new OperatoerDTO (rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
	    	
	    }
	    catch (SQLException e) {e.printStackTrace(); throw new DALException(e); }
	    catch (Exception e) {e.printStackTrace(); throw new DALException(e); }
	
		
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
		ResultSet rs = Connector.doQuery("SELECT * FROM Operatoer");
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
	
