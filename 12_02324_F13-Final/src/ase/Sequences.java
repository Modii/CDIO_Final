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
	// (1)	Opstart
	//-----------------------------------------------------------------
	public void sequence1(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException

	{
		data.setServerInput(inFromServer.readLine());
		this.sequence2(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (2)	Operatør indtaster operatørnummer og vægten svarer tilbage med navn og godkendes af bruger.
	//-----------------------------------------------------------------
	public void sequence2(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			this.RMPrintOgRead(20, 8, "Indtast operatoernummer", inFromServer, outToServer);
			this.tjekMsgFejlOgRead(2, inFromServer, outToServer);
			this.checkForCancel(inFromServer, outToServer);
			data.setOprID(this.splitInt(inFromServer, outToServer));

			if (!func.testId("" + data.getOprID())){
				this.RMPrintOgRead(49, 2, "Dette operatoer ID eksisterer ikke!", inFromServer, outToServer);
				this.tjekMsgFejlOgRead(2, inFromServer, outToServer);
				this.sequence2(inFromServer, outToServer);
			}
			else{
				String weightMsg = "Operatoer: " + mOpr.getOperatoer(data.getOprID()).getOprNavn() + ". Tryk OK for at bruge det valgte ID eller CANCEL for at vaelge et andet.";
				this.RMPrintOgRead(49, 4, weightMsg, inFromServer, outToServer);
				this.tjekMsgFejlOgRead(2, inFromServer, outToServer);
				if(data.getServerInput().equals("RM49 A 1"))
					this.sequence3(inFromServer, outToServer);
				else if(data.getServerInput().equals("RM49 A 2"))
					this.sequence2(inFromServer, outToServer);
			}
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (3)	Operatør indtaster produktbatchnummer
	//-----------------------------------------------------------------
	public void sequence3(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		this.RMPrintOgRead(20, 8, "Indtast produktbatchnr.", inFromServer, outToServer);
		this.tjekMsgFejlOgRead(3, inFromServer, outToServer);
		checkForCancel(inFromServer, outToServer);
		data.setPbID(this.splitInt(inFromServer, outToServer));

		// Ikke eksisterende PbID.
		if (!func.testPbId(data.getPbID())){
			this.RMPrintOgRead(49, 2, "Dette produktbatch ID eksisterer ikke!", inFromServer, outToServer);
			this.tjekMsgFejlOgRead(3, inFromServer, outToServer);
			data.setServerInput(inFromServer.readLine());
			this.tjekMsgFejlOgRead(3, inFromServer, outToServer);
			this.sequence3(inFromServer, outToServer);
		} 

		// Allerede afsluttet PB (status=2).
		else if(mPb.getProduktBatch(data.getPbID()).getStatus() == 2){
			this.RMPrintOgRead(49, 2, "Produktbatch allerede afsluttet!", inFromServer, outToServer);
			this.tjekMsgFejlOgRead(3, inFromServer, outToServer);
			data.setServerInput(inFromServer.readLine());
			this.tjekMsgFejlOgRead(3, inFromServer, outToServer);
			this.sequence3(inFromServer, outToServer);	
		}
		else
			data.setReceptID(mPb.getProduktBatch(data.getPbID()).getReceptId());
		data.setListen(mRecKomp.getReceptKompList(data.getReceptID()));
		this.sequence4(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (4)	Vægten svarer tilbage med navn på recept
	//-----------------------------------------------------------------
	public void sequence4(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			String weightMsg = "Recept: " + mRec.getRecept(mPb.getProduktBatch(data.getPbID()).getReceptId()).getReceptNavn() + ". Tryk OK for at bruge den valgte recept eller CANCEL for at vaelge en anden.";
			this.RMPrintOgRead(49, 4, weightMsg, inFromServer, outToServer);
			this.tjekMsgFejlOgRead(4, inFromServer, outToServer);

			if(data.getServerInput().equals("RM49 A 1"))
				this.sequence5(inFromServer, outToServer);
			else if(data.getServerInput().equals("RM49 A 2"))
				this.sequence3(inFromServer, outToServer);
			else this.sequence4(inFromServer, outToServer);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (5)	Vægten fortæller Operatør hvilken råvare han skal afveje nu
	//-----------------------------------------------------------------

	public void sequence5(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try{
			if (data.getListen().size() > 0) {
				int id = data.getListen().get(data.getListen().size()-1).getRaavareId();
				//Gemmer raavareID til senere brug.
				data.setRaavareID(id);
				data.getListen().remove(data.getListen().size()-1);
				this.RMPrintOgRead(49, 4, "Du skal nu afveje raavaren: " + mRaa.getRaavare(id).getRaavareNavn(), inFromServer, outToServer);
				this.tjekMsgFejlOgRead(5, inFromServer, outToServer);
				if(data.getServerInput().equals("RM49 A 1"))
					this.sequence6(inFromServer, outToServer);
				else if(data.getServerInput().equals("RM49 A 2"))
					this.sequence3(inFromServer, outToServer);
				else this.sequence5(inFromServer, outToServer);
			}
			else
				this.sequence17(inFromServer, outToServer);
		}
		catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (6)	Operatøren kontrollerer at vægten er ubelastet og trykker ’ok’
	//-----------------------------------------------------------------

	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		RMPrintOgRead(49, 2, "Aflast vaegten og tryk OK for at tarere.", inFromServer, outToServer);
		tjekMsgFejlOgRead(6, inFromServer, outToServer);
		this.sequence7(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (7) Tarerer vægten uden at gemme tara.
	//-----------------------------------------------------------------	

	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{

		outToServer.writeBytes("T\r\n");
		data.setServerInput(inFromServer.readLine());
		this.tjekMsgFejlOgRead(7, inFromServer, outToServer);
		this.sequence8(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (8) Vægt beder om første tara-beholder.
	//-----------------------------------------------------------------	

	public void sequence8(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{
		this.RMPrintOgRead(49, 2, "Anbring taraholder. Tryk dernaest OK.", inFromServer, outToServer);
		this.tjekMsgFejlOgRead(8, inFromServer, outToServer);
		this.sequence9(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (9) Vægt tareres
	//-----------------------------------------------------------------
	public void sequence9(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		outToServer.writeBytes("T\r\n");
		data.setServerInput(inFromServer.readLine());
		
		this.tjekMsgFejlOgRead(13, inFromServer, outToServer);
		
		if(data.getServerInput().startsWith("T S")){
			data.setSplittedInput(data.getServerInput().split(" "));
			data.setTara(Double.parseDouble(data.getSplittedInput()[7]));
			this.sequence13(inFromServer, outToServer);
		}
		else this.sequence9(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (11) Vægten beder om raavarebatch nummer på råvare.
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

			// Udregner tolerance ud fra angivet tolerance og nominel nettovægt.
			data.setTolerance(); data.setTotPosTol(); data.setTotNegTol();

			data.setWeightMsg("Du har valgt: " + mRaa.getRaavare(mRaaB.getRaavareBatch(data.getRbID()).getRaavareId()).getRaavareNavn() + ". Tryk OK for at paabegynde afvejning. Den kraevede maengde skal være mellem " + data.getTotNegTol() + " kg og " + data.getTotPosTol() +" kg. Tryk AFVEJ for at afveje.");
			outToServer.writeBytes("RM49 2 \"" + data.getWeightMsg() + "\"\r\n");	
			outToServer.flush();
			data.setServerInput(inFromServer.readLine());
			data.setServerInput(inFromServer.readLine());
			this.sequence14(inFromServer, outToServer);
		}
		else this.sequence13(inFromServer, outToServer);
	}
	//-----------------------------------------------------------------
	// (12) Bruger afvejer og trykker ok.
	//-----------------------------------------------------------------
	public void sequence14(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		// Skifter til vejedisplay.
		outToServer.writeBytes("DW" + "\r\n");
		data.setServerInput(inFromServer.readLine());

		// Tilføjer tolerence-pil ud vha. udregnet tolerance.
		outToServer.writeBytes("P121 " + mRecKomp.getReceptKomp(data.getReceptID(), data.getRaavareID()).getNomNetto() + " kg " + data.getTolerance() + " kg " + data.getTolerance() + " kg " + "\r\n");
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

				if(data.getNetto() >= data.getTotNegTol() && data.getNetto() <= data.getTotPosTol() && data.getServerInput().startsWith("S S")){
					RaavareBatchDTO raavareBatch = mRaaB.getRaavareBatch(data.getRbID());
					raavareBatch.setMaengde(raavareBatch.getMaengde() - data.getNetto());
					mRaaB.updateRaavareBatch(raavareBatch);

					mPbKomp.createProduktBatchKomp(new ProduktBatchKompDTO(data.getPbID(), data.getRbID(), data.getTara(), data.getNetto(), data.getOprID()));
					ProduktBatchDTO produktBatch = mPb.getProduktBatch(data.getPbID());
					produktBatch.setStatus(1);
					mPb.updateProduktBatch(produktBatch);
					this.sequence5(inFromServer, outToServer);
				}
				else{
					data.setWeightMsg("Ugyldig vejning. Den nominelle nettoveagt skal være mellem " + data.getTotNegTol() + " kg og " + data.getTotPosTol() + " kg ifoelge databasens toleranceveardier. Undgå yderligere, at vægten er i overbelastning eller underbelastning.");
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
				this.sequence2(inFromServer, outToServer);
			}
			else this.sequence17(inFromServer, outToServer);
		}
		else this.sequence17(inFromServer, outToServer);
	}



	public int splitInt(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		data.setSplittedInput(data.getServerInput().split(" "));
		int returnSplitInt = Integer.parseInt(data.getSplittedInput()[2].replaceAll("\"",""));
		return returnSplitInt;
	}

	public double splitDouble(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
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

	public void checkForCancel(BufferedReader inFromServer, DataOutputStream outToServer){
		System.out.println("Hvad cancel tjekker: " + data.getServerInput());
		if(data.getServerInput().contains(" C"))
			try {
				this.sequence2(inFromServer, outToServer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			return;
	}


	// Denne metode kan bruges adskillige steder i koden. Den får som input at vide, hvilken sekvens, den skal gå til, hvis vægtens retur-besked er en fejlbesked.
	// Metoden er til for at undgå bunker af if og else.
	// Hvis vægten returnerer en acceptabel kommando, vender metoden blot tilbage til sekvensen.
	// Kan eventuelt udvides med en RM49 2, der siger, at der opstod en fejl.
	public void tjekMsgFejlOgRead(int sekvensVedFejl, BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{
		try{
			if(data.getServerInput().contains(" B") || data.getServerInput().contains(" A")){
				System.out.println("Tjekkede: " + data.getServerInput());
				data.setServerInput(inFromServer.readLine());
				return;
			}
			else if (data.getServerInput().contains("T S"))
				return;
			else if(data.getServerInput().contains(" I") || data.getServerInput().contains(" L") || data.getServerInput().contains("T +") || data.getServerInput().contains("T -") || data.getServerInput().contains("S +") || data.getServerInput().contains("S -")){
				switch(sekvensVedFejl){
				case 3: this.sequence2(inFromServer, outToServer);
				break;

				case 4: this.sequence2(inFromServer, outToServer);
				break;

				case 5: this.sequence3(inFromServer, outToServer);
				break;

				case 6: this.sequence4(inFromServer, outToServer);
				break;

				case 65: this.sequence5(inFromServer, outToServer);
				break;

				case 7: this.sequence6(inFromServer, outToServer);
				break;

				case 8: this.sequence7(inFromServer, outToServer);
				break;

				case 910: this.sequence8(inFromServer, outToServer);
				break;

			//	case 11: this.sequence11(inFromServer, outToServer);
				//break;

				case 12: this.sequence9(inFromServer, outToServer);
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
		catch (DALException e) {
			e.printStackTrace();
		}
	}
}
