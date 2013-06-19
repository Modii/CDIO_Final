package ase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

import db_connection.Connector;

public class Main {

	public static void main(String[] args) throws Exception {
		new Connector();
		Sequences s = new Sequences();
		Socket clientSocket = null;
		Scanner scan = new Scanner(System.in);
		boolean connected = false;
		while (!connected) {
			System.out.println("Indtast IP til vægt:");
			String ip = scan.nextLine();
			if (ip.length() == 0)
				ip = "127.0.0.1";
			System.out.println("IP: "+ip);
			System.out.println("Indtast port til vægt:");
			String port = scan.nextLine();
			if (port.length() == 0)
				port = "8000";
			System.out.println("Port: "+port);
			connected = true;
			try {
				clientSocket = new Socket(ip, Integer.parseInt(port));
			}
			catch (NumberFormatException e) {
				System.out.println("NumberFormatException i port. Prøv igen!");
				connected = false;
			}
			catch (ConnectException e) {
				System.out.println("Kunne ikke forbinde til "+ip+":"+port+". Prøv igen!");
				connected = false;
			}
		}
		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		
		s.sequence2(inFromServer, outToServer);

		clientSocket.close();
	}
}