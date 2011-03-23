package org.marcosoft.util;

import java.util.Calendar;

public final class HoraUtils {
	
	public static int parseMinutos(String hora) {
		String[] split = hora.split(":");
		try {
			int h = Integer.parseInt(split[0]);
			int m = Integer.parseInt(split[1]);
			return h * 60 + m;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	
	public static String formatHorasSegundos(int segundos) {
		int h = segundos / 60 / 60;
		int m = (segundos - (h * 60 * 60)) / 60;
		int s = segundos - (h * 60 * 60) - (m * 60);
		String hora = String.format("%02d:%02d:%02d", h, m, s);
		return hora;
	}
	
	public static String formatHoras(int mins) {
		int h = mins / 60;
		int m = mins - (h * 60);
		String hora = String.format("%02d:%02d", h, m);
		return hora;
	}
	
	/**
	 * Retorna a hora corrente em minutos.
	 * @return a hora
	 */
	public static int getHoraCorrenteEmMinutos() {
		Calendar calendar = Calendar.getInstance();
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		return h * 60 + m;
	}
	
	/**
	 * Retorna a hora corrente em segundos.
	 * @return a hora
	 */
	public static int getHoraCorrenteEmSegundos() {
		Calendar calendar = Calendar.getInstance();
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		return h * 60 * 60 + m * 60 + s;
	}
}
