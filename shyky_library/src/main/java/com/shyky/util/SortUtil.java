package com.shyky.util;

/**
 * 排序操作工具类
 * 
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/7/9
 * @since 1.0
 */
public final class SortUtil {
	/**
	 * 构造方法私有化
	 */
	private SortUtil() {

	}

	public static void swop(int number1, int number2) {
		int temp = number1;
		number1 = number2;
		number2 = temp;
	}

	/**
	 * 冒泡排序
	 * 
	 * @param array
	 *            要用作排序的整数数组
	 */
	public static void bubble(int[] array) {
		if (ObjectUtil.notNull(array)) {
			for (int j = 0; j < array.length; j++) {
				for (int k = j + 1; k < array.length; k++) {
					if (array[j] > array[k]) {
						int temp = array[j];
						array[j] = array[k];
						array[k] = temp;
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序
	 * 
	 * @param array
	 *            要用作排序的整数数组
	 */
	public static void bubble(float[] array) {
		if (ObjectUtil.notNull(array)) {
			for (int j = 0; j < array.length; j++) {
				for (int k = j + 1; k < array.length; k++) {
					if (array[j] > array[k]) {
						float temp = array[j];
						array[j] = array[k];
						array[k] = temp;
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序
	 * 
	 * @param array
	 *            要用作排序的整数数组
	 */
	public static void bubble(double[] array) {
		if (ObjectUtil.notNull(array)) {
			for (int j = 0; j < array.length; j++) {
				for (int k = j + 1; k < array.length; k++) {
					if (array[j] > array[k]) {
						double temp = array[j];
						array[j] = array[k];
						array[k] = temp;
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		int size = 1000000;
		int[] a = new int[size];
		for (int j = 0; j < size; j++)
			a[j] = RandomUtil.getRandomInt(100);
		System.out.println(a.length);
		long start = System.currentTimeMillis();
		bubble(a);
		long end = System.currentTimeMillis();
		System.out.println("用时：" + (end - start));
		System.out.println(ListUtil.string(a));
	}
}