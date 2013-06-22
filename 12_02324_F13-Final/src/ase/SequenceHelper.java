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
	private String dato, weightMsg, serverInput, itemName, userInput;
	private int oprID, pbID, receptID, rbID, raavareID, itemNoInput, itemNoStore;	
	private double tara, netto, brutto, bruttoCheck, tolerance, totPosTol, totNegTol;
	private String[] splittedInput = new String[10];
	private List<ReceptKompDTO> listen;

	public String generateDato(){
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
	
	public double trimDecimal(double d) {
		DecimalFormat df = new DecimalFormat("#.###");
		String trimmed = (df.format(d));
		String trimmed1 = trimmed.replace(',' , '.');
		double trimmedTemp = Double.parseDouble(trimmed1);
		return trimmedTemp;
	}
	
	public double getBrutto() {
		return brutto;
	}
	public void setBrutto(double brutto) {
		this.brutto = brutto;
	}
	public List<ReceptKompDTO> getListen() {
		return listen;
	}
	public void setListen(List<ReceptKompDTO> listen) {
		this.listen = listen;
	}
	public int getRbID() {
		return rbID;
	}
	public void setRbID(int rbID) {
		this.rbID = rbID;
	}
	public int getReceptID() {
		return receptID;
	}
	public void setReceptID(int receptID) {
		this.receptID = receptID;
	}
	public String getWeightMsg() {
		return weightMsg;
	}
	public void setWeightMsg(String weightMsg) {
		// System.out.println("setWeightMsg: " + weightMsg);
		this.weightMsg = weightMsg;
	}
	public int getOprID() {
		return oprID;
	}
	public void setOprID(int oprID) {
		this.oprID = oprID;
	}
	public String getServerInput() {
		return serverInput;
	}
	public void setServerInput(String serverInput) {
		//System.out.println("setServerInput: " + serverInput);
		this.serverInput = serverInput;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUserInput() {
		return userInput;
	}
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	public int getItemNoInput() {
		return itemNoInput;
	}
	public void setItemNoInput(int itemNoInput) {
		this.itemNoInput = itemNoInput;
	}
	public int getItemNoStore() {
		return itemNoStore;
	}
	public void setItemNoStore(int iteNoStore) {
		this.itemNoStore = iteNoStore;
	}
	public String[] getSplittedInput() {
		return splittedInput;
	}
	public void setSplittedInput(String[] splittedInput) {
		this.splittedInput = splittedInput;
	}
	public double getTara() {
		return tara;
	}
	public void setTara(double tara) {
		this.tara = tara;
	}
	public double getNetto() {
		return netto;
	}
	public void setNetto(double netto) {
		this.netto = netto;
	}
	public double getBruttoCheck() {
		return bruttoCheck;
	}
	public void setBruttoCheck(double bruttoCheck) {
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
			tolerance = this.trimDecimal(((mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getNomNetto()) * (mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getTolerance()) / 100));
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
	public double getTotPosTol() {
		return totPosTol;
	}
	public void setTotPosTol() {
		try {
			totPosTol = this.trimDecimal(((mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getNomNetto())) + this.getTolerance());
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
	public double getTotNegTol() {
		return totNegTol;
	}
	public void setTotNegTol() {
		try {
			totNegTol = this.trimDecimal(((mRecKomp.getReceptKomp(this.getReceptID(), this.getRaavareID()).getNomNetto())) - this.getTolerance());
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
