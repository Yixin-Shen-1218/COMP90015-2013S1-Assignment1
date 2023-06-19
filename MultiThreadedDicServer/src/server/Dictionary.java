/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Dictionary {
	private HashMap<String, String> dictHashMap;
	private String dic_path;
	private final static String DEFAULT_DICT_PATH = "Dictionary.dat";

	@SuppressWarnings("unchecked")
	public Dictionary(String dic_path) {
		this.dic_path = dic_path;
		
		// read the dictionary file to the application
		ObjectInputStream reader;
		try {
			reader = new ObjectInputStream(new FileInputStream(dic_path));
			this.dictHashMap = (HashMap<String, String>) reader.readObject();
			reader.close();
//			System.out.println(dictHashMap);
		} catch (FileNotFoundException e) {
			System.out.println("File not found error, the dictionary path is not valid. Trying to read the default dictionary!");
			getDefaultDict();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found error occurs. Trying to read the default dictionary!");
			getDefaultDict();
		} catch (IOException e) {
			System.out.println("IO error occurs.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public HashMap<String, String> getDictHashMap() {
		return dictHashMap;
	}
	
	// read the default dictionary file to the application
	@SuppressWarnings("unchecked")
	private void getDefaultDict() {
		ObjectInputStream reader;
		try {
			reader = new ObjectInputStream(new FileInputStream(DEFAULT_DICT_PATH));
			this.dictHashMap = (HashMap<String, String>) reader.readObject();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Default dictionary file not found. Now trying to create a blank dictionary file!");
			createDefaultDict();
		} catch (ClassNotFoundException e) {
			System.out.println("Default dictionary file not found. Now trying to create a blank dictionary file!");
			createDefaultDict();
		} catch (IOException e) {
			System.out.println("IO error occurs.");
			e.printStackTrace();
		}
	}

	// function to create a blank dictionary file
	private void createDefaultDict() {
		this.dictHashMap = new HashMap<String, String>();
		ObjectOutputStream objOut;
		try {
			objOut = new ObjectOutputStream(new FileOutputStream(DEFAULT_DICT_PATH));
			objOut.writeObject(dictHashMap);
			objOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found exception occurs.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception occurs.");
			e.printStackTrace();
		}
	}


	private void updateDicFile() throws FileNotFoundException, IOException {
        ObjectOutputStream objOut=new ObjectOutputStream(new FileOutputStream(dic_path));
        objOut.writeObject(dictHashMap);
        objOut.flush();
        objOut.close();
	}

	private boolean isWordInDict(String word) {
		return dictHashMap.containsKey(word);
	}
	
	public synchronized boolean addWord(String word, String meaning) throws FileNotFoundException, IOException {
		// if the add word is duplicate, return false
		// else, return true and update the put the word and meaning into the dictionary
		if (isWordInDict(word)) {
//			System.out.println("Sorry, the word is already in the dictionary.");
			return false;
		}else {
			dictHashMap.put(word, meaning);
			updateDicFile();
			return true;
		}
	}

	public synchronized String queryWord(String word) {
		// if the word is in the dictionary, return the meanings of the word
		// else, return ""
		if (isWordInDict(word)) {
			return dictHashMap.get(word);
		}else {
			return "";
		}
	}

	public synchronized boolean removeWord(String word) throws FileNotFoundException, IOException {
		// if the word is in the dictionary, remove the meanings of the word
		if (isWordInDict(word)) {
			dictHashMap.remove(word);
			updateDicFile();
			return true;
		}else {
			return false;
		}
	}

	public synchronized boolean updateWord(String word, String meaning) throws FileNotFoundException, IOException {
		// if the word is in the dictionary, update the meanings of the word
		if (isWordInDict(word)) {
			dictHashMap.put(word, meaning);
			updateDicFile();
			return true;
		}else {
			return false;
		}
	}
}
