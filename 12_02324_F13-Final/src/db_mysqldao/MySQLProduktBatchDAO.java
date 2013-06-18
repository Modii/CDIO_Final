package db_mysqldao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db_connection.Connector;
import dao_interfaces.DALException;
import dao_interfaces.IProduktBatchDAO;
import dto.ProduktBatchDTO;

public class MySQLProduktBatchDAO implements IProduktBatchDAO{

	@Override
	public ProduktBatchDTO getProduktBatch(int pbId) throws DALException {
		ResultSet rs = Connector.doQuery("SELECT * FROM Produktbatch WHERE pbId = " + pbId);
		try{
			if (!rs.first()) throw new DALException("Produktbatchen " + pbId + " findes ikke");
			return new ProduktBatchDTO(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5));
		}
		catch(SQLException e){
			throw new DALException(e);
		}
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		List<ProduktBatchDTO> list = new ArrayList<ProduktBatchDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM Produktbatch ORDER BY pbId");
		try
		{
			while (rs.next()) 
			{
				list.add(new ProduktBatchDTO(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createProduktBatch(ProduktBatchDTO produktbatch)
			throws DALException {

		Connector.doUpdate(
				"INSERT INTO Produktbatch(pbId, receptId, startDato, slutDato, status) VALUES " +
						"(" + produktbatch.getPbId() + ", '" + produktbatch.getReceptId() + 
						"', '" + produktbatch.getStartDato() + "', '" + produktbatch.getSlutDato() + "', '" + produktbatch.getStatus() + "')"
				);

	}

	@Override
	public void updateProduktBatch(ProduktBatchDTO produktbatch)
			throws DALException {

		Connector.doUpdate(
				"UPDATE Produktbatch SET  receptId = '" + produktbatch.getReceptId() + "', " +
						"startDato =  '" + produktbatch.getStartDato() + "', " +
						"slutDato =  '" + produktbatch.getSlutDato() + "', " +
						"status =  '" + produktbatch.getStatus() + "' WHERE pbId = " +
						produktbatch.getPbId());

	}
}
