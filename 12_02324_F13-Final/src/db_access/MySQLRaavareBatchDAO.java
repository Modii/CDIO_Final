package db_access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db_connection.Connector;
import dao_interfaces.DALException;
import dao_interfaces.RaavareBatchDAO;
import dto.RaavareBatchDTO;

public class MySQLRaavareBatchDAO implements RaavareBatchDAO{

	@Override
	public RaavareBatchDTO getRaavareBatch(int rbId) throws DALException {
		ResultSet rs = Connector.doQuery("SELECT * FROM Raavarebatch WHERE rbId = " + rbId);
	    try {
	    	if (!rs.first()) throw new DALException("Råvarebatchen " + rbId + " findes ikke");
	    	return new RaavareBatchDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<RaavareBatchDTO> getRaavareBatchList() throws DALException {
		List<RaavareBatchDTO> list = new ArrayList<RaavareBatchDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM Raavarebatch");
		try
		{
			while (rs.next()) 
			{
				list.add(new RaavareBatchDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public List<RaavareBatchDTO> getRaavareBatchList(int raavareId)
			throws DALException {
		List<RaavareBatchDTO> list = new ArrayList<RaavareBatchDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM Raavarebatch WHERE raavareId = " + raavareId);
		try
		{
			while (rs.next()) 
			{
				list.add(new RaavareBatchDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createRaavareBatch(RaavareBatchDTO RaavareBatch)
			throws DALException {
		Connector.doUpdate(
				"INSERT INTO Raavarebatch(rbId, raavareId, maengde) VALUES " +
						"(" + RaavareBatch.getRbId() + ", '" + RaavareBatch.getRaavareId() + "', '" +
						RaavareBatch.getMaengde() + "')");
	}

	@Override
	public void updateRaavareBatch(RaavareBatchDTO RaavareBatch)
			throws DALException {
		Connector.doUpdate(
				"UPDATE Raavarebatch SET raavareId =  '" + RaavareBatch.getRaavareId() + 
				"', maengde = '" + RaavareBatch.getMaengde() + "' WHERE rbId = " +
				RaavareBatch.getRbId());

	}

}
