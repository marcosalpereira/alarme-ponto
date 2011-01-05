package org.marcosoft.alarm;

import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * Soar beep.
 */
public class Beeper {

	private BeepConfig beepConfig;
	
	private boolean toStop;

	public Beeper(BeepConfig beepConfig) {
		this.beepConfig = beepConfig;
	}
	
	public void stop() {
		this.toStop = true;
	}

	public void beep() {
		toStop = false;
		for (int countBeep = 0; countBeep < beepConfig.getTimes(); countBeep++) {
			if (!beepConfig.isMute()) {
				try {
					execute(beepConfig.getCommand());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"I can't beep! " + e.getMessage());
					break;
				}
			}
			sleep(beepConfig.getPause());
			if (toStop) break;
		}
	}

	private void sleep(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {

		}
	}

	/**
	 * Executa o comando e espera pelo termino do processo.
	 * @param command comando
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void execute(String command) throws IOException,
			InterruptedException {
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
	}
}
