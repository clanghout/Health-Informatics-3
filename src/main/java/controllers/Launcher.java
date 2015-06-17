package controllers;

import javafx.application.Application;
import model.BackgroundProcesses.BackgroundProcessor;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Class that is started when the user starts up the program.
 */
public final class Launcher {

	private Launcher() { }

	/** 
	 * The main program.
	 */
	public static void main(String[] args) {
		BackgroundProcessor processor = new BackgroundProcessor(new ArrayBlockingQueue<Runnable>(1));

		Thread t = new Thread(processor);
		t.setDaemon(true);
		t.start();


		Application.launch(MainUI.class, args);

	}

}

