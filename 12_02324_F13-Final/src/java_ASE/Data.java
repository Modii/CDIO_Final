package java_ASE;

public class Data {

	String weightMsg, serverInput, itemName, userInput;
	int oprID;
	int itemNoInput, itemNoStore;
	String[] splittedInput = new String[10];
	double tara, netto, bruttoCheck;

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
	int getItemNoStore() {
		return itemNoStore;
	}
	void setItemNoStore(int itemNoStore) {
		this.itemNoStore = itemNoStore;
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
}
