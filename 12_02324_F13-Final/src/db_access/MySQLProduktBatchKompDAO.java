package db_access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db_connection.Connector;
import dao_interfaces.DALException;
import dao_interfaces.ProduktBatchKompDAO;
import dto.ProduktBatchKompDTO;

public class MySQLProduktBatchKompDAO implements ProduktBatchKompDAO {

	@Override
	public ProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId)
			throws DALException {
		ResultSet rs = Connector.doQuery("SELECT * FROM ProduktbatchKomp WHERE pbId = " + pbId +
				" AND rbId = " + rbId);
		try {
			if (!rs.first()) throw new DALException("Produktbatchkomponenten med pbId: " +
					pbId + " og rbId: " + rbId + " findes ikke");
			return new ProduktBatchKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5));
		}
		catch (SQLException e){
			throw new DALException(e); 
		}
	}

	@Override
	public List<ProduktBatchKompDTO> getProduktBatchKompList(int pbId)
			throws DALException {
		List<ProduktBatchKompDTO> list = new ArrayList<ProduktBatchKompDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM ProduktbatchKomp WHERE pbId = " + pbId);
		try
		{
			while (rs.next()) 
			{
				list.add(new ProduktBatchKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public List<ProduktBatchKompDTO> getProduktBatchKompList()
			throws DALException {
		List<ProduktBatchKompDTO> list = new ArrayList<ProduktBatchKompDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM ProduktbatchKomp");
		try
		{
			while (rs.next()) 
			{
				list.add(new ProduktBatchKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent)
			throws DALException {
		Connector.doUpdate(
				"INSERT INTO ProduktbatchKomp(pbId, rbId, oprId, tara, netto) VALUES " +
						"(" + produktbatchkomponent.getPbId() + ", '" + produktbatchkomponent.getRbId() + "', '" +
						produktbatchkomponent.getOprId() + "', '" + 
						produktbatchkomponent.getTara() + "', '" + produktbatchkomponent.getNetto() + "')"
				);

	}

	@Override
	public void updateProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent)
			throws DALException {
		Connector.doUpdate(
				"UPDATE ProduktbatchKomp SET  oprId = '" + produktbatchkomponent.getOprId() +
				"', tara =  '" + produktbatchkomponent.getTara() + 
				"', netto = '" + produktbatchkomponent.getNetto() + "' WHERE pbId = " +
				produktbatchkomponent.getPbId() + " AND rbId = " + produktbatchkomponent.getRbId()
		);
		
		

	}

}
