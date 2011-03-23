package org.marcosoft.alarm;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.marcosoft.util.HoraUtils;

/**
 * Continuamente checa se é hora de alarmar.
 */
public class TimeChecker extends Observable implements Runnable {
	private Map<Integer, Boolean> alarmados = new HashMap<Integer, Boolean>();
	private Horarios horarios;
	
	public TimeChecker(Horarios horarios) {
		this.horarios = horarios;
	}
	
	@Override
	public void run() {
		for (;;) {
			int minutosCorrente = HoraUtils.getHoraCorrenteEmMinutos();
			int segundosCorrente = HoraUtils.getHoraCorrenteEmSegundos();
			int proximoAlarme = horarios.getProximoAlarme(minutosCorrente);
			if (proximoAlarme > 0) {
				if (proximoAlarme == minutosCorrente) {
					if (alarmados.get(proximoAlarme) == null) {
						alarmados.put(proximoAlarme, true);				
						notifyObservers(0);
					}					
				} else {
					notifyObservers(proximoAlarme * 60 - segundosCorrente);
				}
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