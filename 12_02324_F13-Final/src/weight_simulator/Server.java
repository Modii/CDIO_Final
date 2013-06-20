package weight_simulator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class Server implements Runnable{
	private static final Object lock = new Object();
	private static BufferedReader instream;
	private static DataOutputStream outstream;
	private static int portdst = 8000;
	public Server(int port) {
		portdst = port;
	}

	public void run(){
		try{

			Main.listener = new ServerSocket(portdst);
			System.out.println("Venter på connection på port " + portdst );
			System.out.println("Indtast eventuel portnummer som 1. argument");
			System.out.println("på kommandolinjen for andet portnr.");
			Main.sock = Main.listener.accept();
			instream = new BufferedReader(new InputStreamReader(Main.sock.getInputStream()));
			outstream = new DataOutputStream(Main.sock.getOutputStream());
			Main.printMenu();
			outstream.writeBytes("I4 A \"3154308\"\r\n");
			while (!(Main.inline = instream.readLine().toUpperCase()).isEmpty()){
				System.out.println("Streng modtaget:"+Main.inline);
				if(Main.inline.startsWith("CP")){
					portdst = (instream.read());
				}
				else if (Main.inline.startsWith("DW")){
					Main.printMenu();
					outstream.writeBytes("DW A\r\n");
					outstream.flush();
				}
				else if (Main.inline.startsWith("D")){
					if (Main.inline.equals("D"))
						Main.instruktionsDisplay="";
					else
						Main.instruktionsDisplay=(Main.inline.substring(2,
								Main.inline.length()));
					Main.printMenu();
					outstream.writeBytes("DB"+"\r\n");
				}
				else if (Main.inline.startsWith("T")){
					Main.tara=Main.brutto;
					outstream.writeBytes("T S      " + (Main.tara) + " kg "+"\r\n");
					Main.printMenu();
				}
				else if (Main.inline.startsWith("S")){
					Main.printMenu();
					outstream.writeBytes("S S      " + (Main.brutto)+ " kg " +"\r\n");
					outstream.flush();
				}
				else if (Main.inline.startsWith("P121")){
					Main.printMenu();
					outstream.writeBytes("P121 A\r\n");
					outstream.flush();
				}
				else if (Main.inline.startsWith("RM20")){
					synchronized(lock){
						String[] temp = Main.inline.split("\"");
						Main.instruktionsDisplay = temp[1];
						Main.printMenu();
						outstream.writeBytes("RM20 B\r\n");
						lock.wait();
						if(Local.getSvar().equals("CANCEL")){
							outstream.writeBytes("RM20 C \r\n");
						}
						else{
							outstream.writeBytes("RM20 A \"" + Local.getSvar() + "\"\r\n");
							outstream.flush();
						}
					}
				}
				else if (Main.inline.startsWith("RM30")){
					String[] temp = Main.inline.split("\""); //KNAPPENAVN
					Local.setKnap(temp[1]);
					outstream.writeBytes("RM30 B\r\n");
					outstream.flush();
				}
				else if (Main.inline.startsWith("RM39")){
					synchronized(lock){
						outstream.writeBytes("RM39 A\r\n");
						outstream.flush();
						Main.instruktionsDisplay = "Knap tilfoejet: "+Local.getKnap();
						Main.printMenu();
						lock.wait();
						outstream.writeBytes("RM30 A 1\r\n");
					}
				}
				else if (Main.inline.startsWith("RM49")){
					synchronized(lock){
						String[] temp = Main.inline.split("\"");
						Main.instruktionsDisplay = temp[1];
						Main.printMenu();
						outstream.writeBytes("RM49 B\r\n");
						lock.wait();
						if (Local.getSvar().equals("OK")) 
							outstream.writeBytes("RM49 A 1\r\n");
						else if (Local.getSvar().equals("CANCEL"))
							outstream.writeBytes("RM49 A 2\r\n");
						outstream.flush();
					}
				}
				else if (Main.inline.startsWith("B")){ // denne ordre findes
					//ikke på en fysisk vægt
					String temp= Main.inline.substring(2,Main.inline.length());
					Main.brutto = Double.parseDouble(temp);
					Main.printMenu();
					outstream.writeBytes("DB"+"\r\n");
					outstream.flush();
				}
				else if ((Main.inline.startsWith("Q"))){
					System.out.println("");
					System.out.println("Program stoppet Q modtaget paa com port");
					System.in.close();
					System.out.close();
					instream.close();
					outstream.close();
					System.exit(0);
				}
			}
		}
		catch (Exception e){
			System.out.println("Exception: ");
			e.printStackTrace();
		}
	}

	public static Object getLock() {
		return lock;
	}

}
