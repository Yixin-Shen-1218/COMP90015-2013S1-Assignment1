/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package server;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

public class Server_GUI{
	
	JFrame frame;
	private JPanel contentPane;
	private Dictionary dictionary;
	private int request_num;
	private JLabel request_label;
	private JScrollPane scrollPane;
	private JPanel word_panel;
	
	/**
	 * Create the application.
	 */
	public Server_GUI(int request_num, Dictionary dictionary) {
		this.request_num = request_num;
		this.dictionary = dictionary;
		
		initialize();
	}
	
	public JLabel getRequestLabel() {
		return this.request_label;
	}
	
	public JScrollPane getJScrollPane() {
		return this.scrollPane;
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 150);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		request_label = new JLabel("New label");
		request_label.setFont(new Font("Arial", Font.PLAIN, 18));
		request_label.setHorizontalAlignment(SwingConstants.CENTER);
		request_label.setBounds(10, 58, 366, 30);
		frame.getContentPane().add(request_label);
		
		JLabel name_label = new JLabel("Server GUI");
		name_label.setFont(new Font("Arial", Font.PLAIN, 15));
		name_label.setHorizontalAlignment(SwingConstants.CENTER);
		name_label.setBounds(57, 10, 274, 30);
		frame.getContentPane().add(name_label);
		
//		displayDict(this.dictionary);
	}
	
	public void displayDict2(Dictionary dictionary) {
		word_panel = new JPanel();
		word_panel.setBounds(10, 70, 366, 483);
		frame.getContentPane().add(word_panel);
		
		HashMap<String, String> dictHashMap= dictionary.getDictHashMap();
		String[] keyArrays = dictHashMap.keySet().toArray(new String[dictHashMap.size()]);
		ListModel<String> jListModel = new DefaultComboBoxModel<>(keyArrays);
		JList myJList = new JList<>();
		myJList.setModel(jListModel);
		myJList.setFixedCellHeight(20);
		myJList.setFixedCellWidth(366);
		
		word_panel.add(myJList);
	}
	
	public void displayDict(Dictionary dictionary) {
		word_panel = new JPanel();
		word_panel.setBounds(10, 70, 366, 483);
		word_panel.setMaximumSize(new Dimension(366, 100));
		word_panel.setLayout(new FlowLayout());
		frame.getContentPane().add(word_panel);
		
//		label_panel.setLayout(new BorderLayout());
		
		HashMap<String, String> dictHashMap= dictionary.getDictHashMap();
		int word_num = dictHashMap.size();
		System.out.println("word number = " + word_num);
		
		for(int i=0;i<word_num;i++)
		{
			String word = (String) dictHashMap.keySet().toArray()[i];
//			String meaning = (String) dictHashMap.values().toArray()[i];
			
			System.out.println(word);
			
			JLabel label=new JLabel (word);
			label.setFont(new Font("Arial", Font.PLAIN, 18));
			label.setHorizontalAlignment(SwingConstants.LEADING);
			label.setPreferredSize(new Dimension(366, 50));
			
			word_panel.add(label);
		}
		
		scrollPane = new JScrollPane(word_panel);
		scrollPane.setBounds(10, 70, 366, 483);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane);
	}
}
