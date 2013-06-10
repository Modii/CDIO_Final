package db_access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db_connection.Connector;
import dao_interfaces.DALException;
import dao_interfaces.ReceptKompDAO;
import dto.ReceptKompDTO;

public class MySQLReceptKompDAO implements ReceptKompDAO {

	@Override
	public ReceptKompDTO getReceptKomp(int receptId, int raavareId)
			throws DALException {
		ResultSet rs = Connector.doQuery("SELECT * FROM ReceptKomp WHERE receptId = " + receptId +
				" AND raavareId = " + raavareId);
		try {
			if (!rs.first()) throw new DALException("Receptkomponenten med receptId: " +
					receptId + " og raavareId: " + raavareId + " findes ikke");
			return new ReceptKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4));
		}
		catch (SQLException e){
			throw new DALException(e); 
		}
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList(int receptId)
			throws DALException {
		List<ReceptKompDTO> list = new ArrayList<ReceptKompDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM ReceptKomp WHERE receptId = " + receptId);
		try
		{
			while (rs.next()) 
			{
				list.add(new ReceptKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList() throws DALException {
		List<ReceptKompDTO> list = new ArrayList<ReceptKompDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM ReceptKomp");
		try
		{
			while (rs.next()) 
			{
				list.add(new ReceptKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createReceptKomp(ReceptKompDTO receptkomponent)
			throws DALException {
		Connector.doUpdate(
				"INSERT INTO ReceptKomp(receptId, raavareId, nomNetto, tolerance) VALUES " +
						"(" + receptkomponent.getReceptId() + ", '" + receptkomponent.getRaavareId() + "', '" +
						receptkomponent.getNomNetto() + "', '" + receptkomponent.getTolerance() + "')"
				);
	}

	@Override
	public void updateReceptKomp(ReceptKompDTO receptkomponent)
			throws DALException {
		Connector.doUpdate(
				"UPDATE ReceptKomp SET nomNetto = '" + receptkomponent.getNomNetto() + "', tolerance =  '" + receptkomponent.getTolerance() + "' WHERE receptId = " +
						receptkomponent.getReceptId() + " AND raavareId = " + receptkomponent.getRaavareId()
				);
	}

}
