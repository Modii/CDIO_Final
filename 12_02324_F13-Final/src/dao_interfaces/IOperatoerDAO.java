package dao_interfaces;

import java.util.List;

import dto.OperatoerDTO;

public interface IOperatoerDAO {
	OperatoerDTO getOperatoer(int oprId) throws DALException;
	List<OperatoerDTO> getOperatoerList() throws DALException;
	void createOperatoer(OperatoerDTO opr) throws DALException;
	void updateOperatoer(OperatoerDTO opr) throws DALException;
	void removeOperatoer(OperatoerDTO operatoer) throws DALException;
}
