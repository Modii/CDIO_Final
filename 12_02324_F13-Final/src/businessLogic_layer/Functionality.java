package businessLogic_layer;

import java.util.Random;
import java.util.Scanner;

import dao_interfaces.IOperatoerDAO;
import dao_interfaces.IProduktBatchDAO;
import dao_interfaces.IRaavareBatchDAO;
import dao_interfaces.IRaavareDAO;
import dao_interfaces.IReceptDAO;
import db_mysqldao.MySQLOperatoerDAO;
import db_mysqldao.MySQLProduktBatchDAO;
import db_mysqldao.MySQLRaavareBatchDAO;
import db_mysqldao.MySQLRaavareDAO;
import db_mysqldao.MySQLReceptDAO;
import dao_interfaces.DALException;

public class Functionality implements IFunctionality{

	private IProduktBatchDAO produktbatchDAO = new MySQLProduktBatchDAO();
	private IOperatoerDAO oprDAO = new MySQLOperatoerDAO();
	private IRaavareDAO raavareDAO = new MySQLRaavareDAO();
	private IReceptDAO receptDAO = new MySQLReceptDAO();
	private IRaavareBatchDAO raavarebatchDAO = new MySQLRaavareBatchDAO();

	public Functionality(IProduktBatchDAO produktbatchDAO, IOperatoerDAO oprDAO, IRaavareDAO raavareDAO, IReceptDAO receptDAO, IRaavareBatchDAO raavarebatchDAO) {
		super();
		this.produktbatchDAO = produktbatchDAO;
		this.oprDAO = oprDAO;
		this.raavareDAO = raavareDAO;
		this.receptDAO = receptDAO;
		this.raavarebatchDAO = raavarebatchDAO;
	}

	public Functionality(){

	}

	Scanner scan = new Scanner(System.in);

	/**
	 * Genererer automatisk et password ud fra de givet kriterier 
	 */
	public String generatePassword() {
		String password = ""; // Indeholder password
		int numOfLowercase = 0; // Antal af Lowercase char's i passwd
		int numOfUppercase = 0; // Antal af Upppercase..
		int numOfNumber = 0;
		int numOfSymbol = 0;
		int differentTypes = 0; // Forskellige kategorier/typer -
		// Lowercase,Uppercase, osv i passwd.
		String[] arrayOfTypes = { "Lowercase", "Uppercase", "Number", "Symbol" };
		while (password.length() < 6 || differentTypes < 3) {
			Random r = new Random();
			switch (arrayOfTypes[r.nextInt(arrayOfTypes.length)]) {
			case "Lowercase":
				password += randomLowercase();
				numOfLowercase++;
				if (numOfLowercase <= 1) {
					differentTypes++;
				}
				break;
			case "Uppercase":
				password += randomUppercase();
				numOfUppercase++;
				if (numOfUppercase <= 1) {
					differentTypes++;
				}
				break;
			case "Number":
				password += randomNumber();
				numOfNumber++;
				if (numOfNumber <= 1) {
					differentTypes++;
				}
				break;
			case "Symbol":
				password += randomSymbol();
				numOfSymbol++;
				if (numOfSymbol <= 1) {
					differentTypes++;
				}
				break;
			}
		}
		return password;
	}

	public static char randomLowercase() {
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'a');
		return c;
	}
	public static char randomUppercase() {
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'A');
		return c;
	}

	public static char randomNumber() {
		Random r = new Random();
		char c = (char) (r.nextInt(9) + '0');
		return c;
	}

	public static char randomSymbol() {
		Random r = new Random();
		char[] arrayOfSymbols = { '.', '-', '_', '+', '!', '?', '=' };
		char c = (char) arrayOfSymbols[r.nextInt(arrayOfSymbols.length)];
		return c;
	}

	public void askForOprName() {
		scan.nextLine();
	}

	/**
	 * Her sammenlignes operatør id fra brugerinput med eksisterende id'er i vores Array
	 * Fra index {0;9} indeholder listen objekter med parametrerne null.
	 * Derfor bruges catch p� NullPointerException, s� man ikke kan tilg� disse objekter.  
	 */
	public boolean testId(int i) throws DALException {
		try {
			return (i == oprDAO.getOperatoer(i).getOprId());
		}

		catch (IndexOutOfBoundsException e) {
			throw new DALException("ID findes ikke");
		}
		catch (NullPointerException f) {
			throw new DALException("ID findes ikke");
		}
		catch (DALException g) {
			return false;
		}
	}
	/**
	 * Her sammenlignes pb id fra brugerinput med eksisterende id'er i vores Array på samme måde som ovenover.
	 */
	public boolean testPbId(int i) throws DALException {
		try {
			return (i == produktbatchDAO.getProduktBatch(i).getPbId());
		}

		catch (IndexOutOfBoundsException e) {
			throw new DALException("ID findes ikke");
		}
		catch (NullPointerException f) {
			throw new DALException("ID findes ikke");
		}
	}

	public boolean testRaavareId(int i) throws DALException {
		try {
			return (i == raavarebatchDAO.getRaavareBatch(i).getRaavareId());
		}
		catch (IndexOutOfBoundsException e) {
			throw new DALException("ID findes ikke");
		}
		catch (NullPointerException f) {
			throw new DALException("ID findes ikke");
		}
	}
	/**
	 * Her sammenlignes password fra brugerinput med eksisterende password i vores Array 
	 * baseret p� det f�rstindtastede id (int i) som er unikt for operat�r til operat�r  
	 */
	public boolean testPassword(int i, String s) throws DALException {
		if ((s.equals(oprDAO.getOperatoer(i).getPassword())))
			return true;
		else
			throw new DALException("Password invalid!");
	}

	/**
	 * Her valideres hvert enkelt element i det indtastede password for at overholde
	 * de givet kriterier for et password  
	 */
	public boolean askForNewPassword(int oprId, String newPassword) throws DALException {
		boolean containLowerCase = false, containUpperCase = false, containNumber = false, containSymbol = false;
		int differentTypes = 0;

		for(int i = 0; i < newPassword.length(); i++) {
			if (checkLowercase(newPassword.charAt(i)) && !containLowerCase){
				containLowerCase = true;
				differentTypes++;
			}
			else if (checkUppercase(newPassword.charAt(i)) && !containUpperCase){
				containUpperCase = true;
				differentTypes++;
			}
			else if (checkNumber(newPassword.charAt(i)) && !containNumber){
				containNumber = true;
				differentTypes++;
			}
			else if (checkSymbol(newPassword.charAt(i)) && !containSymbol){
				containSymbol = true;
				differentTypes++;
			}
		}
		if (newPassword.length() >= 6 && differentTypes >= 3) {
			oprDAO.getOperatoer(oprId).setPassword(newPassword);
			return true;
		}

		return false;
	}

	/**
	 * Her tjekkers om det f�rst indtastede nye password er ens 
	 * med det andet indtastede nye password  
	 */
	public boolean checkIfIdentical(String s, String k){
		return (s.equals(k));
	}

	public static boolean checkLowercase(char c) {
		return (c >= 'a' && c <= 'z');
	}
	public static boolean checkUppercase(char c) {
		return (c >= 'A' && c <= 'Z');
	}
	public static boolean checkNumber(char c) {
		return (c >= '0' && c <= '9');
	}
	public static boolean checkSymbol(char c) {
		return (c == '.' || c == '-' || c == '_' || c == '+' | c == '!' || c == '?' || c == '=');
	}

	/**
	 * Her udregnes netto-v�gten ud fra brugerinputs, hvilket sker under menupunkt 3  
	 */
	public double calculateWeight(double tare, double brutto) {
		double netto = brutto - tare;
		return netto;
	}

	public IRaavareDAO getRaavareDAO() {
		return raavareDAO;
	}
	public IReceptDAO getReceptDAO() {
		return receptDAO;
	}

	public IOperatoerDAO getOprDAO() {
		return oprDAO;
	}

	public void setOprDAO(IOperatoerDAO oprDAO) {
		this.oprDAO = oprDAO;
	}


}
