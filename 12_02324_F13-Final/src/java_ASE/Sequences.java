package java_ASE;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import db_mysqldao.MySQLOperatoerDAO;
import db_mysqldao.MySQLProduktBatchDAO;

public class Sequences {

	Functionality f = new Functionality();
	MySQLOperatoerDAO mOpr = new MySQLOperatoerDAO();
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
		System.out.println(d.getSplittedInput()[2]);
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
				d.setServerInput(inFromServer.readLine()); //RM20 A + Brugerinput

				d.setSplittedInput(d.getServerInput().split(" "));
				if (d.getSplittedInput()[2] == "1") {
					this.sequence5(inFromServer, outToServer);
				}
				else 
					this.sequence3(inFromServer, outToServer);
			}
			else {this.sequence3(inFromServer, outToServer);
			}

		} catch (NumberFormatException | DALException e) {
			e.printStackTrace();
		}
	}
	boolean aborted()
	{
		return !d.getServerInput().startsWith("RM20 A");
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
		this.sequence4(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (6)	Vægten svarer tilbage med navn på recept
	//-----------------------------------------------------------------
	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		d.setSplittedInput(d.getServerInput().split(" "));
		System.out.println(d.getSplittedInput()[2]);
		try {
	if(f.testPbId(Integer.parseInt(d.getSplittedInput()[2])))
	{
		
	}
			d.setWeightMsg(mPb.getPbId(Integer.parseInt(d.getSplittedInput()[2]))).());
				outToServer.writeBytes("RM20 8 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
				outToServer.flush();
				d.setServerInput(inFromServer.readLine());
			{
				
				
			}
		} catch (NumberFormatException | DALException e) {
			e.printStackTrace();
		}
	}
}












//
//	//-----------------------------------------------------------------
//	// (5)	Program sammenligner userinput med varenumre i store.txt
//	// (6)	Når identisk varenummer er fundet spørger program bruger
//	//		om det er korrekt varenavn. Hvis ja sendes videre til n�ste
//	//		sekvens. Hvis nej køres sequence3()
//	//-----------------------------------------------------------------
//	public void sequence5_6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
//	{
//
//		BufferedReader inFromLocal = new BufferedReader(new FileReader(new File("store.txt")));
//
//		boolean notFound = true;
//
//		while(notFound){
//			try{
//				d.setSplittedInput(inFromLocal.readLine().split(","));
//			}
//			catch(NullPointerException e){
//				notFound = true;
//			}
//			d.setItemNoStore(Integer.parseInt(d.getSplittedInput()[0]));
//			System.out.println("store: " + d.getItemNoStore());
//			System.out.println("input: " + d.getItemNoInput() + "\n");
//
//			if(d.getItemNoStore() == d.getItemNoInput()){ 
//				inFromLocal.close();
//				//Så snart at det indtastede nummer er lig et nummer i "databasen", 
//				// sættes notFound = false og nedenstående kode eksekveres. 
//				notFound = false;
//				d.setItemName(d.getSplittedInput()[1]);
//
//				d.setWeightMsg("Vare: " + d.getItemName()); 
//
//				outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
//
//				d.setServerInput(inFromServer.readLine());
//				//				d.setServerInput(inFromServer.readLine());
//				//				d.setServerInput(inFromServer.readLine());
//
//				d.setSplittedInput(d.getServerInput().split(" "));
//				if(d.getServerInput().equals("RM20 B"))
//				{
//					d.setServerInput(inFromServer.readLine());
//					if(!aborted()){
//						d.setSplittedInput(d.getServerInput().split(" "));
//						d.setUserInput(d.getSplittedInput()[2]);		
//
//						//Hvis varen er korrekt fortsættes der til sekvens 7 ellers starter man forfra i sekvens 3.						
//						if(d.getUserInput().equals("\"1\""))
//						{
//							this.sequence7(inFromServer, outToServer);
//						}
//						//Fejl: Kan annullere, men kan derefter ikke v�lge samme vare igen.
//						else if(d.getUserInput().equals("\"0\""))
//						{
//							this.sequence3(inFromServer, outToServer);
//						}
//					}
//					else
//						this.sequence1(inFromServer, outToServer);
//				}
//			}
//		}
//		d.setWeightMsg("Varenummer ikke fundet");
//		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
//		inFromServer.readLine();
//		this.sequence3(inFromServer, outToServer);
//
//	}
//
//	//-----------------------------------------------------------------
//	// (7)	Vægtdisplay beder om evt. tara og at brugeren bekræfter. 	
//	//-----------------------------------------------------------------
//	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
//	{
//		d.setWeightMsg("Anbring skål");
//		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
//
//		d.setServerInput(inFromServer.readLine());
//
//		if(d.getServerInput().equals("RM20 B"))
//			d.setServerInput(inFromServer.readLine());
//		else
//			this.sequence7(inFromServer, outToServer);
//
//		if(!aborted())
//			this.sequence8(inFromServer, outToServer);
//		else
//			this.sequence1(inFromServer, outToServer);
//
//	}
//
//	//-----------------------------------------------------------------
//	// (8) Vægt tareres og tara gemmes i lokal variabel
//	//-----------------------------------------------------------------
//	public void sequence8(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
//	{
//		outToServer.writeBytes("T\r\n");
//
//		d.setServerInput(inFromServer.readLine());
//		if(d.getServerInput().startsWith("T S")){
//			d.setSplittedInput(d.getServerInput().split(" +"));
//			d.setTara(Double.parseDouble(d.getSplittedInput()[2]));
//			this.sequence9(inFromServer, outToServer);
//		}
//		else this.sequence7(inFromServer, outToServer);
//	}
//
//	//-----------------------------------------------------------------
//	// (9) Operatøren instrueres til at påfylde vare og derefter trykke enter.
//	//-----------------------------------------------------------------
//	public void sequence9(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
//		d.setWeightMsg("Påfyld vare");
//		outToServer.writeBytes("RM20 4 \"" + d.getWeightMsg() + "\" \" \" \"&3\"\r\n");
//
//		d.setServerInput(inFromServer.readLine());
//
//		if(d.getServerInput().equals("RM20 B"))
//			d.setServerInput(inFromServer.readLine());
//		else
//			this.sequence9(inFromServer, outToServer);
//
//		if(!aborted())
//			this.sequence10(inFromServer, outToServer);
//		else
//			this.sequence1(inFromServer, outToServer);
//	}
//
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
//}
