package com.mei.utils;

import java.util.List;

import android.content.Context;

import com.mei.app.MApplication;

public class PreferenceHelper {
	private static final String APP_DATA_FILE = "APP_DATA_FILE";
	private static final String USER_DATA_FILE = "USER_DATA_FILE";
	private static final String SEARCH_URL_DATA_FILE = "SEARCH_URL_DATA_FILE";

	private static final String KEY_LOGIN= "KEY_LOGIN";
	private static final String KEY_SEARCH= "KEY_SEARCH";
	private static final String KEY_FIRSTIN_APP = "KEY_FIRSTIN_APP";
	private static final String KEY_SEARCH_URL = "SEARCH_URL";
	
	public static String getLoginInfo() {
		return CommonUtils.b(MApplication.mContext.getSharedPreferences(USER_DATA_FILE, Context.MODE_PRIVATE).getString(KEY_LOGIN, ""));
	}
	
	public static void setLoginInfo(String userInfoId) {
		MApplication.mContext.getSharedPreferences(USER_DATA_FILE, Context.MODE_PRIVATE).edit().putString(KEY_LOGIN, CommonUtils.a(userInfoId)).commit();
	}
	
	public static List<String> getSearchHistory() {
		return CommonUtils.String2SceneList(CommonUtils.b(MApplication.mContext.getSharedPreferences(USER_DATA_FILE, Context.MODE_PRIVATE).getString(KEY_SEARCH, "")));
	}
	
	public static void setSearchHistory(List<String> searchHistory) {
		MApplication.mContext.getSharedPreferences(USER_DATA_FILE, Context.MODE_PRIVATE).edit().putString(KEY_SEARCH, CommonUtils.a(CommonUtils.SceneList2String(searchHistory))).commit();
	}
	
	public static void clearUserData() {
		MApplication.mContext.getSharedPreferences(USER_DATA_FILE, Context.MODE_PRIVATE).edit().clear().commit();
	}

	public static boolean getFirstInApp() {
		return MApplication.mContext.getSharedPreferences(APP_DATA_FILE, Context.MODE_PRIVATE).getBoolean(KEY_FIRSTIN_APP, true);
	}
	
	public static void setFirstInApp(boolean isFirstIn) {
		MApplication.mContext.getSharedPreferences(APP_DATA_FILE, Context.MODE_PRIVATE).edit().putBoolean(KEY_FIRSTIN_APP, isFirstIn).commit();
	}

	public static String getSearchUrl() {
		return MApplication.mContext.getSharedPreferences(SEARCH_URL_DATA_FILE, Context.MODE_PRIVATE).getString(KEY_SEARCH_URL, "");
	}

	public static void setSearchUrl(String url) {
		MApplication.mContext.getSharedPreferences(SEARCH_URL_DATA_FILE, Context.MODE_PRIVATE).edit().putString(KEY_SEARCH_URL, url).commit();
	}
}
