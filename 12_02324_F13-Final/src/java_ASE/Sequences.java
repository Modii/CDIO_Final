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

public class Sequences {

	private Functionality func = new Functionality();
	private MySQLOperatoerDAO mOpr = new MySQLOperatoerDAO();
	private MySQLProduktBatchDAO mPb = new MySQLProduktBatchDAO();
	private MySQLReceptDAO mRec = new MySQLReceptDAO();
	private MySQLRaavareDAO mRaa = new MySQLRaavareDAO();
	private MySQLRaavareBatchDAO mRaaB = new MySQLRaavareBatchDAO();
	private Data data = new Data();
	private int aktueltPb;

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
			this.sequence4(inFromServer, outToServer);
		}
		else
			this.sequence3(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (4)	Vægten svarer tilbage med navn og godkendes af bruger.
	//-----------------------------------------------------------------
	public void sequence4(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		// Mangler muligvis exception ved forkert ID.
		data.setSplittedInput(data.getServerInput().split(" "));
		try {
			if (func.testId(Integer.parseInt(data.getSplittedInput()[2]))){
				data.setWeightMsg(mOpr.getOperatoer(Integer.parseInt(data.getSplittedInput()[2])).getOprNavn());
				outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());

				data.setWeightMsg("Korrekt ID? Ja 1 / Nej 0");
				outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());
				data.setServerInput(inFromServer.readLine());
				data.setServerInput(inFromServer.readLine()); //RM20 A + Brugerinput

				data.setSplittedInput(data.getServerInput().split(" "));
				if (Integer.parseInt(data.getSplittedInput()[2]) == 1) {
					this.sequence5(inFromServer, outToServer);
				}
				else if (Integer.parseInt(data.getSplittedInput()[2]) == 0)
					this.sequence3(inFromServer, outToServer);
				else{
					this.sequence4(inFromServer, outToServer);
				}
			}
			else {
				this.sequence3(inFromServer, outToServer);
			}

		} catch (NumberFormatException | DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (5)	Operatør indtaster produktbatchnummer
	//-----------------------------------------------------------------
	public void sequence5(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setWeightMsg("Indtast produktbatchnummer");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		data.setServerInput(inFromServer.readLine());

		this.sequence6(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (6)	Vægten svarer tilbage med navn på recept
	//-----------------------------------------------------------------
	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setSplittedInput(data.getServerInput().split(" "));
		aktueltPb = Integer.parseInt(data.getSplittedInput()[2]);
		try {
			mPb.getProduktBatch(aktueltPb).setStatus(1);
			if(func.testPbId(aktueltPb))
			{
				data.setWeightMsg(mRec.getRecept(mPb.getProduktBatch(aktueltPb).getReceptId()).getReceptNavn());
				outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());
				System.out.println("Vægten svarer tilbage første readline: " + data.getServerInput());
				if(data.getServerInput().equals("RM20 B")){
					this.sequence7(inFromServer, outToServer);
				}
				else
				{
					System.out.println("Herp");
					this.sequence6(inFromServer, outToServer);
				}
			}
		} catch (NumberFormatException | DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (7)	Operatøren kontrollerer at vægten er ubelastet og trykker ’ok’
	//-----------------------------------------------------------------
	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setWeightMsg("Aflast. Tryk OK for tarer");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		System.out.println("Tryk ok for tarer første readline: " + data.getServerInput());
		data.setServerInput(inFromServer.readLine());
		System.out.println("Tryk ok for tarer anden readline: " + data.getServerInput());
		System.out.println(data.getServerInput());
		
		if(data.getServerInput().equals("RM20 B")){
			this.sequence8(inFromServer, outToServer);
		}
		else
		{
			System.out.println("derp");
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
		data.setWeightMsg("Anbring tarabeholder");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM20 B"))
		{
			data.setServerInput(inFromServer.readLine());
			this.sequence11(inFromServer, outToServer);
		}
		else
			this.sequence9_10(inFromServer, outToServer);
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
		data.setWeightMsg("Indtast raavarebatch nr.");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM20 B"))
		{
			data.setServerInput(inFromServer.readLine());
			data.setSplittedInput(data.getServerInput().split(" "));
			try {
				if(func.testRaavareBatchId(Integer.parseInt(data.getSplittedInput()[2])))
				{
					data.setWeightMsg(mRaa.getRaavare(mRaaB.getRaavareBatch(Integer.parseInt(data.getSplittedInput()[2])).getRaavareId()).getRaavareNavn());
					outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
					outToServer.flush();
					data.setServerInput(inFromServer.readLine());
					this.sequence14(inFromServer, outToServer);
				}
				else
					this.sequence13(inFromServer, outToServer);
			} catch (NumberFormatException | DALException e) {
				e.printStackTrace();
			}
		}

		else
			this.sequence13(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (14) Bruger afvejer og trykker ok.
	//-----------------------------------------------------------------
	public void sequence14(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		data.setWeightMsg("Afvej mængde. Dernæst OK.");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		System.out.println("1: " + data.getServerInput());
		data.setServerInput(inFromServer.readLine());
		System.out.println("2: " + data.getServerInput());
		this.sequence15(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (15) Vægt spørger om der er flere afvejninger. PROBLEM
	//-----------------------------------------------------------------
	public void sequence15(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setWeightMsg("Flere raavare?Ja=1,Nej=0");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		System.out.println("4: " + data.getServerInput());
		if(data.getServerInput().equals("RM20 B"))
		{
			data.setServerInput(inFromServer.readLine());
			System.out.println("5: " + data.getServerInput());
			data.setSplittedInput(data.getServerInput().split(" "));
			if (Integer.parseInt(data.getSplittedInput()[2]) == 1) {
				this.sequence16_17(inFromServer, outToServer);
			}
			else if (Integer.parseInt(data.getSplittedInput()[2]) == 0)
				this.sequence7(inFromServer, outToServer);
			else{
				this.sequence15(inFromServer, outToServer);
			}
		}
		else this.sequence15(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (16_17) Systemet sætter produktbatch nummerets status til ”Afsluttet” (status = 2).
	//-----------------------------------------------------------------	
	public void sequence16_17(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		try {
			mPb.getProduktBatch(aktueltPb).setStatus(2);
		} catch (NumberFormatException | DALException e) {
			e.printStackTrace();
		}
		data.setWeightMsg("Ny vejning?Ja=1/Nej=0");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM20 B"))
			data.setServerInput(inFromServer.readLine());

		data.setSplittedInput(data.getServerInput().split(" "));
		if (Integer.parseInt(data.getSplittedInput()[2]) == 1) {
			this.sequence3(inFromServer, outToServer);
		}
		else if (Integer.parseInt(data.getSplittedInput()[2]) == 0){
			// Mangler kommando til at afslutte
		}
		else{
			this.sequence15(inFromServer, outToServer);
		}
	}
}