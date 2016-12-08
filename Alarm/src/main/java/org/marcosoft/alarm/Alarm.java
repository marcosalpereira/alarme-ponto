package org.marcosoft.alarm;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import org.marcosoft.lib.App;
import org.marcosoft.lib.ApplicationProperties;
import org.marcosoft.lib.Exec;
import org.marcosoft.lib.PropertiesEditor;
import org.marcosoft.lib.SoftwareUpdate;

public class Alarm extends App implements Observer {

	public static final String PROPERTY_BEEPER_PAUSE = "beeper.pause";
	public static final String PROPERTY_BEEPER_TIMES = "beeper.times";
	public static final String PROPERTY_BEEPER_COMMAND = "beeper.command";

	public static final String DEFAULT_BEEPER_TIMES = "20";
	public static final String DEFAULT_BEEPER_PAUSE = "3000";
	private static final String DEFAULT_BEEPER_COMMAND = "/usr/bin/beep -f 4000 -l 70 -n -f 3000 -l 70 -n -f 4000 -l 70 -n -f 3000 -l 70 -n -f 4000 -l 70 -n -f 3000 -l 70";

	private final TimeChecker timeChecker;
	private final Beeper beeper;

	private final ApplicationProperties applicationProperties;

	private final BeepConfig beepConfig;

	private final Horarios horarios;

	private final AlarmEditor alarmEditor;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		new Alarm(args).main();
	}

	public Alarm(String[] args) {
		super(args, "Alarme Ponto");
		SoftwareUpdate.update(this);

		applicationProperties = getApplicationProperties();
		applicationProperties.setDefault(PROPERTY_BEEPER_COMMAND, DEFAULT_BEEPER_COMMAND);
		applicationProperties.setDefault(PROPERTY_BEEPER_PAUSE, DEFAULT_BEEPER_PAUSE);
		applicationProperties.setDefault(PROPERTY_BEEPER_TIMES, DEFAULT_BEEPER_TIMES);

		beepConfig = carregarConfiguracoesBeep();
		horarios = carregarHorarios();

		horarios.addObserver(this);

		alarmEditor = new AlarmEditor(this, horarios);
		alarmEditor.addObserver(this);

		beeper = new Beeper(beepConfig);

		timeChecker = new TimeChecker(horarios);
		timeChecker.addObserver(this);

	}

	private void main() {
		final Thread thread = new Thread(timeChecker);
		thread.setDaemon(true);
		thread.start();
	}

	public void update(Observable o, Object arg) {
		if (o instanceof TimeChecker) {
			final Integer segundos = (Integer) arg;
			if (segundos == 0) {
				alarmEditor.mostrarMensagemAlarme();
				openSiscop();
				beeper.beep();
			} else {
				alarmEditor.mostrarProximoAlarme(segundos);
			}

		} else if (o instanceof AlarmEditor) {
			final String cmd = (String) arg;
			if ("OK".equals(cmd)) {
				beeper.stop();

			} else if ("MUTE_ON".equals(cmd)) {
				beepConfig.setMute(true);

			} else if ("MUTE_OFF".equals(cmd)) {
				beepConfig.setMute(false);

			} else if ("OPCOES".equals(cmd)) {
				new PropertiesEditor(applicationProperties, new Validator(), PROPERTY_BEEPER_COMMAND,
						PROPERTY_BEEPER_PAUSE, PROPERTY_BEEPER_TIMES);
			}

		} else if (o instanceof Horarios) {
			salvarHorarios();
			timeChecker.resetAlarmados();
		}
	}

	private void openSiscop() {
		final String urlSiscop = System.getProperty("alarm.url.siscop", "http://siscop.portalcorporativo.serpro");
		final String browser = System.getProperty("alarm.browser", "firefox");
		Exec.exe(browser, Arrays.asList(urlSiscop));
	}

	private BeepConfig carregarConfiguracoesBeep() {
		final String command = applicationProperties.getProperty(PROPERTY_BEEPER_COMMAND);
		final int pause = applicationProperties.getIntProperty(PROPERTY_BEEPER_PAUSE);
		final int times = applicationProperties.getIntProperty(PROPERTY_BEEPER_TIMES);
		return new BeepConfig(command, times, pause);
	}

	private Horarios carregarHorarios() {
		final Horarios horarios = new Horarios();
		horarios.setPrimeiroTurnoEntrada(applicationProperties.getProperty("primeiroTurnoEntrada", "00:00"));
		horarios.setPrimeiroTurnoSaida(applicationProperties.getProperty("primeiroTurnoSaida", "00:00"));

		horarios.setSegundoTurnoEntrada(applicationProperties.getProperty("segundoTurnoEntrada", "00:00"));
		horarios.setSegundoTurnoSaida(applicationProperties.getProperty("segundoTurnoSaida", "00:00"));

		horarios.setPrimeiroTurnoExtraEntrada(applicationProperties.getProperty("turnoExtraEntrada", "00:00"));
		horarios.setPrimeiroTurnoExtraSaida(applicationProperties.getProperty("turnoExtraSaida", "00:00"));

		horarios.setSegundoTurnoExtraEntrada(applicationProperties.getProperty("segundoTurnoExtraEntrada", "00:00"));
		horarios.setSegundoTurnoExtraSaida(applicationProperties.getProperty("segundoTurnoExtraSaida", "00:00"));

		return horarios;
	}

	private void salvarHorarios() {
		applicationProperties.setProperty("primeiroTurnoEntrada", horarios.getPrimeiroTurnoEntrada());
		applicationProperties.setProperty("primeiroTurnoSaida", horarios.getPrimeiroTurnoSaida());

		applicationProperties.setProperty("segundoTurnoEntrada", horarios.getSegundoTurnoEntrada());
		applicationProperties.setProperty("segundoTurnoSaida", horarios.getSegundoTurnoSaida());

		applicationProperties.setProperty("turnoExtraEntrada", horarios.getPrimeiroTurnoExtraEntrada());
		applicationProperties.setProperty("turnoExtraSaida", horarios.getPrimeiroTurnoExtraSaida());

		applicationProperties.setProperty("segundoTurnoExtraEntrada", horarios.getSegundoTurnoExtraEntrada());
		applicationProperties.setProperty("segundoTurnoExtraSaida", horarios.getSegundoTurnoExtraSaida());
	}

}
