package java_ASE;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import db_mysqldao.MySQLOperatoerDAO;
import db_mysqldao.MySQLProduktBatchDAO;
import db_mysqldao.MySQLReceptDAO;

public class Sequences {

	Functionality f = new Functionality();
	MySQLOperatoerDAO mOpr = new MySQLOperatoerDAO();
	MySQLReceptDAO mRec = new MySQLReceptDAO();
	MySQLProduktBatchDAO mPb = new MySQLProduktBatchDAO();
	Data d = new Data();

	//-----------------------------------------------------------------
	// (3)	Operatør indtaster operatørnummer
	//-----------------------------------------------------------------
	public void sequence3(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		d.setWeightMsg("Indtast operatoernummer");
		outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		d.setServerInput(inFromServer.readLine());
		d.setServerInput(inFromServer.readLine());
		this.sequence4(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (4)	Vægten svarer tilbage med navn og godkendes af bruger.
	//-----------------------------------------------------------------
	public void sequence4(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		// Mangler muligvis exception ved forkert ID.
		d.setSplittedInput(d.getServerInput().split(" "));
		try {
			if (f.testId(Integer.parseInt(d.getSplittedInput()[2]))){
				d.setWeightMsg(mOpr.getOperatoer(Integer.parseInt(d.getSplittedInput()[2])).getOprNavn());
				outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				d.setServerInput(inFromServer.readLine());

				d.setWeightMsg("Korrekt ID? Ja 1 / Nej 0");
				outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				d.setServerInput(inFromServer.readLine());
				d.setServerInput(inFromServer.readLine());
				d.setServerInput(inFromServer.readLine()); //RM20 A + Brugerinput

				d.setSplittedInput(d.getServerInput().split(" "));
				if (Integer.parseInt(d.getSplittedInput()[2]) == 1) {
					this.sequence5(inFromServer, outToServer);
				}
				else if (Integer.parseInt(d.getSplittedInput()[2]) == 0)
					this.sequence3(inFromServer, outToServer);
				else{

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
		d.setWeightMsg("Indtast produktbatchnummer");
		outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		d.setServerInput(inFromServer.readLine());
		d.setServerInput(inFromServer.readLine());
		this.sequence6(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (6)	Vægten svarer tilbage med navn på recept
	//-----------------------------------------------------------------
	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		d.setSplittedInput(d.getServerInput().split(" "));
		try {
			if(f.testPbId(Integer.parseInt(d.getSplittedInput()[2])))
			{
				d.setWeightMsg(mRec.getRecept(mPb.getProduktBatch(Integer.parseInt(d.getSplittedInput()[2])).getReceptId()).getReceptNavn());
				outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				d.setServerInput(inFromServer.readLine());
				this.sequence7(inFromServer, outToServer);
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
		d.setWeightMsg("Aflast. Tryk OK for tarer");
		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		d.setServerInput(inFromServer.readLine());

		if(d.getServerInput().equals("RM20 B"))
			d.setServerInput(inFromServer.readLine());
		else
		{
			this.sequence7(inFromServer, outToServer);
		}

		this.sequence8(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (8) Vægt tareres
	//-----------------------------------------------------------------	

	public void sequence8(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		outToServer.writeBytes("T\r\n");
		this.sequence9_10(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (9_10) Vægt beder om første tara-beholder.
	//-----------------------------------------------------------------	

	public void sequence9_10(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		d.setWeightMsg("Anbring tarabeholder");
		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		d.setServerInput(inFromServer.readLine());

		if(d.getServerInput().equals("RM20 B"))
		{
			d.setServerInput(inFromServer.readLine());
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

		d.setServerInput(inFromServer.readLine());
		if(d.getServerInput().startsWith("T S")){
			this.sequence13(inFromServer, outToServer);
		}
		else this.sequence12(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (13) Vægten beder om raavarebatch nummer på første råvare.
	//-----------------------------------------------------------------
	public void sequence13(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		d.setWeightMsg("Indtast raavarebatch nr.");
		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		d.setServerInput(inFromServer.readLine());

		if(d.getServerInput().equals("RM20 B"))
			d.setServerInput(inFromServer.readLine());
		
		d.setSplittedInput(d.getServerInput().split(" "));
		if(f.testPbId(Integer.parseInt(d.getSplittedInput()[2])))
		{
			d.setWeightMsg(mRec.getRecept(mPb.getProduktBatch(Integer.parseInt(d.getSplittedInput()[2])).getReceptId()).getReceptNavn());
			outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
			outToServer.flush();
		}


		else
			this.sequence13(inFromServer, outToServer);
	}

	//	//-----------------------------------------------------------------
	//	// (10) Netto registreres.
	//	//-----------------------------------------------------------------
	//	public void sequence10(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
	//
	//		outToServer.writeBytes("S\r\n");
	//
	//		d.setServerInput(inFromServer.readLine());
	//		if(d.getServerInput().startsWith("S S")){
	//			d.setSplittedInput(d.getServerInput().split(" +"));
	//			d.setNetto(Double.parseDouble(d.getSplittedInput()[2])-d.getTara());
	//			this.sequence11(inFromServer, outToServer);
	//		}
	//		else this.sequence9(inFromServer, outToServer);
	//	}
	//
	//	//-----------------------------------------------------------------
	//	// (11) Operatøren instrueres til at fjerne netto og tara.
	//	//-----------------------------------------------------------------
	//	public void sequence11(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
	//		d.setWeightMsg("Fjern enheder");
	//		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
	//
	//		d.setServerInput(inFromServer.readLine());
	//
	//
	//		if(d.getServerInput().equals("RM20 B"))
	//			d.setServerInput(inFromServer.readLine());
	//
	//		else
	//			this.sequence11(inFromServer, outToServer);
	//
	//
	//		if(!aborted())
	//			this.sequence12(inFromServer, outToServer);
	//		else
	//			this.sequence1(inFromServer, outToServer);
	//	}
	//	//-----------------------------------------------------------------
	//	// (12) Vægt tareres, så den er klar til en ny omgang
	//	//-----------------------------------------------------------------	
	//	public void sequence12(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
	//
	//		outToServer.writeBytes("T\r\n");
	//		this.sequence13(inFromServer, outToServer);
	//	}
	//
	//	//-----------------------------------------------------------------
	//	// (13) Minus brutto registreres.
	//	//-----------------------------------------------------------------
	//	public void sequence13(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
	//
	//		d.setServerInput(inFromServer.readLine());
	//		if(d.getServerInput().startsWith("T S")){
	//			d.setSplittedInput(d.getServerInput().split(" +"));
	//			d.setBruttoCheck(Double.parseDouble(d.getSplittedInput()[2]));
	//			this.sequence13(inFromServer, outToServer);
	//		}
	//		else this.sequence14(inFromServer, outToServer);
	//	}
	//
	//	//-----------------------------------------------------------------
	//	// (14) Bruttokontrol OK, hvis det er tilfældet.
	//	//-----------------------------------------------------------------
	//	public void sequence14(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
	//		if(d.getBruttoCheck() >= 2 || d.getBruttoCheck() <= -2){
	//
	//			d.setWeightMsg("Afvejning afvist");
	//			outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
	//
	//			d.setServerInput(inFromServer.readLine());
	//
	//			if(d.getServerInput().equals("RM20 B"))
	//				d.setServerInput(inFromServer.readLine());
	//			else
	//				this.sequence14(inFromServer, outToServer);
	//
	//			if(d.getServerInput().startsWith("RM20 A"))
	//				this.sequence7(inFromServer, outToServer);
	//		}
	//		else{
	//
	//			d.setWeightMsg("Afvejning godkendt!");
	//
	//			outToServer.writeBytes("RM20 4 \""+ d.getWeightMsg() +"\"\r\n");
	//			inFromServer.readLine();
	//			inFromServer.readLine();
	//			this.sequence15(inFromServer, outToServer);
	//		}
	//	}
	//
	//	//-----------------------------------------------------------------
	//	// (15) Mængde på lager afskrives og historikken opdateres.
	//	//-----------------------------------------------------------------
	//	public void sequence15(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
	//
	//		try{
	//			log();
	//		}
	//		catch(FileNotFoundException e){
	//			d.setWeightMsg("Logfejl. Afvist!");
	//			outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
	//
	//			d.setServerInput(inFromServer.readLine());
	//
	//			if(d.getServerInput().equals("RM20 B"))
	//				d.setServerInput(inFromServer.readLine());
	//			else
	//				this.sequence15(inFromServer, outToServer);
	//
	//			if(d.getServerInput().startsWith("RM20 A"))
	//				this.sequence1(inFromServer, outToServer);
	//		}
	//
	//
	//		this.sequence1(inFromServer, outToServer);
	//	}
	//
	//	public void log() throws IOException{
	//
	//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Log.txt"), true));
	//		Calendar c = Calendar.getInstance();
	//		DecimalFormat df = new DecimalFormat("00");
	//		String timeStamp = "" + c.get(Calendar.YEAR) + "-" + df.format(c.get(Calendar.MONTH) + 1) + "-" + df.format(c.get(Calendar.DATE)) + "-" + df.format(c.get(Calendar.HOUR_OF_DAY)) + ":" + df.format(c.get(Calendar.MINUTE)) + ":" + df.format(c.get(Calendar.SECOND));
	//
	//		bw.write(timeStamp + ", " + d.getOprID() + ", " + d.getItemNoInput() + ", " + d.getItemName() + ", " + d.getNetto() + " kg.");
	//		bw.newLine();
	//		bw.flush();
	//		bw.close();
	//	}
	//
	//
	//
}
