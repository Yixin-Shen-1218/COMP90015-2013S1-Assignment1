/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package server;

import java.awt.print.Printable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import client.Client_GUI;

public class Server {
	// the port number of server
	private int port_num;
	// the listening socket of server
	private ServerSocket listening_socket = null;
	// the default dictionary
	private static Dictionary dictionary;
	// the server thread
	private ServerThread server_thread = null;
	// the number of request
	private static int request_num = 0;
	
	private static Server_GUI server_GUI;
	
	public Server(int port_num, String dic_path) {
		this.port_num = port_num;
		Server.dictionary = new Dictionary(dic_path);
	}
	
	public static void main(String[] args) {
		try {
			int port_num = Integer.parseInt(args[0]);
			String dic_path = args[1];
			
			// check the port_num, if the port_num is less than 1024 or greater than 65535
			// prompt error
			if (port_num < 1024 || port_num > 65535) {
				System.out.println("Error, port number should between 1024 and 65535");
				System.exit(-1);
			}
			
			System.out.println("Read the default dictionary" + dic_path);
			
			Server server = new Server(port_num, dic_path);
			server.run();
		} catch (NumberFormatException e) {
			System.out.println("Wrong format of input to run the jar file, please input port number and dictionary path!");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Wrong format of input to run the jar file, please input port number and dictionary path!");
		}
	}

	private void run() {
		server_GUI = new Server_GUI(request_num, dictionary);
		server_GUI.frame.setVisible(true);
		
		try {
			// create server listening socket when the server run
			listening_socket = new ServerSocket(this.port_num);
			System.out.println("server start listening, port number = " + port_num);

			Server.request_num = 0; //counter to keep track of the number of clients
			JLabel request_label = server_GUI.getRequestLabel();
			
			request_label.setText("THE NUMBER OF REQUEST: " + request_num);
			
			//Listen for incoming connections for ever 
			while (true)
			{	
				//Accept an incoming client connection request 
				Socket client_socket = listening_socket.accept(); //This method will block until a connection request is received
				request_num++;
				System.out.println("\nClient conection request number " + request_num + " accepted:");
//				System.out.println("Remote Hostname: " + client_socket.getInetAddress().getHostName());
				System.out.println("Local Port: " + client_socket.getLocalPort());
				
				request_label.setText("THE NUMBER OF REQUEST: " + request_num);
				
				server_thread = new ServerThread(client_socket, dictionary, server_GUI);
				server_thread.start();
			}
		}
		catch (BindException e)
		{
			System.out.println("The port has been used, try another port or kill the process");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("IO Exception!");
			e.printStackTrace();
		}
		finally
		{
			if(listening_socket != null)
			{
				try
				{
					// close the server socket
					listening_socket.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
