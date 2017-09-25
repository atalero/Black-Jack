//GUI was written by Dr. Victor Milenkovic from the University of Miami. Original comments are still in code. Some changes were made to adapt the GUI to the Black Jack game. 
//The GUI interface in this folder is also written by Dr. Victor Milenkovic. 
package black_jack;

import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

/**
 *
 * @author vjm
 */
public class GUI implements User_Interface {

	InnerGUI gui;


	/** Creates a new instance of GUI */
	public GUI() {
		gui = new InnerGUI();
	}

	private static class InnerGUI implements Runnable {

		public String[] commands;
		public String message;
		public String prompt;

		public int choice;
		public String response;

		
		private String windowPrompt;
		public TYPE type;

		public enum TYPE {
			COMMAND, MESSAGE, INFO
		}

		public void setPrompt(String prompt) {
			this.windowPrompt = prompt;
		}
		
		public void getCommand(String[] commands) {
			choice = JOptionPane.showOptionDialog(null, // No parent
					this.windowPrompt, // Prompt message
					"Black Jack", // Window title
					JOptionPane.YES_NO_CANCEL_OPTION, // Option type
					JOptionPane.QUESTION_MESSAGE, // Message type
					null, // Icon
					commands, // List of commands
					commands[commands.length - 1]);

		}

		public void sendMessage(String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		public void getInfo(String prompt) {
			response = JOptionPane.showInputDialog(prompt);
		}

		public void run() {

			switch (this.type) {
			case COMMAND:
				getCommand(commands);
				break;
			case MESSAGE:
				sendMessage(message);
				break;
			case INFO:
				getInfo(prompt);
				break;

			}

		}
	}

	/**
	 * presents set of commands for user to choose one of
	 * 
	 * @param commands
	 *            the commands to choose from
	 * @return the index of the command in the array
	 */
	public int getCommand(String[] commands) {

		gui.commands = commands;
		gui.type = InnerGUI.TYPE.COMMAND;

		try {
			SwingUtilities.invokeAndWait(gui);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return gui.choice;
	}

	/**
	 * tell the user something
	 * 
	 * @param message
	 *            string to print out to the user
	 */
	public void sendMessage(String message) {

		gui.message = message;
		gui.type = InnerGUI.TYPE.MESSAGE;

		try {
			SwingUtilities.invokeAndWait(gui);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * prompts the user for a string
	 * 
	 * @param prompt
	 *            the request
	 * @return what the user enters, null if nothing
	 */
	public synchronized String getInfo(String prompt) {

		gui.prompt = prompt;
		gui.type = InnerGUI.TYPE.INFO;

		try {
			SwingUtilities.invokeAndWait(gui);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return gui.response;
	}
	
	public void setPrompt(String prompt) {
		gui.windowPrompt = prompt;
	}


}
