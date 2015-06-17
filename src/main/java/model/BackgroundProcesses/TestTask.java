package model.BackgroundProcesses;

import static java.lang.Thread.sleep;

/**
 * Created by jens on 6/17/15.
 */
public class TestTask implements Runnable {
	private static int k = 0;
	int i;

	public TestTask(int i) {
		this.i = k;
		k+=i;
	}

	@Override
	public void run() {
		for (int j = 0; j < 10; j++) {
			System.out.println(i + " " + j);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
