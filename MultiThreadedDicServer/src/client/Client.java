/*
 * Author: Yixin SHEN <yixishen1@student.unimelb.edu.au>
 * Student ID: 1336242
 */

package client;

public class Client {
	public static void main(String[] args) {
		try {
			String ip = args[0];
			int port_num = Integer.parseInt(args[1]);
			
			Client_Controller controller = new Client_Controller(ip, port_num);
			Client_GUI GUI_window = new Client_GUI(controller);
			controller.setGUI(GUI_window);
			GUI_window.frame.setVisible(true);

		}catch(Exception e) {
			System.out.println("Wrong format of input to run the jar file, please try again!");
		}
	}
}