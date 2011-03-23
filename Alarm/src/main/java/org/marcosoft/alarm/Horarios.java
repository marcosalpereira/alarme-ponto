package org.marcosoft.alarm;

import java.util.Calendar;
import java.util.Observable;

import org.marcosoft.util.HoraUtils;

public class Horarios extends Observable {
	public enum Turnos { Primeiro, Segundo, PrimeiroExtra, SegundoExtra };
	
	private String primeiroTurnoEntrada;
	private String primeiroTurnoSaida;
	
	private String segundoTurnoEntrada;
	private String segundoTurnoSaida;
	
	private String primeiroTurnoExtraEntrada;
	private String primeiroTurnoExtraSaida;

	private String segundoTurnoExtraEntrada;
	private String segundoTurnoExtraSaida;
	
	public int getEntrada(Turnos turno) {
		switch (turno) {
		case Primeiro:
			return HoraUtils.parseMinutos(primeiroTurnoEntrada);

		case Segundo:			
			return HoraUtils.parseMinutos(segundoTurnoEntrada);
			
		case PrimeiroExtra:			
			return HoraUtils.parseMinutos(primeiroTurnoExtraEntrada);
			
		case SegundoExtra:			
			return HoraUtils.parseMinutos(segundoTurnoExtraEntrada);
		}
		return -1;		
	}
	
	public int getSaida(Turnos turno) {
		switch (turno) {
		case Primeiro:
			return HoraUtils.parseMinutos(primeiroTurnoSaida);
			
		case Segundo:			
			return HoraUtils.parseMinutos(segundoTurnoSaida);
			
		case PrimeiroExtra:			
			return HoraUtils.parseMinutos(primeiroTurnoExtraSaida);
			
		case SegundoExtra:			
			return HoraUtils.parseMinutos(segundoTurnoExtraSaida);
		}
		return -1;		
	}
	
	public void setSaida(Turnos turno, int min) {
		switch (turno) {
		case Primeiro:
			primeiroTurnoSaida = HoraUtils.formatHoras(min);
			break;
			
		case Segundo:			
			segundoTurnoSaida = HoraUtils.formatHoras(min);
			break;
			
		case PrimeiroExtra:			
			primeiroTurnoExtraSaida = HoraUtils.formatHoras(min);
			break;
			
		case SegundoExtra:			
			segundoTurnoExtraSaida = HoraUtils.formatHoras(min);
			break;
		}		
	}
	
	public void setEntrada(Turnos turno, int min) {
		switch (turno) {
		case Primeiro:
			primeiroTurnoEntrada = HoraUtils.formatHoras(min);
			break;
			
		case Segundo:			
			segundoTurnoEntrada = HoraUtils.formatHoras(min);
			break;
			
		case PrimeiroExtra:			
			primeiroTurnoExtraEntrada = HoraUtils.formatHoras(min);
			break;
			
		case SegundoExtra:			
			segundoTurnoExtraEntrada = HoraUtils.formatHoras(min);
			break;
		}		
	}	
	

	public void setPrimeiroTurnoEntrada(String primeiroTurnoEntrada) {
		this.primeiroTurnoEntrada = primeiroTurnoEntrada;
	}

	public void setPrimeiroTurnoSaida(String primeiroTurnoSaida) {
		this.primeiroTurnoSaida = primeiroTurnoSaida;
	}

	public void setSegundoTurnoEntrada(String segundoTurnoEntrada) {
		this.segundoTurnoEntrada = segundoTurnoEntrada;
	}

	public void setSegundoTurnoSaida(String segundoTurnoSaida) {
		this.segundoTurnoSaida = segundoTurnoSaida;
	}

	public void setPrimeiroTurnoExtraEntrada(String primeiroTurnoExtraEntrada) {
		this.primeiroTurnoExtraEntrada = primeiroTurnoExtraEntrada;
	}

	public void setPrimeiroTurnoExtraSaida(String primeiroTurnoExtraSaida) {
		this.primeiroTurnoExtraSaida = primeiroTurnoExtraSaida;
	}

	public void setSegundoTurnoExtraEntrada(String segundoTurnoExtraEntrada) {
		this.segundoTurnoExtraEntrada = segundoTurnoExtraEntrada;
	}

	public void setSegundoTurnoExtraSaida(String segundoTurnoExtraSaida) {
		this.segundoTurnoExtraSaida = segundoTurnoExtraSaida;
	}

	public String getPrimeiroTurnoEntrada() {
		return primeiroTurnoEntrada;
	}

	public String getPrimeiroTurnoSaida() {
		return primeiroTurnoSaida;
	}

	public String getSegundoTurnoEntrada() {
		return segundoTurnoEntrada;
	}

	public String getSegundoTurnoSaida() {
		return segundoTurnoSaida;
	}

	public String getPrimeiroTurnoExtraEntrada() {
		return primeiroTurnoExtraEntrada;
	}

	public String getPrimeiroTurnoExtraSaida() {
		return primeiroTurnoExtraSaida;
	}

	public String getSegundoTurnoExtraEntrada() {
		return segundoTurnoExtraEntrada;
	}

	public String getSegundoTurnoExtraSaida() {
		return segundoTurnoExtraSaida;
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
			segundoTurnoEntrada, segundoTurnoSaida, primeiroTurnoExtraEntrada,
			primeiroTurnoExtraSaida, segundoTurnoExtraEntrada,
			segundoTurnoExtraSaida };
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
	
	public int getDuracaoPrimeiroTurno() {
		return getSaida(Turnos.Primeiro)- getEntrada(Turnos.Primeiro);
	}
	
	public int getDuracaoSegundoTurno() {
		return getSaida(Turnos.Segundo)- getEntrada(Turnos.Segundo);
	}
	
	public int getDuracaoPrimeiroTurnoExtra() {
		return getSaida(Turnos.PrimeiroExtra)- getEntrada(Turnos.PrimeiroExtra);
	}
	
	public int getDuracaoSegundoTurnoExtra() {
		return getSaida(Turnos.SegundoExtra)- getEntrada(Turnos.SegundoExtra);
	}

	
}
