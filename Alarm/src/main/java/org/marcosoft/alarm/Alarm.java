package org.marcosoft.alarm;
import java.util.Observable;
import java.util.Observer;

import org.marcosoft.util.ApplicationProperties;
import org.marcosoft.util.PropertiesEditor;


public class Alarm implements Observer {
	private static final long serialVersionUID = -1045806326715430034L;

	public static final String PROPERTY_BEEPER_PAUSE = "beeper.pause";
	public static final String PROPERTY_BEEPER_TIMES = "beeper.times";
	public static final String PROPERTY_BEEPER_COMMAND = "beeper.command";
	
	public static final String DEFAULT_BEEPER_TIMES = "20";
	public static final String DEFAULT_BEEPER_PAUSE = "3000";
	private static final String DEFAULT_BEEPER_COMMAND = 
		"/usr/bin/beep -f 4000 -l 70 -n -f 3000 -l 70 -n -f 4000 -l 70 -n -f 3000 -l 70 -n -f 4000 -l 70 -n -f 3000 -l 70";
	
	private TimeChecker timeChecker;
	private Beeper beeper;

	private ApplicationProperties applicationProperties;

	private BeepConfig beepConfig;

	private Horarios horarios;

	private AlarmEditor alarmEditor;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		new Alarm();
	}
	
	public Alarm() {
		
		applicationProperties = new ApplicationProperties("alarme", System.getProperty("user.name"));
		applicationProperties.setDefault(PROPERTY_BEEPER_COMMAND, DEFAULT_BEEPER_COMMAND);
		applicationProperties.setDefault(PROPERTY_BEEPER_PAUSE, DEFAULT_BEEPER_PAUSE);
		applicationProperties.setDefault(PROPERTY_BEEPER_TIMES, DEFAULT_BEEPER_TIMES);
		
		beepConfig = carregarConfiguracoesBeep();
		horarios = carregarHorarios();
		
		horarios.addObserver(this);
		
		alarmEditor = new AlarmEditor(horarios);
		alarmEditor.addObserver(this);
		
		beeper = new Beeper(beepConfig);		

		timeChecker = new TimeChecker(horarios);
		timeChecker.addObserver(this);
		
		Thread thread = new Thread(timeChecker);		
		thread.setDaemon(true);
		thread.start();
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof TimeChecker) {
			Integer segundos = (Integer) arg;
			if (segundos == 0) {
				alarmEditor.mostrarMensagemAlarme();
				beeper.beep();
			} else {
				alarmEditor.mostrarProximoAlarme(segundos);
			}
			
		} else if (o instanceof AlarmEditor) {
			String cmd = (String) arg;
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
		}
	}

	private BeepConfig carregarConfiguracoesBeep() {
		String command = applicationProperties.getProperty(PROPERTY_BEEPER_COMMAND);
		int pause = applicationProperties.getIntProperty(PROPERTY_BEEPER_PAUSE);
		int times = applicationProperties.getIntProperty(PROPERTY_BEEPER_TIMES);
		return new BeepConfig(command, times, pause);
	}

	private Horarios carregarHorarios() {
		Horarios horarios = new Horarios();
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


