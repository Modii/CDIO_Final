package java_ASE;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import db_mysqldao.MySQLOperatoerDAO;
import db_mysqldao.MySQLProduktBatchDAO;
import db_mysqldao.MySQLRaavareBatchDAO;
import db_mysqldao.MySQLRaavareDAO;
import db_mysqldao.MySQLReceptDAO;
import dto.ProduktBatchDTO;

public class Sequences {

	private Functionality func = new Functionality();
	private MySQLOperatoerDAO mOpr = new MySQLOperatoerDAO();
	private MySQLProduktBatchDAO mPb = new MySQLProduktBatchDAO();
	private MySQLReceptDAO mRec = new MySQLReceptDAO();
	private MySQLRaavareDAO mRaa = new MySQLRaavareDAO();
	private MySQLRaavareBatchDAO mRaaB = new MySQLRaavareBatchDAO();
	private Data data = new Data();

	//-----------------------------------------------------------------
	// (2)	Opstart
	//-----------------------------------------------------------------
	public void sequence2(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException

	{
		data.setServerInput(inFromServer.readLine());
		this.sequence3(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (3)	Operatør indtaster operatørnummer
	//-----------------------------------------------------------------
	public void sequence3(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setWeightMsg("Indtast operatoernummer");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM20 B"))
		{
			data.setServerInput(inFromServer.readLine());
			data.setSplittedInput(data.getServerInput().split(" "));
			String temp;
			temp = data.getSplittedInput()[2].replaceAll("\"","");
			data.setOprID(Integer.parseInt(temp));
			this.sequence4(inFromServer, outToServer);
		}
		else
		{
			System.out.println("Derp");
			this.sequence3(inFromServer, outToServer);
		}
	}

	//-----------------------------------------------------------------
	// (4)	Vægten svarer tilbage med navn og godkendes af bruger.
	//-----------------------------------------------------------------
	public void sequence4(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			if (func.testId(data.getOprID())){
				data.setWeightMsg("Operatoer: " + mOpr.getOperatoer(data.getOprID()).getOprNavn() + ". Tryk OK for at bruge det valgte ID eller CANCEL for at vaelge et andet.");
				outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());

				if(data.getServerInput().equals("RM49 B"))
				{
					data.setServerInput(inFromServer.readLine());
					if(data.getServerInput().equals("RM49 A 1")){
						this.sequence5(inFromServer, outToServer);
					}
					else if(data.getServerInput().equals("RM49 A 2")){
						this.sequence4(inFromServer, outToServer);
					}
				}
				else this.sequence4(inFromServer, outToServer);
			}
			else this.sequence3(inFromServer, outToServer);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (5)	Operatør indtaster produktbatchnummer
	//-----------------------------------------------------------------
	public void sequence5(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setWeightMsg("Indtast produktbatchnr.");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM20 B"))
		{
			data.setServerInput(inFromServer.readLine());
			data.setSplittedInput(data.getServerInput().split(" "));
			String temp = data.getSplittedInput()[2].replaceAll("\"","");
			data.setServerInput((temp));
			data.setPbID(Integer.parseInt(temp));
			this.sequence6(inFromServer, outToServer);
		}
		else
			this.sequence5(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (6)	Vægten svarer tilbage med navn på recept
	//-----------------------------------------------------------------
	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			if(func.testPbId(data.getPbID()))
			{
				ProduktBatchDTO produktBatch = mPb.getProduktBatch(data.getPbID());
				produktBatch.setStatus(2);
				data.setWeightMsg("Recept: " + mRec.getRecept(mPb.getProduktBatch(data.getPbID()).getReceptId()).getReceptNavn() + ". Tryk OK for at bruge den valgte recept eller CANCEL for at vaelge en anden.");
				outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());

				if(data.getServerInput().equals("RM49 B"))
				{
					data.setServerInput(inFromServer.readLine());
					if(data.getServerInput().equals("RM49 A 1")){
						this.sequence7(inFromServer, outToServer);
					}
					else if(data.getServerInput().equals("RM49 A 2")){
						this.sequence5(inFromServer, outToServer);
					}
					else this.sequence6(inFromServer, outToServer);
				}
				this.sequence6(inFromServer, outToServer);
			}
			else {
				this.sequence5(inFromServer, outToServer);
			}

		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (7)	Operatøren kontrollerer at vægten er ubelastet og trykker ’ok’
	//-----------------------------------------------------------------
	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setWeightMsg("Aflast vaegten og tryk OK for at tarere.");
		outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM49 B"))
		{
			data.setServerInput(inFromServer.readLine());
			if(data.getServerInput().equals("RM49 A 1")){
				this.sequence8(inFromServer, outToServer);
			}
			else {
				this.sequence7(inFromServer, outToServer);
			}
		}
		else {
			this.sequence7(inFromServer, outToServer);
		}
	}

	//-----------------------------------------------------------------
	// (8) Vægt tareres
	//-----------------------------------------------------------------	

	public void sequence8(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		outToServer.writeBytes("T\r\n");
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().startsWith("T S")){
			this.sequence9_10(inFromServer, outToServer);
		}
		else this.sequence8(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (9_10) Vægt beder om første tara-beholder.
	//-----------------------------------------------------------------	

	public void sequence9_10(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setWeightMsg("Anbring taraholder. Tryk dernæst OK.");
		outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM49 B"))
		{
			data.setServerInput(inFromServer.readLine());
			if(data.getServerInput().equals("RM49 A 1")){
				this.sequence11(inFromServer, outToServer);
			}
			else {
				this.sequence9_10(inFromServer, outToServer);
			}
		}
		else {
			this.sequence9_10(inFromServer, outToServer);
		}
	}
	//-----------------------------------------------------------------
	// (11)	Vægtdisplay beder om evt. tara og at brugeren bekræfter. 	
	//-----------------------------------------------------------------
	public void sequence11(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		// Vægten af tarabeholder registreres
		// Mangler registrering af tara.
		this.sequence12(inFromServer, outToServer);

	}
	//-----------------------------------------------------------------
	// (12) Vægt tareres
	//-----------------------------------------------------------------
	public void sequence12(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		outToServer.writeBytes("T\r\n");
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().startsWith("T S")){
			this.sequence13(inFromServer, outToServer);
		}
		else this.sequence12(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (13) Vægten beder om raavarebatch nummer på råvare.
	//-----------------------------------------------------------------
	public void sequence13(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setWeightMsg("Indtast raavarebatchnr.");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM20 B"))
		{
			this.sequence14(inFromServer, outToServer);
		}
		else
			this.sequence13(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (14) Bruger afvejer og trykker ok.
	//-----------------------------------------------------------------
	public void sequence14(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		data.setServerInput(inFromServer.readLine());
		data.setSplittedInput(data.getServerInput().split(" "));
		String temp = data.getSplittedInput()[2].replaceAll("\"","");
		data.setServerInput((temp));
		data.setPbID(Integer.parseInt(temp));
		try {
			data.setWeightMsg("Du har valgt: " + mRaa.getRaavare(mRaaB.getRaavareBatch(Integer.parseInt(temp)).getRaavareId()).getRaavareNavn() + ". Tryk OK for at paabegynde afvejning. Naar den oenskede maengde er afvejet, tryk da paa AFVEJ");
		} catch (NumberFormatException | DALException e) {
			e.printStackTrace();
		}
		outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		data.setServerInput(inFromServer.readLine());

		outToServer.writeBytes("DW" + "\r\n");
		data.setServerInput(inFromServer.readLine());

		System.out.println("DW: " + data.getServerInput());
		String temp1 = "afvej";
		System.out.println("Vi skriver: " + "RM30 \"" + temp1);
		outToServer.writeBytes("RM30 \"" + temp1 + "\"\r\n");

		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM30 B")){
			outToServer.writeBytes("RM39 1" + "\r\n");
			data.setServerInput(inFromServer.readLine());
			data.setServerInput(inFromServer.readLine());
			if(data.getServerInput().equals("RM30 A 1")){
				outToServer.writeBytes("S\r\n");
				data.setServerInput(inFromServer.readLine());
				this.sequence15_16(inFromServer, outToServer);
			}
			else this.sequence13(inFromServer, outToServer);
		}
		else this.sequence13(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (15_16) Vægt spørger om der er flere afvejninger.
	//-----------------------------------------------------------------
	public void sequence15_16(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setWeightMsg("Afvejning af varen er gennefoert. Tryk OK for at afveje flere raavarer eller CANCEL for at markere produktbatchet som afsluttet.");
		outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM49 B"))
		{
			data.setServerInput(inFromServer.readLine());
			if(data.getServerInput().equals("RM49 A 1")){
				this.sequence7(inFromServer, outToServer);
			}
			else if(data.getServerInput().equals("RM49 A 2"))
			{
				try {
					ProduktBatchDTO produktBatch = mPb.getProduktBatch(data.getPbID());
					produktBatch.setStatus(2);
					mPb.updateProduktBatch(produktBatch);
				} catch (DALException e) {
					e.printStackTrace();
				}
				this.sequence17(inFromServer, outToServer);
			}
			else sequence15_16(inFromServer, outToServer);
		}
		else this.sequence15_16(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (17) Systemet spørger om der skal startes forfra eller afsluttes
	//-----------------------------------------------------------------	
	public void sequence17(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		data.setWeightMsg("Produktionsforskrift gennemfoert. Tryk OK for at påbegynde en ny afvejningsprocedure eller CANCEL for at afslutte programmet.");
		outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM49 B"))
		{
			data.setServerInput(inFromServer.readLine());
			if(data.getServerInput().equals("RM49 A 1")){
				this.sequence3(inFromServer, outToServer);
			}
			else if(data.getServerInput().equals("RM49 A 2")){
				// Afslut.
			}
			else this.sequence17(inFromServer, outToServer);
		}
		else this.sequence17(inFromServer, outToServer);
	}
}