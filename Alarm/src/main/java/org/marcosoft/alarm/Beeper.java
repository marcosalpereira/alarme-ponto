package org.marcosoft.alarm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import org.marcosoft.lib.AudioUtils;
import org.marcosoft.lib.IOUtils;

/**
 * Soar beep.
 */
public class Beeper {

	private final BeepConfig beepConfig;

	private boolean toStop;

    private boolean beepSoundCardError;

    private ByteArrayInputStream alertBytes;

	public Beeper(BeepConfig beepConfig) {
		this.beepConfig = beepConfig;
	}

	public void stop() {
		this.toStop = true;
	}

	public void beep() {
	    beepSoundCardError = false;
		toStop = false;
		for (int countBeep = 0; countBeep < beepConfig.getTimes(); countBeep++) {
			if (!beepConfig.isMute()) {
				try {
				    beepSoundCard();
					execute(beepConfig.getCommand());
				} catch (final Exception e) {
					JOptionPane.showMessageDialog(null, "I can't beep! " + e.getMessage());
					break;
				}
			}
			sleep(beepConfig.getPause());
			if (toStop) {
				break;
			}
		}
	}

    private void beepSoundCard() {
        if (beepSoundCardError) {
            return;
        }
        try {
            if (alertBytes == null) {
                final InputStream stream = Beeper.class.getResourceAsStream("/alert.wav");
                alertBytes = new ByteArrayInputStream(IOUtils.readContent(stream));
            }
            alertBytes.reset();
            AudioUtils.playClip(alertBytes);
        } catch (final Exception e) {
            e.printStackTrace();
            beepSoundCardError = true;
        }
    }

	private void sleep(int milis) {
		try {
			Thread.sleep(milis);
		} catch (final InterruptedException e) {

		}
	}

	/**
	 * Executa o comando e espera pelo termino do processo.
	 *
	 * @param command
	 *            comando
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void execute(String command) throws IOException, InterruptedException {
		final Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
	}
}
