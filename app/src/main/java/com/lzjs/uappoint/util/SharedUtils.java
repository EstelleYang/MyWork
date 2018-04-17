/**
 * 存放用户配置类
 * zhangxw
 * 2015-12-08
 *
 */
package com.lzjs.uappoint.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

//实现标记的写入与读取
public class SharedUtils {
	private static final String FILE_NAME = "uappoint";
	private static final String MODE_NAME = "welcome";
	private static final String CHECU_UPDATE = "checkUpdate";//检查更新

	// 获取boolean类型的值
	public static boolean getWelcomeBoolean(Context context) {
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getBoolean(MODE_NAME, false);
	}

	// 写入Boolean类型的值
	public static void putWelcomeBoolean(Context context, boolean isFirst) {
		Editor editor = context.getSharedPreferences(FILE_NAME,
				Context.MODE_APPEND).edit();
		editor.putBoolean(MODE_NAME, isFirst);
		editor.commit();
	}

	// 是否检测更新
	public static boolean getCheckVersionUpdateBoolean(Context context){
		return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).getBoolean(CHECU_UPDATE,true);
	}

	// 更改检查更新的状态
	public static void putCheckVersionUpdateBoolean(Context context,boolean checkUpdate){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putBoolean(CHECU_UPDATE,checkUpdate);
		editor.commit();
	}

	// 写入一个String类型的数据
	public static void putCityName(Context context, String cityName) {

		Editor editor = context.getSharedPreferences(FILE_NAME,
				Context.MODE_APPEND).edit();
		editor.putString("cityName", cityName);
		editor.commit();
	}

	// 获取String类型的值
	public static String getCityName(Context context) {

		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getString("cityName", "兰州市");
	}

	/**
	 * 获取定位城市
	 * @param context
	 * @return
	 */
	public static String getLocationCityName(Context context){
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("locationCityName", "");
	}

	/**
	 *插入定位城市
	 * @param context
	 * @param cityName
	 * @return
	 */
	public static void putLocationCityName(Context context, String cityName){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putString("locationCityName", cityName);
		editor.commit();
	}

	public static void putLoginUserId(Context context, String userId){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putString("userId", userId);
		editor.commit();
	}

	public static String getLoginUserId(Context context){
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("userId", "");
	}

	public static void putInitChannel(Context context, boolean userId){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putBoolean("initChannel", userId);
		editor.commit();
	}

	public static boolean getInitChannel(Context context){
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean("initChannel", true);
	}
}