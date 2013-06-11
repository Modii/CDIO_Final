package java_ASE;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import db_connection.Connector;

public class Main {




	public static void main(String[] args) throws Exception { 
		new Connector();
		Sequences s = new Sequences();

		Socket clientSocket = new Socket("localhost", 4567); 

		DataOutputStream outToServer = 
				new DataOutputStream(clientSocket.getOutputStream()); 

		BufferedReader inFromServer = 
				new BufferedReader(new
						InputStreamReader(clientSocket.getInputStream())); 
		

		s.sequence3(inFromServer, outToServer);

		clientSocket.close();
	}

}
