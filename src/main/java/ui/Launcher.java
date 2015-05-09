package ui;

import javafx.application.Application;

/**
 * Class that is started when the user starts up the program.
 */
public final class Launcher {

	private Launcher() { }

	/** 
	 * The main program.
	 */
	public static void main(String[] args) {
	
		Application.launch(MainUI.class, args);
		
	}

}

