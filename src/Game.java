/* 
 * Project 4: Battleship
 * Author: Margi Katwala (mkatwa3), Rime Brika (rbrika2), Anusha Pai (apai7)
 * Professor Troy and Wei
 * Fall 2017
 * Game: in charge of creating the connection between the server and client
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Game{
	private static boolean connected = false;
	public static boolean serverMode;

	public static Board board;

	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	public static Socket echoSocket;

	public static ObjectOutputStream out;
	public static ObjectInputStream in;



	public static void main(String args[]) {

		board = new Board("Board");
	}
	public static void closeSockets(){
		try{
			serverSocket.close();
			clientSocket.close();
			echoSocket.close();
		}
		catch(Exception ex){}
	}

	@SuppressWarnings("unchecked")
	public static void setUpServer() {
		//server = new Board("Server");
		serverSocket = null;
		try { 
			serverSocket = new ServerSocket(10007);
			System.out.println("Server Info:");
			serverSocket.getInetAddress();
			System.out.println("    IP address: " + InetAddress.getLocalHost().getHostAddress());
			System.out.println("    Port      : " + serverSocket.getLocalPort());
			// BufferedReader and BufferWriter -> send the ip address and port to the client
		} 
		catch (IOException e) 
		{ 
			System.err.println("Could not listen on port: 10007."); 
			System.exit(1); 
		} 

		clientSocket = null;

		// Waiting for Client
		try { 
			System.out.println ("Waiting for Client");
			clientSocket = serverSocket.accept();
			System.out.println ("Established Connection");
			connected=true;
		} 
		catch (IOException e) 
		{ 
			System.err.println("Accept failed."); 
			System.exit(1); 
		} 
		board.setMode("Server");
		board.connectionEstablished();
		board.setLabel("You may began placing ships now");


		// Getting input and output streams
		out=null;
		in=null;

		// reading and writing data from/to stream
		try {
			out = new ObjectOutputStream(
					clientSocket.getOutputStream());
			in = new ObjectInputStream(
					clientSocket.getInputStream());
			board.setIn(in);
			board.setOut(out);
		} catch (Exception ex) {
			System.err.println("Error occured : setUpServer()");
		}


	}
	@SuppressWarnings("unchecked")
	public static void setUpClient() {
		echoSocket = null;
		out = null;
		in = null;
		//client = new Board("Client");

		while(!connected) {
			try {
				echoSocket = new Socket("127.0.0.1", 10007);
				board.setMode("Client");
				board.connectionEstablished();



				out = new ObjectOutputStream(echoSocket.getOutputStream());
				in = new ObjectInputStream(echoSocket.getInputStream());
				connected = true;
				board.setLabel("You may began placing ships now");

				board.setIn(in);
				board.setOut(out);

			} catch (UnknownHostException e) {
				//System.err.println("Not able to recognize given host");
			} catch (IOException e) {
				//System.err.println("Couldn't get I/O for "
				// + "the connection.");
			}

		}

	}
}

