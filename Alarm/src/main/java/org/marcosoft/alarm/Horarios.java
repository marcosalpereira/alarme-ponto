package org.marcosoft.alarm;

import java.util.Observable;

import org.marcosoft.util.HoraUtils;

public class Horarios extends Observable {
	public enum Turnos {
		Primeiro, Segundo, PrimeiroExtra, SegundoExtra
	}

	public enum TipoHora {
		Entrada, Saida
	}

	private String[] horarios = new String[Turnos.values().length * TipoHora.values().length];
	private int[] horariosEmMinutos = new int[Turnos.values().length * TipoHora.values().length];

	public int getEntrada(Turnos turno) {
		return getMinutos(turno, TipoHora.Entrada);
	}

	private int getMinutos(Turnos turno, TipoHora tipoHora) {
		return horariosEmMinutos[turno.ordinal() * 2 + tipoHora.ordinal()];
	}

	private void setMinutos(Turnos turno, TipoHora tipoHora, int min) {
		horariosEmMinutos[turno.ordinal() * 2 + tipoHora.ordinal()] = min;
		horarios[turno.ordinal() * 2 + tipoHora.ordinal()] = HoraUtils.formatHoras(min);
	}

	private void setHora(Turnos turno, TipoHora tipoHora, String hora) {
		horariosEmMinutos[turno.ordinal() * 2 + tipoHora.ordinal()] = HoraUtils.parseMinutos(hora);
		horarios[turno.ordinal() * 2 + tipoHora.ordinal()] = hora;
	}

	private String getHora(Turnos turno, TipoHora tipoHora) {
		return horarios[turno.ordinal() * 2 + tipoHora.ordinal()];
	}

	public int getSaida(Turnos turno) {
		return getMinutos(turno, TipoHora.Saida);
	}

	public void setSaida(Turnos turno, int min) {
		setMinutos(turno, TipoHora.Saida, min);
	}

	public void setEntrada(Turnos turno, int min) {
		setMinutos(turno, TipoHora.Entrada, min);
	}

	public void setPrimeiroTurnoEntrada(String primeiroTurnoEntrada) {
		setHora(Turnos.Primeiro, TipoHora.Entrada, primeiroTurnoEntrada);
	}

	public void setPrimeiroTurnoSaida(String primeiroTurnoSaida) {
		setHora(Turnos.Primeiro, TipoHora.Saida, primeiroTurnoSaida);
	}

	public void setSegundoTurnoEntrada(String segundoTurnoEntrada) {
		setHora(Turnos.Segundo, TipoHora.Entrada, segundoTurnoEntrada);
	}

	public void setSegundoTurnoSaida(String segundoTurnoSaida) {
		setHora(Turnos.Segundo, TipoHora.Saida, segundoTurnoSaida);
	}

	public void setPrimeiroTurnoExtraEntrada(String primeiroTurnoExtraEntrada) {
		setHora(Turnos.PrimeiroExtra, TipoHora.Entrada, primeiroTurnoExtraEntrada);
	}

	public void setPrimeiroTurnoExtraSaida(String primeiroTurnoExtraSaida) {
		setHora(Turnos.PrimeiroExtra, TipoHora.Saida, primeiroTurnoExtraSaida);
	}

	public void setSegundoTurnoExtraEntrada(String segundoTurnoExtraEntrada) {
		setHora(Turnos.SegundoExtra, TipoHora.Entrada, segundoTurnoExtraEntrada);
	}

	public void setSegundoTurnoExtraSaida(String segundoTurnoExtraSaida) {
		setHora(Turnos.SegundoExtra, TipoHora.Saida, segundoTurnoExtraSaida);
	}

	public String getPrimeiroTurnoEntrada() {
		return getHora(Turnos.Primeiro, TipoHora.Entrada);
	}

	public String getPrimeiroTurnoSaida() {
		return getHora(Turnos.Primeiro, TipoHora.Saida);
	}

	public String getSegundoTurnoEntrada() {
		return getHora(Turnos.Segundo, TipoHora.Entrada);
	}

	public String getSegundoTurnoSaida() {
		return getHora(Turnos.Segundo, TipoHora.Saida);
	}

	public String getPrimeiroTurnoExtraEntrada() {
		return getHora(Turnos.PrimeiroExtra, TipoHora.Entrada);
	}

	public String getPrimeiroTurnoExtraSaida() {
		return getHora(Turnos.PrimeiroExtra, TipoHora.Saida);
	}

	public String getSegundoTurnoExtraEntrada() {
		return getHora(Turnos.SegundoExtra, TipoHora.Entrada);
	}

	public String getSegundoTurnoExtraSaida() {
		return getHora(Turnos.SegundoExtra, TipoHora.Saida);
	}

	public int getProximoAlarme(int minCorrente) {
		for (int min : horariosEmMinutos) {
			if (min >= minCorrente) {
				return min;
			}
		}
		return -1;
	}

	public String[] asArray() {
		return horarios;
	}

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	public int getDuracaoPrimeiroTurno() {
		return getSaida(Turnos.Primeiro) - getEntrada(Turnos.Primeiro);
	}

	public int getDuracaoSegundoTurno() {
		return getSaida(Turnos.Segundo) - getEntrada(Turnos.Segundo);
	}

	public int getDuracaoPrimeiroTurnoExtra() {
		return getSaida(Turnos.PrimeiroExtra) - getEntrada(Turnos.PrimeiroExtra);
	}

	public int getDuracaoSegundoTurnoExtra() {
		return getSaida(Turnos.SegundoExtra) - getEntrada(Turnos.SegundoExtra);
	}

}
