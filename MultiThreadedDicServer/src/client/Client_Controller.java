/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package client;

import java.awt.Color;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicPasswordFieldUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Client_Controller {
	private String ip;
	private int port_num;
	private Client_GUI client_GUI;
	
	// 4 types commands
	private final static String QUERY = "query";
	private final static String ADD = "add";
	private final static String UPDATE = "update";
	private final static String REMOVE = "remove";
	
	// Empty warning
	private final static String EMPTY_WORD_WARNING = "Sorry, please enter a word.";
	private final static String EMPTY_MEANING_WARNING = "Sorry, please enter one or more meanings.";
	
	public Client_Controller(String ip, int port_num) {
	//	this.ip = "localhost";
		this.ip = ip;
		this.port_num = port_num;
		
		System.out.println("client ip adress = " + ip + " port number = " + port_num + "\n");
	}
	
	
	private class ClientThread extends Thread{
		private JSONObject requestJsonObject;
		
		public ClientThread(JSONObject requestJsonObject) {
			this.requestJsonObject = requestJsonObject;
		}
		
		@Override
		public void run() {
			Socket socket = null;
			try
			{
				// Create a stream socket bounded to any port and connect it to the
				// socket bound to the ip address and port number
				socket = new Socket(ip, port_num);
				System.out.println("Connection established");

				// Output and Input Stream
				DataInputStream input = new DataInputStream(socket.getInputStream());
			    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			    
				// Send the input string to the server by writing to the socket output stream
			    output.writeUTF(this.requestJsonObject.toJSONString());
			    output.flush();
			    
			    
			    JSONParser parser = new JSONParser();
			    
				JSONObject response = (JSONObject) parser.parse(input.readUTF());
			    OperateResponse(response);
	    		
	    		input.close();
	    		output.close();
			}
			catch (UnknownHostException e)
			{
				displayException("Error occurs, the IP address is not valid!");
				e.printStackTrace();
			} catch (IllegalArgumentException  e) {
				displayException("Error occurs, the port number is invalid!");
				e.printStackTrace();
			} catch (ConnectException e) {
				displayException("Connection error occurs, the ip address or the port number is invalid!");
				e.printStackTrace();
			} catch (ParseException e) {
				displayException("Error occurs, there is a json parse error!");
				e.printStackTrace();
			} catch (IOException e){
				displayException("Error occurs, there is an IO error!");
				e.printStackTrace();
			} catch (Exception e) {
				displayException("Error occurs, there is an unknown error!");
				e.printStackTrace();
			}
			finally
			{
				// Close the socket
				if (socket != null)
				{
					try
					{
						socket.close();
					}
					catch (IOException e) 
					{
						displayException("Error occurs, there is an IO error!");
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void displayException(String exceptionString) {
		System.out.println(exceptionString);
		JLabel error_label = client_GUI.getError_Label();
		error_label.setForeground(Color.RED);
		error_label.setText(exceptionString);
	}
	
	
	public void setGUI(Client_GUI client_GUI) {
		// TODO Auto-generated method stub
		this.client_GUI = client_GUI;
	}
	
	
	public void send_request(String word, String meaning, String requestString) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("word", word);
		map.put("meaning", meaning);
		map.put("requestString", requestString);
		
		JSONObject requestJsonObject = new JSONObject(map);
		
		new ClientThread(requestJsonObject).start();
	}


	// set text area, text field, label to null
	public void clear() {
		client_GUI.geTextField().setText(null);
		client_GUI.getTextArea().setText(null);
		client_GUI.getWord_Label().setText(null);
		client_GUI.getError_Label().setText(null);
	}

	
	public void query(String word) {
		if (isWordEmpty()) {
			System.out.println("\nword input is empty");
			return;
		}else {
			System.out.println("\nquery word = " + word);
			send_request(word, "", Client_Controller.QUERY);
		}
	}


	public void add(String word, String meaning) {

		if (isWordEmpty() || isMeaningEmpty()) {
			System.out.println("\nsomething is empty");
			return;
		}else {
			System.out.println("\nadd word = " + word);
			System.out.println("meaning = " + meaning);
			send_request(word, meaning, Client_Controller.ADD);
		}
	}


	public void remove(String word) {
		if (isWordEmpty()) {
			System.out.println("\nword input is empty");
			return;
		}else {
			System.out.println("\ndelete word = " + word);
			send_request(word, "", Client_Controller.REMOVE);
		}
		
	}
	
	
	public void update(String word, String meaning) {
		if (isWordEmpty() || isMeaningEmpty()) {
			System.out.println("\nsomething is empty");
			return;
		}else {
			System.out.println("\nupdate word = " + word);
			System.out.println("update meaning = " + meaning);
			send_request(word, meaning, Client_Controller.UPDATE);
		}
		
	}
	
	
	private boolean isWordEmpty() {
		String wordString = client_GUI.geTextField().getText();
		JLabel word_label = client_GUI.getWord_Label();
		if (wordString.isEmpty()) {
			word_label.setText(EMPTY_WORD_WARNING);
			word_label.setForeground(Color.RED);
		}else {
			word_label.setText(null);
		}
		
		return wordString.isEmpty();
	}
	

	private boolean isMeaningEmpty() {
		String meaningString = client_GUI.getTextArea().getText();
		JLabel error_label = client_GUI.getError_Label();
		if (meaningString.isEmpty()) {
			error_label.setText(EMPTY_MEANING_WARNING);
			error_label.setForeground(Color.RED);
		}else {
			error_label.setText(null);
		}
		
		return meaningString.isEmpty();
	}
	
	private void OperateResponse(JSONObject response) {
		String message = (String) response.get("message");
		String meaning = (String) response.get("meaning");
		Boolean status = (Boolean) response.get("status");
		
		System.out.println("message = " + message);
		System.out.println("meaning = " + meaning);
		System.out.println("status = " + status);
		
		// set the meanings to text area
		client_GUI.getTextArea().setText(meaning);
		
		JLabel error_label = client_GUI.getError_Label();
		error_label.setText(message);
		if (status) {
			error_label.setForeground(Color.GREEN);
		}else {
			error_label.setForeground(Color.RED);
		}
	}
}
