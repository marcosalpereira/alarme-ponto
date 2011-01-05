package org.marcosoft.alarm;

import java.util.Calendar;
import java.util.Observable;

public class Horarios extends Observable {
	
	private String primeiroTurnoEntrada;
	private String primeiroTurnoSaida;
	
	private String segundoTurnoEntrada;
	private String segundoTurnoSaida;
	
	private String turnoExtraEntrada;
	private String turnoExtraSaida;
	
	public String getPrimeiroTurnoEntrada() {
		return primeiroTurnoEntrada;
	}
	public void setPrimeiroTurnoEntrada(String primeiroTurnoEntrada) {
		this.primeiroTurnoEntrada = primeiroTurnoEntrada;
	}
	public String getPrimeiroTurnoSaida() {
		return primeiroTurnoSaida;
	}
	public void setPrimeiroTurnoSaida(String primeiroTurnoSaida) {
		this.primeiroTurnoSaida = primeiroTurnoSaida;
	}
	public String getSegundoTurnoEntrada() {
		return segundoTurnoEntrada;
	}
	public void setSegundoTurnoEntrada(String segundoTurnoEntrada) {
		this.segundoTurnoEntrada = segundoTurnoEntrada;
	}
	public String getSegundoTurnoSaida() {
		return segundoTurnoSaida;
	}
	public void setSegundoTurnoSaida(String segundoTurnoSaida) {
		this.segundoTurnoSaida = segundoTurnoSaida;
	}
	public String getTurnoExtraEntrada() {
		return turnoExtraEntrada;
	}
	public void setTurnoExtraEntrada(String turnoExtraEntrada) {
		this.turnoExtraEntrada = turnoExtraEntrada;
	}
	public String getTurnoExtraSaida() {
		return turnoExtraSaida;
	}
	public void setTurnoExtraSaida(String turnoExtraSaida) {
		this.turnoExtraSaida = turnoExtraSaida;
	}

	/**
	 * Verifica se é hora de alarmar.
	 * @return a hora de alarme ou <code>null</code>
	 */
	public String isHoraAlarme() {
		String[] horarios = asArray();		
		String hora = getHoraCorrente();
		for (int i = 0; i < horarios.length; i++) {
			if (hora.equals(horarios[i])) {
				return hora;
			}
		}
		return null;
	}
	
	public String[] asArray() {
		return new String[] { primeiroTurnoEntrada, primeiroTurnoSaida,
			segundoTurnoEntrada, segundoTurnoSaida, turnoExtraEntrada,
			turnoExtraSaida };
	}
	
	/**
	 * Retorna a hora corrente no formato hh:mm.
	 * @return a hora
	 */
	private String getHoraCorrente() {
		Calendar calendar = Calendar.getInstance();
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		String hora = String.format("%02d:%02d", h, m); 
		return hora;
	}
	
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	
	
}