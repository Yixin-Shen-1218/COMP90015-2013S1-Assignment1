/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class Client_GUI {

	JFrame frame;
	private Client_Controller controller;
	private JTextField textField;
	private JTextArea textArea;
	private JLabel error_label;
	private JLabel word_label;
	/**
	 * Create the application.
	 */
	public Client_GUI(Client_Controller controller) {
		this.controller = controller;
		initialize();
	}

	public JTextField geTextField() {
		return this.textField;
	}
	
	public JTextArea getTextArea() {
		return this.textArea;
	}

	public JLabel getError_Label() {
		return this.error_label;
	}
	
	public JLabel getWord_Label() {
		return this.word_label;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dictionary Application");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel.setBounds(305, 10, 210, 50);
		frame.getContentPane().add(lblNewLabel);
		
		// Button to clear all the text in area
		JButton Clear_Button = new JButton("Clear");
		Clear_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clear();
			}
		});
		
		Clear_Button.setBounds(616, 110, 105, 40);
		frame.getContentPane().add(Clear_Button);
		
		JLabel lblNewLabel_1 = new JLabel("Please enter a word: ");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1.setBounds(82, 71, 245, 30);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 15));
		textField.setBounds(82, 111, 454, 40);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Meanings: ");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_2.setBounds(82, 192, 197, 30);
		frame.getContentPane().add(lblNewLabel_2);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 15));
		textArea.setBounds(82, 232, 454, 241);
		frame.getContentPane().add(textArea);
		
		JButton Query_Button = new JButton("Query");
		Query_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				controller.query(word);
			}
		});
		
		Query_Button.setBounds(616, 232, 105, 40);
		frame.getContentPane().add(Query_Button);
		
		JButton Add_Button = new JButton("Add");
		Add_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				String meaning = textArea.getText();
				controller.add(word, meaning);
			}
		});
		Add_Button.setBounds(616, 295, 105, 40);
		frame.getContentPane().add(Add_Button);
		
		
		JButton Remove_Button = new JButton("Remove");
		Remove_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				controller.remove(word);
			}
		});
		Remove_Button.setBounds(616, 358, 105, 40);
		frame.getContentPane().add(Remove_Button);
		
		
		JButton Update_Button = new JButton("Update");
		Update_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				String meaning = textArea.getText();
				controller.update(word, meaning);
			}
		});
		Update_Button.setBounds(616, 420, 105, 40);
		frame.getContentPane().add(Update_Button);

		error_label = new JLabel("");
		error_label.setBounds(82, 481, 454, 30);
		frame.getContentPane().add(error_label);
		
		word_label = new JLabel("");
		word_label.setBounds(82, 152, 454, 30);
		frame.getContentPane().add(word_label);
	}
}
