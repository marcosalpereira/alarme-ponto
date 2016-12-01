package org.marcosoft.alarm;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.marcosoft.util.HoraUtils;

/**
 * Continuamente checa se é hora de alarmar.
 */
public class TimeChecker extends Observable implements Runnable {
	private final Map<String, Boolean> alarmados = new HashMap<String, Boolean>();
	private final Horarios horarios;

	public TimeChecker(Horarios horarios) {
		this.horarios = horarios;
	}

	public void resetAlarmados() {
		/* 21 */ this.alarmados.clear();
	}

	public void run() {
		for (;;) {
			final int minutosCorrente = HoraUtils.getHoraCorrenteEmMinutos();
			final int segundosCorrente = HoraUtils.getHoraCorrenteEmSegundos();
			final int proximoAlarme = horarios.getProximoAlarme(minutosCorrente);
			if (proximoAlarme > 0) {
				if (proximoAlarme == minutosCorrente) {
					if (isJaAlarmado(proximoAlarme)) {
						registrarAlarme(proximoAlarme);
						notifyObservers(0);
					}
				} else {
					notifyObservers(proximoAlarme * 60 - segundosCorrente);
				}
			}
			sleep(1000);
		}
	}

	private void registrarAlarme(int proximoAlarme) {
		alarmados.put(getChaveAlarmados(proximoAlarme), true);
	}

	private String getChaveAlarmados(int proximoAlarme) {
		return HoraUtils.dataAtualAsString() + proximoAlarme;
	}

	private boolean isJaAlarmado(int proximoAlarme) {
		return alarmados.get(getChaveAlarmados(proximoAlarme)) == null;
	}

	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}

	private void sleep(int milis) {
		try {
			Thread.sleep(milis);
		} catch (final InterruptedException e) {

		}
	}

}