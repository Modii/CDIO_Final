package ase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import dao_interfaces.DALException;
import db_mysqldao.MySQLReceptKompDAO;
import dto.ReceptKompDTO;


public class SequenceHelper {
	private MySQLReceptKompDAO mRecKomp = new MySQLReceptKompDAO();

	String dato, weightMsg, serverInput, itemName, userInput;
	int oprID, pbID, receptID, rbID, raavareID, itemNoInput, iteNoStore;	
	double tara, netto, brutto, bruttoCheck, tolerance, totPosTol, totNegTol;

	String[] splittedInput = new String[10];
	List<ReceptKompDTO> listen;

	String generateDato(){
		Calendar d = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("00");
		dato = d.get(Calendar.YEAR) + "-"
				+ df.format(d.get(Calendar.MONTH) + 1) + "-"
				+ df.format(d.get(Calendar.DATE)) + " "
				+ df.format(d.get(Calendar.HOUR_OF_DAY)) + ":"
				+ df.format(d.get(Calendar.MINUTE)) + ":"
				+ df.format(d.get(Calendar.SECOND));
		return dato;
	}

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
		// System.out.println("setWeightMsg: " + weightMsg);
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
		// System.out.println("setServerInput: " + serverInput);
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

	public int splitInt(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		this.setSplittedInput(this.getServerInput().split(" "));
		int returnSplitInt = Integer.parseInt(this.getSplittedInput()[2].replaceAll("\"",""));
		return returnSplitInt;
	}

	public double splitDouble(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		this.setSplittedInput(this.getServerInput().split(" "));
		double returnSplitDouble = (Double.parseDouble(this.getSplittedInput()[7]));
		return returnSplitDouble;
	}

	// Metoden tjekker hvilken type RM-kommando, der er tale om. Derefter kommer den krævede efterfulgte integerværdi og dernæst vægtbeskeden.
	public void RMPrintOgRead(int RMType, int x1, String weightMsg, BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		this.setWeightMsg(weightMsg);
		if(RMType == 49)
			outToServer.writeBytes("RM49 " + x1 + " \"" + this.getWeightMsg() + "\"\r\n");	
		else if (RMType == 20)
			outToServer.writeBytes("RM20 " + x1 + " \"" + this.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		this.setServerInput(inFromServer.readLine());
	}
}
