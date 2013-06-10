package db_mysqldao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db_connection.Connector;
import dao_interfaces.DALException;
import dao_interfaces.ProduktBatchDAO;
import dto.ProduktBatchDTO;

public class MySQLProduktBatchDAO implements ProduktBatchDAO{

	@Override
	public ProduktBatchDTO getProduktBatch(int pbId) throws DALException {
		ResultSet rs = Connector.doQuery("SELECT * FROM Produktbatch WHERE pbId = " + pbId);
		try{
			if (!rs.first()) throw new DALException("Produktbatchen " + pbId + " findes ikke");
			return new ProduktBatchDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3));
		}
		catch(SQLException e){
			throw new DALException(e);
		}
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		List<ProduktBatchDTO> list = new ArrayList<ProduktBatchDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM Produktbatch");
		try
		{
			while (rs.next()) 
			{
				list.add(new ProduktBatchDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createProduktBatch(ProduktBatchDTO produktbatch)
			throws DALException {

		Connector.doUpdate(
				"INSERT INTO Produktbatch(pbId, receptId, status) VALUES " +
						"(" + produktbatch.getPbId() + ", '" + produktbatch.getReceptId() + 
						"', '" + produktbatch.getStatus() + "')"
				);

	}

	@Override
	public void updateProduktBatch(ProduktBatchDTO produktbatch)
			throws DALException {

		Connector.doUpdate(
				"UPDATE Produktbatch SET  receptId = '" + produktbatch.getReceptId() + "', " +
						"status =  '" + produktbatch.getStatus() + "' WHERE pbId = " +
						produktbatch.getPbId());

	}
}
