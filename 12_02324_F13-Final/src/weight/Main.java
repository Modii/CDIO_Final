package weight;


import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	
	static ServerSocket listener;
	static double brutto=0;
	static double tara=0;
	static String inline;
	static String IndstruktionsDisplay= "";
	static Socket sock;

	public static void printmenu(){
		for (int i=0;i<20;i++)
			System.out.println(" ");

		System.out.println("*************************************************");
		System.out.println("Netto: " + (brutto-tara)+ " kg" );
		System.out.println("Display: " + IndstruktionsDisplay );
		System.out.println("*************************************************");
		System.out.println(" ");
		System.out.println("Debug info: ");
		System.out.println("Hooked up to " + sock.getInetAddress() );
		System.out.println("Brutto: " + (brutto)+ " kg" );
		System.out.println("Streng modtaget: "+inline) ;
		System.out.println(" ");
		System.out.println("Denne vægt simulator lytter på ordrene:");
		System.out.println("D, DN, S, T, B, Q ");
		System.out.println("på kommunikationsporten. ");
		System.out.println(" ");
		System.out.println("*************************************************");
		System.out.println("\nTast 'T' for tara.");
		System.out.println("Tast 'B' for ny brutto (eksempel: \" B 20.5 \").");
		System.out.println("Tast 'Q' for at slukke vægten.");
		System.out.println("Tast 'S' efterfulgt af dit svar for at svare på forespørgsel (eksempel: \" S 'svar' \").");
		System.out.println("Tast 'O' for at trykke OK");
		System.out.println("Tast 'C' for at trykke CANCEL");
		System.out.println("Tast 'knappenavn' for at trykke på en tilfoejet knap.");
		System.out.println(" ");
		System.out.println("*************************************************");
		System.out.print ("Tast her: ");
	}
	public static void main(String[] args){
		int port = 8000;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		Thread t1 = new Thread(new Local());
		Thread t2 = new Thread(new Server(port));
		
		t1.start();
		t2.start();
	}
}
