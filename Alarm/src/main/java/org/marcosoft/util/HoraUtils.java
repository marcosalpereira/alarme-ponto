package org.marcosoft.util;

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
	
	
	public static String formatHoras(int mins) {
		int h = mins / 60;
		int m = mins - (h * 60);
		String hora = String.format("%02d:%02d", h, m);
		return hora;
	}
}
