package com.changhong.tvos.dtv.tvap;

/**
 * @author
 *
 */
public class DtvPortInfoKeyTools {
	public static byte[] deskey = null;

	private static String teakey_o = "XrYuB4We"; //正式
//	private static String teakey_t = "12345678"; //测试

	private static int teano = 16;

	public static byte[] getDeskey() {
		return deskey;
	}

	public static String getTeakey() {
		return teakey_o;
		//	return teakey_t;
	}

	public static int getTeano() {
		return teano;
	}
}
