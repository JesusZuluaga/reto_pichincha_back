package com.reto.PruebaTecnica.domain.model.common;

public final class EnumUtils {

	private EnumUtils() {
	}

	public static boolean belongsToIgnoreCase(String value, Enum<?>[] enums) {
		boolean belongs = false;
		for (Enum<?> e : enums) {
			belongs = belongs || e.name().equalsIgnoreCase(value);
		}
		return belongs;
	}
}
