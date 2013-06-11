package db_mysqldao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db_connection.Connector;
import dao_interfaces.DALException;
import dao_interfaces.IRaavareDAO;
import dto.RaavareDTO;

public class MySQLRaavareDAO implements IRaavareDAO {

	@Override
	public RaavareDTO getRaavare(int raavareId) throws DALException {
		ResultSet rs = Connector.doQuery("SELECT * FROM Raavare WHERE raavareId = " + raavareId);
		try {
			if (!rs.first()) throw new DALException("R�varen " + raavareId + " findes ikke");
			return new RaavareDTO (rs.getInt(1), rs.getString(2), rs.getString(3));
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<RaavareDTO> getRaavareList() throws DALException {
		List<RaavareDTO> list = new ArrayList<RaavareDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM Raavare");
		try
		{
			while (rs.next()) 
			{
				list.add(new RaavareDTO (rs.getInt(1), rs.getString(2), rs.getString(3)));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createRaavare(RaavareDTO raavare) throws DALException {
		Connector.doUpdate(
				"INSERT INTO Raavare(raavareId, raavareNavn, leverandoer) VALUES " +
						"(" + raavare.getRaavareId() + ", '" + raavare.getRaavareNavn() 
						+ "', '" + raavare.getLeverandoer() + "')"
				);
	}

	@Override
	public void updateRaavare(RaavareDTO raavare) throws DALException {
		Connector.doUpdate(
				"UPDATE Raavare SET  raavareNavn = '" + raavare.getRaavareNavn() + "', leverandoer =  '" 
						+ raavare.getLeverandoer() + "' WHERE raavareId = " + raavare.getRaavareId()
				);
	}

}
