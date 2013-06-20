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
	private SequenceHelper seqH = new SequenceHelper();
	private MySQLOperatoerDAO mOpr = new MySQLOperatoerDAO();
	private MySQLProduktBatchDAO mPb = new MySQLProduktBatchDAO();
	private MySQLProduktBatchKompDAO mPbKomp = new MySQLProduktBatchKompDAO();
	private MySQLReceptDAO mRec = new MySQLReceptDAO();
	private MySQLRaavareDAO mRaa = new MySQLRaavareDAO();
	private MySQLRaavareBatchDAO mRaaB = new MySQLRaavareBatchDAO();
	private MySQLReceptKompDAO mRecKomp = new MySQLReceptKompDAO();

	//-----------------------------------------------------------------
	// (1)	Opstart
	//-----------------------------------------------------------------
	public void sequence1(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException

	{
		seqH.setServerInput(inFromServer.readLine());
		this.sequence2(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (2)	Operatør indtaster operatørnummer og vægten svarer tilbage med navn og godkendes af bruger.
	//-----------------------------------------------------------------
	public void sequence2(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			seqH.RMPrintOgRead(20, 8, "Indtast operatoernummer", inFromServer, outToServer);
			seqH.tjekMsgFejlOgRead(2, inFromServer, outToServer);
			seqH.checkForCancel(inFromServer, outToServer);
			seqH.setOprID(seqH.splitInt(inFromServer, outToServer));

			if (!func.testId("" + seqH.getOprID())){
				seqH.RMPrintOgRead(49, 2, "Dette operatoer ID eksisterer ikke!", inFromServer, outToServer);
				seqH.tjekMsgFejlOgRead(2, inFromServer, outToServer);
				this.sequence2(inFromServer, outToServer);
			}
			else{
				String weightMsg = "Operatoer: " + mOpr.getOperatoer(seqH.getOprID()).getOprNavn() + ". Tryk OK for at bruge det valgte ID eller CANCEL for at vaelge et andet.";
				seqH.RMPrintOgRead(49, 4, weightMsg, inFromServer, outToServer);
				seqH.tjekMsgFejlOgRead(2, inFromServer, outToServer);
				if(seqH.getServerInput().equals("RM49 A 1"))
					this.sequence3(inFromServer, outToServer);
				else if(seqH.getServerInput().equals("RM49 A 2"))
					this.sequence2(inFromServer, outToServer);
			}
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (3)	Operatør indtaster produktbatchnummer.
	//-----------------------------------------------------------------
	public void sequence3(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		seqH.RMPrintOgRead(20, 8, "Indtast produktbatchnr.", inFromServer, outToServer);
		seqH.tjekMsgFejlOgRead(3, inFromServer, outToServer);
		seqH.checkForCancel(inFromServer, outToServer);
		seqH.setPbID(seqH.splitInt(inFromServer, outToServer));

		// Ikke eksisterende PbID.
		if (!func.testPbId(seqH.getPbID())){
			seqH.RMPrintOgRead(49, 2, "Dette produktbatch ID eksisterer ikke!", inFromServer, outToServer);
			seqH.tjekMsgFejlOgRead(3, inFromServer, outToServer);
			this.sequence3(inFromServer, outToServer);
		} 

		// Allerede afsluttet PB (status=2).
		else if(mPb.getProduktBatch(seqH.getPbID()).getStatus() == 2){
			seqH.RMPrintOgRead(49, 2, "Produktbatch allerede afsluttet!", inFromServer, outToServer);
			seqH.tjekMsgFejlOgRead(3, inFromServer, outToServer);
			this.sequence3(inFromServer, outToServer);	
		}
		else
			seqH.setReceptID(mPb.getProduktBatch(seqH.getPbID()).getReceptId());
		seqH.setListen(mRecKomp.getReceptKompList(seqH.getReceptID()));
		this.sequence4(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (4)	Vægten svarer tilbage med navn på recept.
	//-----------------------------------------------------------------
	public void sequence4(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try {
			String weightMsg = "Recept: " + mRec.getRecept(mPb.getProduktBatch(seqH.getPbID()).getReceptId()).getReceptNavn() + ". Tryk OK for at bruge den valgte recept eller CANCEL for at vaelge en anden.";
			seqH.RMPrintOgRead(49, 4, weightMsg, inFromServer, outToServer);
			seqH.tjekMsgFejlOgRead(4, inFromServer, outToServer);

			if(seqH.getServerInput().equals("RM49 A 1"))
				this.sequence5(inFromServer, outToServer);
			else if(seqH.getServerInput().equals("RM49 A 2"))
				this.sequence3(inFromServer, outToServer);
			else this.sequence4(inFromServer, outToServer);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (5)	Vægten fortæller Operatør hvilken råvare han skal afveje nu.
	//-----------------------------------------------------------------

	public void sequence5(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException
	{
		try{
			if (seqH.getListen().size() > 0) {
				int id = seqH.getListen().get(seqH.getListen().size()-1).getRaavareId();
				//Gemmer raavareID til senere brug.
				seqH.setRaavareID(id);
				seqH.getListen().remove(seqH.getListen().size()-1);
				seqH.RMPrintOgRead(49, 4, "Du skal nu afveje raavaren: " + mRaa.getRaavare(id).getRaavareNavn(), inFromServer, outToServer);
				seqH.tjekMsgFejlOgRead(5, inFromServer, outToServer);
				if(seqH.getServerInput().equals("RM49 A 1"))
					this.sequence6(inFromServer, outToServer);
				else if(seqH.getServerInput().equals("RM49 A 2"))
					this.sequence3(inFromServer, outToServer);
				else this.sequence5(inFromServer, outToServer);
			}
			else
				this.sequence12(inFromServer, outToServer);
		}
		catch (DALException e) {
			e.printStackTrace();
		}
	}

	//-----------------------------------------------------------------
	// (6)	Operatøren kontrollerer at vægten er ubelastet og trykker OK.
	//-----------------------------------------------------------------

	public void sequence6(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		seqH.RMPrintOgRead(49, 2, "Aflast vaegten og tryk OK for at tarere.", inFromServer, outToServer);
		seqH.tjekMsgFejlOgRead(6, inFromServer, outToServer);
		this.sequence7(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (7) Tarerer vægten.
	//-----------------------------------------------------------------	

	public void sequence7(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{

		outToServer.writeBytes("T\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(7, inFromServer, outToServer);
		this.sequence8(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (8) Vægt beder om første tara-beholder.
	//-----------------------------------------------------------------	

	public void sequence8(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{
		seqH.RMPrintOgRead(49, 2, "Anbring taraholder. Tryk dernaest OK.", inFromServer, outToServer);
		seqH.tjekMsgFejlOgRead(8, inFromServer, outToServer);
		this.sequence9(inFromServer, outToServer);
	}

	//-----------------------------------------------------------------
	// (9) Vægt tareres
	//-----------------------------------------------------------------
	public void sequence9(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		outToServer.writeBytes("T\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(9, inFromServer, outToServer);
		seqH.setTara(seqH.splitDouble(inFromServer, outToServer));
		this.sequence10(inFromServer, outToServer);
	}


	//-----------------------------------------------------------------
	// (10) Vægten beder om raavarebatch nummer på råvare.
	//-----------------------------------------------------------------
	public void sequence10(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException{

		seqH.RMPrintOgRead(20, 8, "Indtast raavarebatchnr.", inFromServer, outToServer);
		seqH.tjekMsgFejlOgRead(10, inFromServer, outToServer);
		seqH.checkForCancel(inFromServer, outToServer);
		seqH.setRbID(seqH.splitInt(inFromServer, outToServer));

		if(mRaaB.getRaavareBatch(seqH.getRbID()).getMaengde() < mRecKomp.getReceptKomp(seqH.getReceptID(), seqH.getRaavareID()).getNomNetto()){
			String weightMsg = "Der er ikke nok '" + mRaa.getRaavare(mRaaB.getRaavareBatch(seqH.getRbID()).getRaavareId()).getRaavareNavn() + "' i raavarebatchen. Vaelg venligst en anden raavarebatch.";
			seqH.RMPrintOgRead(49, 2, weightMsg, inFromServer, outToServer);
			seqH.tjekMsgFejlOgRead(10, inFromServer, outToServer);
		}	
		else{
			// Udregner tolerance ud fra angivet tolerance og nominel nettovægt.
			seqH.setTolerance(); seqH.setTotPosTol(); seqH.setTotNegTol();
			String weightMsg = "Du har valgt: " + mRaa.getRaavare(mRaaB.getRaavareBatch(seqH.getRbID()).getRaavareId()).getRaavareNavn() + ". Tryk OK for at paabegynde afvejning. Den kraevede maengde skal vaere mellem " + seqH.getTotNegTol() + " kg og " + seqH.getTotPosTol() +" kg. Tryk AFVEJ i den efterfoelgende skaerm for at afveje.";
			seqH.RMPrintOgRead(49, 2, weightMsg, inFromServer, outToServer);
			seqH.setServerInput(inFromServer.readLine());
			this.sequence11(inFromServer, outToServer);
		}
	}

	//-----------------------------------------------------------------
	// (11) Bruger afvejer og trykker ok.
	//-----------------------------------------------------------------
	public void sequence11(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException, DALException
	{
		// Skifter til vejedisplay.
		outToServer.writeBytes("DW" + "\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(11, inFromServer, outToServer);

		// Tilføjer tolerence-pil vha. udregnet tolerance.
		outToServer.writeBytes("P121 " + mRecKomp.getReceptKomp(seqH.getReceptID(), seqH.getRaavareID()).getNomNetto() + " kg " + seqH.getTolerance() + " kg " + seqH.getTolerance() + " kg " + "\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(11, inFromServer, outToServer);

		// Opretter en softknap til vejedisplay
		outToServer.writeBytes("RM30 \"" + "Afvej" + "\"\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(11, inFromServer, outToServer);

		// Anbringer den oprettede softkey.
		outToServer.writeBytes("RM39 1" + "\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(11, inFromServer, outToServer);

		// Afvejer hvis der ikke sker fejl ved softkey-tryk. Der kan kun trykkes på 1 korrekt knap, 
		// derfor tjekkes der ikke for nogen bestemt knap korrekt knap (RM30 A 1)
		outToServer.writeBytes("S\r\n");
		seqH.setServerInput(inFromServer.readLine());
		seqH.tjekMsgFejlOgRead(9, inFromServer, outToServer);
		seqH.setNetto(seqH.splitDouble(inFromServer, outToServer));
		// System.out.println("Netto: " + seqH.getNetto() +". TotNegTol: " + seqH.getTotNegTol() + ". TotPosTol: " + seqH.getTotPosTol());

		if(seqH.getNetto() >= seqH.getTotNegTol() && seqH.getNetto() <= seqH.getTotPosTol()){
			RaavareBatchDTO raavareBatch = mRaaB.getRaavareBatch(seqH.getRbID());
			raavareBatch.setMaengde(raavareBatch.getMaengde() - seqH.getNetto());
			mRaaB.updateRaavareBatch(raavareBatch);

			mPbKomp.createProduktBatchKomp(new ProduktBatchKompDTO(seqH.getPbID(), seqH.getRbID(), seqH.getTara(), seqH.getNetto(), seqH.getOprID()));
			ProduktBatchDTO produktBatch = mPb.getProduktBatch(seqH.getPbID());
			produktBatch.setStatus(1);
			mPb.updateProduktBatch(produktBatch);
			this.sequence5(inFromServer, outToServer);
		}
		else{
			String weightMsg = "Ugyldig vejning. Den nominelle nettovaegt skal vaere mellem " + seqH.getTotNegTol() + " kg og " + seqH.getTotPosTol() + " kg ifoelge databasens tolerancevaerdier. Undgaa yderligere, at vaegten er i overbelastning eller underbelastning.";
			seqH.RMPrintOgRead(49, 2, weightMsg, inFromServer, outToServer);
			seqH.setServerInput(inFromServer.readLine());
			this.sequence11(inFromServer, outToServer);
		}
	}

	//-----------------------------------------------------------------
	// (12) Systemet spørger om der skal startes forfra eller afsluttes
	//-----------------------------------------------------------------	
	public void sequence12(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException{

		try {
			ProduktBatchDTO produktBatch = mPb.getProduktBatch(seqH.getPbID());
			produktBatch.setStatus(2);
			produktBatch.setSlutDato(seqH.generateDato());
			mPb.updateProduktBatch(produktBatch);
		} catch (DALException e) {
			e.printStackTrace();
		}

		String weightMsg = "Produktionsforskrift gennemfoert. Tryk OK for at paabegynde en ny afvejningsprocedure eller sluk vaegten.";
		seqH.RMPrintOgRead(49, 2, weightMsg, inFromServer, outToServer);
		seqH.tjekMsgFejlOgRead(12, inFromServer, outToServer);
		this.sequence2(inFromServer, outToServer);
	}
}
