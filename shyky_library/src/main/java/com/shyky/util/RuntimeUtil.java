package com.shyky.util;

import java.io.IOException;

public final class RuntimeUtil {
	public static final int UNIT_B = 22;
	public static final int UNIT_KB = 33;
	public static final int UNIT_MB = 44;
	public static final int UNIT_GB = 55;
	public static final int UNIT_TB = 66;

	private RuntimeUtil() {

	}

	public static Runtime getRuntime() {
		return Runtime.getRuntime();
	}

	public static long freeMemory() {
		return getRuntime().freeMemory();
	}

	public static long totalMemory() {
		return getRuntime().totalMemory();
	}

	public static void execute(String command) {
		try {
			getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String calculate(long value, int unit) {
		switch (unit) {
		case UNIT_B:
			return value + " B";
		case UNIT_KB:
			return (value / 1024) + " KB";
		case UNIT_MB:
			return (value / 1024 / 1024) + " MB";
		case UNIT_GB:
			return (value / 1024 / 1024 / 1024) + " G";
		case UNIT_TB:
			return (value / 1024 / 1024 / 1024 / 1024) + " T";
		}
		return null;
	}

	public static void main(String[] args) {
		long free = freeMemory();
		long total = totalMemory();
		System.out.println(calculate(free, UNIT_B));
		System.out.println(calculate(free, UNIT_KB));
		System.out.println(calculate(free, UNIT_MB));
		System.out.println(calculate(free, UNIT_GB));
		System.out.println(calculate(free, UNIT_TB));
		System.out.println("total:");
		System.out.println(calculate(total, UNIT_B));
		System.out.println(calculate(total, UNIT_KB));
		System.out.println(calculate(total, UNIT_MB));
		System.out.println(calculate(total, UNIT_GB));
		System.out.println(calculate(total, UNIT_TB));
		execute("adb.exe");
	}
}