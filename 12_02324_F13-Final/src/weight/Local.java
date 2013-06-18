package weight;

import java.util.Scanner;

public class Local implements Runnable {

	private static String svar;
	private static String knappeNavn;

	public void run(){
		Scanner scan = new Scanner(System.in);
		String input = "";
		try{
			while(!input.startsWith("Q")){	
				input = scan.nextLine();
				if(input.startsWith("B")){
					String temp= input.substring(2,input.length());
					Main.brutto = Double.parseDouble(temp);
					Main.printmenu();
				}
				else if(input.startsWith("Q")){
					System.out.println("Vægten slukkes!");
					scan.close();
					System.out.close();
					System.exit(0);
				}
				else if(input.startsWith("T")){
					Main.tara = Main.brutto;
					Main.printmenu();			
				}
				else if(input.startsWith("S")){
					synchronized (Server.getLock()) {
						svar = input.substring(2);
						Server.getLock().notify();
						Main.printmenu();
					}
				}
				else if(input.equals("O")){
					synchronized (Server.getLock()) {
						svar = "OK";
						Server.getLock().notify();
						Main.printmenu();
					}
				}
				else if(input.equals("C")){
					synchronized (Server.getLock()) {
						svar = "CANCEL";
						Server.getLock().notify();
						Main.printmenu();
					}
				}
				else if (input.equals(getKnap())) {
					synchronized (Server.getLock()) {
						Server.getLock().notify();
					}
				}

			}
		}
		catch(Exception e){
			System.out.println("Exception: "+e.getMessage());
		}
	}

	public static String getSvar(){
		return svar;
	}
	public static void setKnap(String knappeNavn){
		Local.knappeNavn = knappeNavn;
	}
	public static String getKnap() {
		return Local.knappeNavn;
	}
}

