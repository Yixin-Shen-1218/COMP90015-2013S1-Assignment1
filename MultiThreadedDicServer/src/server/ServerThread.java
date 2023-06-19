/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.basic.BasicPasswordFieldUI;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerThread extends Thread{
	private Socket client_socket;
	private Dictionary dictionary;
	private Server_GUI server_GUI;
	// the return elements to client
	private String meaning = "";
	private String message = "";
	private boolean status = true; 
	
	
	
	// the 4 types commands
	private final static String QUERY = "query";
	private final static String ADD = "add";
	private final static String REMOVE = "remove";
	private final static String UPDATE = "update";

	
	// prompts send to client
	private final static String ADD_SUCCESS_MSG = "New word is added successfully.";
	private final static String ADD_DUPLICATE_MSG = "Sorry, the word is already in the dictionary.";
	private final static String QUERY_SUCCESS_MSG = "Query successed.";
	private final static String QUERY_NOT_FOUND_MSG = "Sorry, the word is not found in the dictionary, please add it first.";
	private final static String REMOVE_SUCCESS_MSG = "The word is removed successfully.";
	private final static String REMOVE_NOT_FOUND_MSG = "Sorry, the word is not found in the dictionary, please add it first.";
	private final static String UPDATE_SUCCESS_MSG = "The meaning(s) of word is updated successfully.";
	private final static String UPDATE_NOT_FOUND_MSG = "Sorry, the word is not found in the dictionary, please add it first.";


	private final static String SERVER_FILE_ERROR_MSG = "File is not found error occurs.";
	private final static String SERVER_IO_ERROR_MSG = "Server IO error occurs.";
	private final static String SERVER_PARSE_ERROR_MSG = "Server parse error occurs.";
	
	
	public ServerThread(Socket client_socket, Dictionary dictionary, Server_GUI server_GUI) {
		this.client_socket = client_socket;
		this.dictionary = dictionary;
		this.server_GUI = server_GUI;
	}
	
	private void OperateDic(JSONObject request) {
		String word = (String) request.get("word");
		String meaning = (String) request.get("meaning");
		String requestString = (String) request.get("requestString");
		
		System.out.println("word = " + word);
		System.out.println("meaning = " + meaning);
		System.out.println("requestString = " + requestString);
		
		switch (requestString) {
		case QUERY:
			makeQueryRequest(word);
			break;
		case ADD:
			makeAddRequest(word, meaning);
			break;
		case REMOVE:
			makeRemoveRequest(word);
			break;
		case UPDATE:
			makeUpdateRequest(word, meaning);
			break;
		default:
			break;
		}
	}
	
	private void makeQueryRequest(String word) {
		meaning = dictionary.queryWord(word);
		if (meaning == "") {
			message = QUERY_NOT_FOUND_MSG;
			status = false;
		}else {
			message = QUERY_SUCCESS_MSG;
			status = true;
		}
	}
	
	private void makeAddRequest(String word, String meaning) {
		try {
			status = dictionary.addWord(word, meaning);			
			if (status) {
				message = ADD_SUCCESS_MSG;
			}else {
				message = ADD_DUPLICATE_MSG;
			}
		} catch (FileNotFoundException e) {
			message = SERVER_FILE_ERROR_MSG;
			e.printStackTrace();
		} catch (IOException e) {
			message = SERVER_IO_ERROR_MSG;
			e.printStackTrace();
		}
	}
	
	private void makeRemoveRequest(String word) {
		try {
			status = dictionary.removeWord(word);
			if (status) {
				message = REMOVE_SUCCESS_MSG;
			}else {
				message = REMOVE_NOT_FOUND_MSG;
			}
		} catch (FileNotFoundException e) {
			message = SERVER_FILE_ERROR_MSG;
			e.printStackTrace();
		} catch (IOException e) {
			message = SERVER_IO_ERROR_MSG;
			e.printStackTrace();
		}
	}
	
	private void makeUpdateRequest(String word, String meaning) {
		try {
			status = dictionary.updateWord(word, meaning);
			if (status) {
				message = UPDATE_SUCCESS_MSG;
			}else {
				message = UPDATE_NOT_FOUND_MSG;
			}
		} catch (FileNotFoundException e) {
			message = SERVER_FILE_ERROR_MSG;
			e.printStackTrace();
		} catch (IOException e) {
			message = SERVER_IO_ERROR_MSG;
			e.printStackTrace();
		}
	}
	


	private JSONObject MakeResponse() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meaning", meaning);
		map.put("message", message);
		map.put("status", status);
		
		JSONObject requestJsonObject = new JSONObject(map);
		
		return requestJsonObject;
	}

	@Override
    public void run() {
        try {
//        	System.out.println("\nHere is the server thread!!!!!!");
        	
			// Input stream
			DataInputStream input = new DataInputStream(client_socket.getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(client_socket.getOutputStream());
		    
		    JSONParser parser = new JSONParser();
		    
		    try {
				JSONObject request = (JSONObject) parser.parse(input.readUTF());
			    OperateDic(request);
			} catch (ParseException e) {
				message = SERVER_PARSE_ERROR_MSG;
				status = false;
				e.printStackTrace();
			}
    		
		    // make response and send to client
		    JSONObject response = MakeResponse();
		    output.writeUTF(response.toJSONString());		    
    		input.close();
    		output.close();
            client_socket.close();
        } catch (IOException ex) {
        	message = SERVER_IO_ERROR_MSG;
        	status = false;
            ex.printStackTrace();
        }
    }
}
