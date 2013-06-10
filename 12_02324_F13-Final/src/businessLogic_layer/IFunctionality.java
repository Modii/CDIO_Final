package businessLogic_layer;

import data_layer.IOprDAO;
import data_layer.IOprDAO.DALException;

public interface IFunctionality {
	String generatePassword();
	void askForOprName();
	boolean testId(int i) throws DALException;
	boolean testPassword(int i, String s) throws DALException;
	boolean askForNewPassword(int i, String s) throws DALException;
	boolean checkIfIdentical(String s, String k);
	double calculateWeight(double tare, double brutto);
	void createOperatoer(String oprNavn, String ini, String cpr) throws DALException;
	void updateOperatoer(int updateID, String updateName, String updateIni, String updateCpr) throws DALException;
	void removeOperatoer(int removeID) throws DALException;
	String showOprList() throws DALException;
	IOprDAO getDataLaget();
	void setDataLaget(IOprDAO dataLaget);
}
