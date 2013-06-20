package ase;

import java.util.List;

import dao_interfaces.DALException;
import db_mysqldao.MySQLReceptKompDAO;
import dto.ReceptKompDTO;


public class Data {
	private MySQLReceptKompDAO mRecKomp = new MySQLReceptKompDAO();

	String weightMsg, serverInput, itemName, userInput;
	int oprID, pbID, receptID, rbID, raavareID, itemNoInput, iteNoStore;	
	double tara, netto, brutto, bruttoCheck, tolerance, totPosTol, totNegTol;

	String[] splittedInput = new String[10];
	List<ReceptKompDTO> listen;


	double getBrutto() {
		return brutto;
	}
	void setBrutto(double brutto) {
		this.brutto = brutto;
	}
	List<ReceptKompDTO> getListen() {
		return listen;
	}
	void setListen(List<ReceptKompDTO> listen) {
		this.listen = listen;
	}
	int getRbID() {
		return rbID;
	}
	void setRbID(int rbID) {
		this.rbID = rbID;
	}

	int getReceptID() {
		return receptID;
	}
	void setReceptID(int receptID) {
		this.receptID = receptID;
	}

	String getWeightMsg() {
		return weightMsg;
	}
	void setWeightMsg(String weightMsg) {
		System.out.println("setWeightMsg: " + weightMsg);
		this.weightMsg = weightMsg;
	}
	int getOprID() {
		return oprID;
	}
	void setOprID(int oprID) {
		this.oprID = oprID;
	}
	String getServerInput() {
		return serverInput;
	}
	void setServerInput(String serverInput) {
		System.out.println("setServerInput: " + serverInput);
		this.serverInput = serverInput;
	}
	String getItemName() {
		return itemName;
	}
	void setItemName(String itemName) {
		this.itemName = itemName;
	}
	String getUserInput() {
		return userInput;
	}
	void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	int getItemNoInput() {
		return itemNoInput;
	}
	void setItemNoInput(int itemNoInput) {
		this.itemNoInput = itemNoInput;
	}
	public int getIteNoStore() {
		return iteNoStore;
	}
	public void setIteNoStore(int iteNoStore) {
		this.iteNoStore = iteNoStore;
	}
	String[] getSplittedInput() {
		return splittedInput;
	}
	void setSplittedInput(String[] splittedInput) {
		this.splittedInput = splittedInput;
	}
	double getTara() {
		return tara;
	}
	void setTara(double tara) {
		this.tara = tara;
	}
	double getNetto() {
		return netto;
	}
	void setNetto(double netto) {
		this.netto = netto;
	}
	double getBruttoCheck() {
		return bruttoCheck;
	}
	void setBruttoCheck(double bruttoCheck) {
		this.bruttoCheck = bruttoCheck;
	}
	public int getPbID() {
		return pbID;
	}
	public void setPbID(int pbID) {
		this.pbID = pbID;
	}
	public int getRaavareID() {
		return raavareID;
	}
	public void setRaavareID(int raavareID) {
		this.raavareID = raavareID;
	}
	public double getTolerance() {
		return tolerance;
	}
	public void setTolerance() {
		try {
			tolerance = ((mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getNomNetto()) * (mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getTolerance()) / 100);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
	public double getTotPosTol() {
		return totPosTol;
	}
	public void setTotPosTol() {
		try {
			totPosTol = (mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getNomNetto()) + this.getTolerance();
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
	public double getTotNegTol() {
		return totNegTol;
	}
	public void setTotNegTol() {
		try {
			totNegTol = (mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getNomNetto()) - this.getTolerance();
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
}
