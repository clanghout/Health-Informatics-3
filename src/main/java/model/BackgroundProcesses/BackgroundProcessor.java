package model.BackgroundProcesses;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * Thread that executes the background tasks.
 * Only one task is allowed at any time.
 */
public class BackgroundProcessor implements Runnable {
	volatile private static BlockingQueue<Runnable> queue;
	private Logger logger = Logger.getLogger("BackgroundProcessor");

	public BackgroundProcessor(BlockingQueue<Runnable> queue) {
		logger.info("Start process handler");
		this.queue = queue;
	}

	public static BlockingQueue getQueue() {
		if (queue == null) {
			throw new IllegalStateException("No background thread running!");
		}
		return queue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Runnable task = queue.take();
				new Thread(task).start();
			}
		} catch (InterruptedException ex) {
			logger.log(Level.SEVERE, "Background task failed: " + ex.toString());
		}
	}
}
