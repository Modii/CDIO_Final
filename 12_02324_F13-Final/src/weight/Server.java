package weight;

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
			Main.printmenu();
			while (!(Main.inline = instream.readLine().toUpperCase()).isEmpty()){
				if(Main.inline.startsWith("CP")){
					portdst = (instream.read());
				}
				else if (Main.inline.startsWith("D")){
					if (Main.inline.equals("D"))
						Main.IndstruktionsDisplay="";
					else
						Main.IndstruktionsDisplay=(Main.inline.substring(2,
								Main.inline.length()));
					Main.printmenu();
					outstream.writeBytes("DB"+"\r\n");
				}
				else if (Main.inline.startsWith("T")){
					outstream.writeBytes("T " + (Main.tara) + " kg "+"\r\n");
					Main.tara=Main.brutto;
					Main.printmenu();
				}
				else if (Main.inline.startsWith("S")){
					Main.printmenu();
					outstream.writeBytes("S " + (Main.brutto-Main.tara)+ " kg " +"\r\n");
					outstream.flush();
				}
				else if (Main.inline.startsWith("RM20")){
					synchronized(lock){
						String[] temp = Main.inline.split("\"");
						Main.IndstruktionsDisplay = temp[1];
						Main.printmenu();
						outstream.writeBytes("RM20 B \r\n");
						lock.wait();
						outstream.writeBytes("RM20 A \"" + Local.getSvar() + "\"\r\n");
						outstream.flush();
					}
				}
				else if (Main.inline.startsWith("B")){ // denne ordre findes
					//ikke på en fysisk vægt
					String temp= Main.inline.substring(2,Main.inline.length());
					Main.brutto = Double.parseDouble(temp);
					Main.printmenu();
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
			System.out.println("Exception: "+e.getMessage());
		}
	}

	public static Object getLock() {
		return lock;
	}
	
}
