package businessLogic_layer;

import dao_interfaces.DALException;
import dao_interfaces.IOperatoerDAO;
import dao_interfaces.IProduktBatchDAO;
import dao_interfaces.IProduktBatchKompDAO;
import dao_interfaces.IRaavareBatchDAO;
import dao_interfaces.IRaavareDAO;
import dao_interfaces.IReceptDAO;
import dao_interfaces.IReceptKompDAO;

public interface IFunctionality {
	String generatePassword();
	void askForOprName();
	boolean testId(String i) throws DALException;
	boolean testPbId(int i) throws DALException;
	boolean testRaavareBatchId(int i) throws DALException;
	boolean testPassword(int i, String s) throws DALException;
	boolean checkPasswordStandards(int i, String s) throws DALException;
	boolean checkIfIdentical(String s, String k);
	double calculateWeight(double tare, double brutto);
	boolean testRaavareId(int rbID) throws DALException;
	IRaavareDAO getRaavareDAO();
	IRaavareBatchDAO getRaavareBatchDAO();
	IProduktBatchDAO getProduktBatchDAO();
	IProduktBatchKompDAO getProduktBatchKompDAO();
	IReceptDAO getReceptDAO();
	IReceptKompDAO getReceptKompDAO();
	IOperatoerDAO getOprDAO();
	void setOprDAO(IOperatoerDAO oprDAO);
	boolean testReceptId(int receptid) throws DALException;
	
}
