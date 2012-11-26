package org.marcosoft.util;

import java.util.Calendar;

public final class HoraUtils {

	public static int parseMinutos(String hora) {
		final String[] split = hora.split(":");
		try {
			final int h = Integer.parseInt(split[0]);
			final int m = Integer.parseInt(split[1]);
			return h * 60 + m;
		} catch (final NumberFormatException e) {
			return 0;
		}
	}


	public static String formatHorasSegundos(int segundos) {
		final int h = segundos / 60 / 60;
		final int m = (segundos - (h * 60 * 60)) / 60;
		final int s = segundos - (h * 60 * 60) - (m * 60);
		final String hora = String.format("%02d:%02d:%02d", h, m, s);
		return hora;
	}

	public static String formatHoras(int mins) {
		final int h = mins / 60;
		final int m = mins - (h * 60);
		final String hora = String.format("%02d:%02d", h, m);
		return hora;
	}

	/**
	 * Retorna a hora corrente em minutos.
	 * @return a hora
	 */
	public static int getHoraCorrenteEmMinutos() {
		final Calendar calendar = Calendar.getInstance();
		final int h = calendar.get(Calendar.HOUR_OF_DAY);
		final int m = calendar.get(Calendar.MINUTE);
		return h * 60 + m;
	}

	/**
	 * Retorna a hora corrente em segundos.
	 * @return a hora
	 */
	public static int getHoraCorrenteEmSegundos() {
		final Calendar calendar = Calendar.getInstance();
		final int h = calendar.get(Calendar.HOUR_OF_DAY);
		final int m = calendar.get(Calendar.MINUTE);
		final int s = calendar.get(Calendar.SECOND);
		return h * 60 * 60 + m * 60 + s;
	}


	public static String dataAtualAsString() {
		final Calendar calendar = Calendar.getInstance();
		final int dia = calendar.get(Calendar.DAY_OF_MONTH);
		final int ano = calendar.get(Calendar.YEAR);
		final int mes = calendar.get(Calendar.MONTH);
		final String data = String.format("%02d.%02d.%02d", dia, mes, ano);
		return data;
    }
}
