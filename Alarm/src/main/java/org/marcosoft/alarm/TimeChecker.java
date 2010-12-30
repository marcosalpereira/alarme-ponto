package org.marcosoft.alarm;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Continuamente checa se é hora de alarmar.
 */
public class TimeChecker extends Observable implements Runnable {
	private Map<String, Boolean> alarmados = new HashMap<String, Boolean>();
	private Horarios horarios;
	
	public TimeChecker(Horarios horarios) {
		this.horarios = horarios;
	}
	
	@Override
	public void run() {
		for (;;) {
			String hora = horarios.isHoraAlarme();
			if (hora != null && alarmados.get(hora) == null) {
				alarmados.put(hora, true);
				
				notifyObservers(hora);
			}
			sleep(1000);
		}			
	}
	
	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
	
	private void sleep(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {

		}
	}	

}