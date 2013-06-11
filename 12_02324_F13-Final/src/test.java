import java.sql.SQLException;

import db_connection.Connector;
import dao_interfaces.DALException;
import businessLogic_layer.Functionality;
public class test {

	/**
	 * @param args
	 * @throws DALException 
	 */
	
	public static void main(String[] args) throws DALException {
		try { new Connector(); } 
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e) { e.printStackTrace(); }
		catch (SQLException e) { e.printStackTrace(); }
		Functionality f = new Functionality();
		System.out.println(f.testId(2));
	}

}
