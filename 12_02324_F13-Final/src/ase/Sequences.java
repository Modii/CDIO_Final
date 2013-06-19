package ase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import businessLogic_layer.Functionality;
import dao_interfaces.DALException;
import db_mysqldao.MySQLOperatoerDAO;
import db_mysqldao.MySQLProduktBatchDAO;
import db_mysqldao.MySQLProduktBatchKompDAO;
import db_mysqldao.MySQLRaavareBatchDAO;
import db_mysqldao.MySQLRaavareDAO;
import db_mysqldao.MySQLReceptDAO;
import db_mysqldao.MySQLReceptKompDAO;
import dto.ProduktBatchDTO;
import dto.ProduktBatchKompDTO;
import dto.RaavareBatchDTO;

public class Sequences {

	private Functionality func = new Functionality();
	private MySQLOperatoerDAO mOpr = new MySQLOperatoerDAO();
	private MySQLProduktBatchDAO mPb = new MySQLProduktBatchDAO();
	private MySQLProduktBatchKompDAO mPbKomp = new MySQLProduktBatchKompDAO();
	private MySQLReceptDAO mRec = new MySQLReceptDAO();
	private MySQLRaavareDAO mRaa = new MySQLRaavareDAO();
	private MySQLRaavareBatchDAO mRaaB = new MySQLRaavareBatchDAO();
	private MySQLReceptKompDAO mRecKomp = new MySQLReceptKompDAO();
	private Data data = new Data();
	private Other other = new Other();

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
			data.setOprID(Integer.parseInt(data.getSplittedInput()[2].replaceAll("\"","")));
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
			if (func.testId("" + data.getOprID())){
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
						this.sequence3(inFromServer, outToServer);
					}
				}
				else this.sequence3(inFromServer, outToServer);
			}
			else this.sequence3(inFromServer, outToServer);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (5)	Operatør indtaster produktbatchnummer
	//-----------------------------------------------------------------
	public void sequence5(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		data.setWeightMsg("Indtast produktbatchnr.");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM20 B")){
			data.setServerInput(inFromServer.readLine());
			data.setSplittedInput(data.getServerInput().split(" "));
			data.setPbID(Integer.parseInt(data.getSplittedInput()[2].replaceAll("\"","")));
			data.setReceptID(mPb.getProduktBatch(data.getPbID()).getReceptId());
			data.setListen(mRecKomp.getReceptKompList(data.getReceptID()));

			// Ikke eksisterende PbID.
			if (!func.testPbId(data.getPbID())){
				System.out.println("Erm den virker vidst");
				data.setWeightMsg("Dette produktbatch ID eksisterer ikke!");
				outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());

				if (data.getServerInput().equals("RM49 B")){
					data.setServerInput(inFromServer.readLine());
					if (data.getServerInput().equals("RM49 A 1"))
						this.sequence5(inFromServer, outToServer);
				} 
				else
					this.sequence5(inFromServer, outToServer);
			}

			// Allerede afsluttet PB (status=2).
			else if(mPb.getProduktBatch(data.getPbID()).getStatus() == 2){
				data.setWeightMsg("Produktbatch allerede afsluttet!");
				outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());

				if(data.getServerInput().equals("RM49 B")){
					data.setServerInput(inFromServer.readLine());
					if(data.getServerInput().equals("RM49 A 1"))
						this.sequence5(inFromServer, outToServer);	
				}
				else
					this.sequence5(inFromServer, outToServer);
			}
			else
				this.sequence6(inFromServer, outToServer);
		}
		else
			this.sequence3(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (6)	Vægten svarer tilbage med navn på recept
	//-----------------------------------------------------------------
	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			if(func.testPbId(data.getPbID())){
				data.setWeightMsg("Recept: " + mRec.getRecept(mPb.getProduktBatch(data.getPbID()).getReceptId()).getReceptNavn() + ". Tryk OK for at bruge den valgte recept eller CANCEL for at vaelge en anden.");
				outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
				outToServer.flush();
				data.setServerInput(inFromServer.readLine());

				if(data.getServerInput().equals("RM49 B")){
					data.setServerInput(inFromServer.readLine());
					if(data.getServerInput().equals("RM49 A 1"))
						this.sequence6_5(inFromServer, outToServer);
					else if(data.getServerInput().equals("RM49 A 2"))
						this.sequence5(inFromServer, outToServer);
					else this.sequence6(inFromServer, outToServer);
				}
				this.sequence6(inFromServer, outToServer);
			}
			else
				this.sequence5(inFromServer, outToServer);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (6,5)	Vægten fortæller Operatør hvilken råvare han skal afveje nu
	//-----------------------------------------------------------------

	public void sequence6_5(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try{
			System.out.println("ProduktbatchID: " + data.getPbID());

			if (data.getListen().size() > 0) {
				int id = data.getListen().get(data.getListen().size()-1).getRaavareId();
				data.setRaavareID(id); //Gemmer raavareID til senere brug.
				String navn = mRaa.getRaavare(id).getRaavareNavn();
				data.getListen().remove(data.getListen().size()-1);
				data.setWeightMsg("Du skal nu afveje raavaren: " + navn);
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
				this.sequence17(inFromServer, outToServer);
			}
		}

		catch (DALException e) {
			e.printStackTrace();
		}
	}



	//-----------------------------------------------------------------
	// (7)	Operatøren kontrollerer at vægten er ubelastet og trykker ’ok’
	//-----------------------------------------------------------------

	// Sekvens 7 efter reducering.
	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		RMPrintOgRead(49, 2, "Aflast vaegten og tryk OK for at tarere.", inFromServer, outToServer);
		tjekForFejl(7, inFromServer.readLine(), inFromServer, outToServer);
		data.setServerInput(inFromServer.readLine());
		tjekForFejl(7, inFromServer.readLine(), inFromServer, outToServer);
		this.sequence8(inFromServer, outToServer);
	}

	// Sekvens 7 før reducering.
	//
	//	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	//	{
	//		data.setWeightMsg("Aflast vaegten og tryk OK for at tarere.");
	//		outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
	//		outToServer.flush();
	//		data.setServerInput(inFromServer.readLine());
	//	if(data.getServerInput().equals("RM49 B"))
	//	{
	//		data.setServerInput(inFromServer.readLine());
	//		if(data.getServerInput().equals("RM49 A 1")){
	//			this.sequence8(inFromServer, outToServer);
	//		}
	//		else {
	//			this.sequence7(inFromServer, outToServer);
	//		}
	//	}
	//	else {
	//		this.sequence7(inFromServer, outToServer);
	//	}
	//}



	//-----------------------------------------------------------------
	// (8) Vægt tareres
	//-----------------------------------------------------------------	

	public void sequence8(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{

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

	public void sequence9_10(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{
		data.setWeightMsg("Anbring taraholder. Tryk dernaest OK.");
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
	public void sequence11(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		// Vægten af tarabeholder registreres
		// Mangler registrering af tara.
		this.sequence12(inFromServer, outToServer);

	}
	//-----------------------------------------------------------------
	// (12) Vægt tareres
	//-----------------------------------------------------------------
	public void sequence12(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		outToServer.writeBytes("T\r\n");
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().startsWith("T S")){
			data.setSplittedInput(data.getServerInput().split(" "));
			data.setTara(Double.parseDouble(data.getSplittedInput()[7]));
			this.sequence13(inFromServer, outToServer);
		}
		else this.sequence12(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (13) Vægten beder om raavarebatch nummer på råvare.
	//-----------------------------------------------------------------
	public void sequence13(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{
		data.setWeightMsg("Indtast raavarebatchnr.");
		outToServer.writeBytes("RM20 8 \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());

		if(data.getServerInput().equals("RM20 B")){
			data.setServerInput(inFromServer.readLine());
			data.setSplittedInput(data.getServerInput().split(" "));
			data.setRbID(Integer.parseInt(data.getSplittedInput()[2].replaceAll("\"","")));
			if(mRaaB.getRaavareBatch(data.getRbID()).getMaengde() < mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getNomNetto()){
				data.setWeightMsg("Der er ikke nok '" + mRaa.getRaavare(mRaaB.getRaavareBatch(data.getRbID()).getRaavareId()).getRaavareNavn() + "' i raavarebatchen. Vaelg venligst en anden raavarebatch.");
				outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
				data.setServerInput(inFromServer.readLine());
				if(data.getServerInput().equals("RM49 B")){
					data.setServerInput(inFromServer.readLine());
					this.sequence13(inFromServer, outToServer);
				}
				else
					this.sequence13(inFromServer, outToServer);
			}	

			data.setWeightMsg("Du har valgt: " + mRaa.getRaavare(mRaaB.getRaavareBatch(data.getRbID()).getRaavareId()).getRaavareNavn() + ". Tryk OK for at paabegynde afvejning. Naar den oenskede maengde er afvejet, tryk da paa AFVEJ");
			outToServer.writeBytes("RM49 4 \"" + data.getWeightMsg() + "\"\r\n");	
			outToServer.flush();
			data.setServerInput(inFromServer.readLine());
			data.setServerInput(inFromServer.readLine());
			this.sequence14(inFromServer, outToServer);
		}
		else this.sequence13(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (14) Bruger afvejer og trykker ok.
	//-----------------------------------------------------------------
	public void sequence14(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		// Skifter til vejedisplay.
		outToServer.writeBytes("DW" + "\r\n");
		data.setServerInput(inFromServer.readLine());

		// Udregner tolerance ud fra angivet tolerance og nominel nettovægt.
		double tolerance = ((mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getNomNetto()) * (mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getTolerance()) / 100);
		double totPosTol = (mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getNomNetto()) + tolerance;
		double totNegTol = (mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getNomNetto()) - tolerance;

		// Tilføjer tolerence-pil ud vha. udregnet tolerance.
		outToServer.writeBytes("P121 " + mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getNomNetto() + " kg " + tolerance + " kg " + tolerance + " kg " + "\r\n");
		data.setServerInput(inFromServer.readLine());

		// Opretter en softknap til vejedisplay
		outToServer.writeBytes("RM30 \"" + "Afvej" + "\"\r\n");

		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM30 B")){
			//
			outToServer.writeBytes("RM39 1" + "\r\n");
			data.setServerInput(inFromServer.readLine());
			data.setServerInput(inFromServer.readLine());

			if(data.getServerInput().equals("RM30 A 1")){
				outToServer.writeBytes("S\r\n");
				data.setServerInput(inFromServer.readLine());
				data.setSplittedInput(data.getServerInput().split(" "));
				data.setNetto(Double.parseDouble(data.getSplittedInput()[7]));

				if(data.getNetto() >= totNegTol && data.getNetto() <= totPosTol && data.getServerInput().startsWith("S S")){
					RaavareBatchDTO raavareBatch = mRaaB.getRaavareBatch(data.getRbID());
					raavareBatch.setMaengde(raavareBatch.getMaengde() - data.getNetto());
					mRaaB.updateRaavareBatch(raavareBatch);

					mPbKomp.createProduktBatchKomp(new ProduktBatchKompDTO(data.getPbID(), data.getRbID(), data.getTara(), data.getNetto(), data.getOprID()));
					ProduktBatchDTO produktBatch = mPb.getProduktBatch(data.getPbID());
					produktBatch.setStatus(1);
					mPb.updateProduktBatch(produktBatch);
					this.sequence6_5(inFromServer, outToServer);
				}
				else{
					data.setWeightMsg("Ugyldig vejning. Den nominelle nettoveagt skal være mellem " + totNegTol + " kg og " + totPosTol + " kg ifoelge databasens toleranceveardier. Undgå yderligere, at vægten er i overbelastning eller underbelastning.");
					outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
					data.setServerInput(inFromServer.readLine());
					data.setServerInput(inFromServer.readLine());
					this.sequence14(inFromServer, outToServer);
				}
			}
			else this.sequence14(inFromServer, outToServer);
		}
		else this.sequence14(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (17) Systemet spørger om der skal startes forfra eller afsluttes
	//-----------------------------------------------------------------	
	public void sequence17(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		try {
			ProduktBatchDTO produktBatch = mPb.getProduktBatch(data.getPbID());
			produktBatch.setStatus(2);
			produktBatch.setSlutDato(other.generateDato());
			mPb.updateProduktBatch(produktBatch);
		} catch (DALException e) {
			e.printStackTrace();
		}
		data.setWeightMsg("Produktionsforskrift gennemfoert. Tryk OK for at paabegynde en ny afvejningsprocedure eller sluk vaegten.");
		outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
		if(data.getServerInput().equals("RM49 B"))
		{
			data.setServerInput(inFromServer.readLine());
			if(data.getServerInput().equals("RM49 A 1")){
				this.sequence3(inFromServer, outToServer);
			}
			else this.sequence17(inFromServer, outToServer);
		}
		else this.sequence17(inFromServer, outToServer);
	}

	
	
	public int splitInt(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setServerInput(inFromServer.readLine());
		data.setSplittedInput(data.getServerInput().split(" "));
		int returnSplitInt = Integer.parseInt(data.getSplittedInput()[2].replaceAll("\"",""));
		return returnSplitInt;
	}

	public double splitDouble(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setServerInput(inFromServer.readLine());
		data.setSplittedInput(data.getServerInput().split(" "));
		double returnSplitDouble = (Double.parseDouble(data.getSplittedInput()[7]));
		return returnSplitDouble;
	}
	
	// Metoden tjekker hvilken type RM-kommando, der er tale om. Derefter kommer den krævede efterfulgte integerværdi og dernæst vægtbeskeden.
	public void RMPrintOgRead(int RMType, int x1, String weightMsg, BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		data.setWeightMsg(weightMsg);
		if(RMType == 49)
			outToServer.writeBytes("RM49 " + x1 + " \"" + data.getWeightMsg() + "\"\r\n");	
		else if (RMType == 20)
			outToServer.writeBytes("RM20 " + x1 + " \"" + data.getWeightMsg() + "\" \" \" \"&3\"\r\n");
		outToServer.flush();
		data.setServerInput(inFromServer.readLine());
	}


	// Denne metode kan bruges adskillige steder i koden. Den får som input at vide, hvilken sekvens, den skal gå til, hvis vægtens retur-besked er en fejlbesked.
	// Metoden er til for at undgå bunker af if og else.
	// Hvis vægten returnerer en acceptabel kommando, vender metoden blot tilbage til sekvensen.
	// Kan eventuelt udvides med en RM49 2, der siger, at der opstod en fejl.
	public void tjekForFejl(int sekvensVedFejl, String kommandoModtaget, BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{
		if(kommandoModtaget.contains(" B") || kommandoModtaget.contains(" A") || kommandoModtaget.contains("T S") )
			return;
		else if(kommandoModtaget.contains(" I") || kommandoModtaget.contains(" L") || kommandoModtaget.contains("T +") || kommandoModtaget.contains("T -")){
			switch(sekvensVedFejl){

			case 3: this.sequence3(inFromServer, outToServer);
			break;

			case 4: this.sequence4(inFromServer, outToServer);
			break;

			case 5: this.sequence5(inFromServer, outToServer);
			break;

			case 6: this.sequence6(inFromServer, outToServer);
			break;

			case 65: this.sequence6(inFromServer, outToServer);
			break;

			case 7: this.sequence7(inFromServer, outToServer);
			break;

			case 8: this.sequence8(inFromServer, outToServer);
			break;

			case 910: this.sequence9_10(inFromServer, outToServer);
			break;

			case 11: this.sequence11(inFromServer, outToServer);
			break;

			case 12: this.sequence12(inFromServer, outToServer);
			break;

			case 13: this.sequence13(inFromServer, outToServer);
			break;

			case 14: this.sequence14(inFromServer, outToServer);
			break;

			case 17: this.sequence17(inFromServer, outToServer);
			break;
			}
		}
	}
}
