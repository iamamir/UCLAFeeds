package com.netsole;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	
	private static final String CURRENT_USER_NAME = "curent_user_name";
	private static final String USER_SPLASH_SCREEN_PREFRENCE = "user_splash_screen_prefrence";
//	private SharedPreferences appSharedPrefs;
//	private Editor prefsEditor;
//
//	public AppPreferences(Activity context) {
//		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
//				Activity.MODE_PRIVATE);
//		this.prefsEditor = appSharedPrefs.edit();
//	}
//
//	public String getUserToken() {
//		return appSharedPrefs.getString("token", "");
//	}
//
//	public void setUserToken(String text) {
//		prefsEditor.putString("token", text);
//		prefsEditor.commit();
//	}
//
//	public String getCurrentZipCode() {
//		return appSharedPrefs.getString("zipcode", "");
//	}
//
//	public void setCurrentZipCode(String text) {
//		prefsEditor.putString("zipcode", text);
//		prefsEditor.commit();
//	}
	
	static String PREFS_NAME = "pref";

	public String getSharedPrefValue(Context mContext, String sharedPrefTitle)
	{
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(sharedPrefTitle, null);
	}

	public void saveSharedPrefValue(Context mContext, String sharedPrefTitle, String sharedPrefValue)
	{
		try
		{
			SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
			final Editor editor = settings.edit();
			editor.putString(sharedPrefTitle, sharedPrefValue);
			editor.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void removeSharedPrefValue(Context mContext, String sharedPrefTitle)
	{
		try
		{
			SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
			Editor editor = settings.edit();
			editor.remove(sharedPrefTitle);
			editor.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clearSharedPref(Context mContext)
	{
		try
		{
			SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
			Editor editor = settings.edit();
			editor.clear();
			editor.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	//Editted by Wajeeha
	
	public void setUserValue(Context mContext, String sharedPrefValue)
	{
		saveSharedPrefValue(mContext, CURRENT_USER_NAME, sharedPrefValue);
		
	}
	public void setSplashScreenValue(Context mContext, String sharedPrefValue)
	{
		saveSharedPrefValue(mContext, USER_SPLASH_SCREEN_PREFRENCE, sharedPrefValue);
		
	}
	public String getUserValue(Context mContext)
	{
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(CURRENT_USER_NAME, "Guest");
	
	}
	public String getUserSplashScreenPrefrence(Context mContext)
	{
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(USER_SPLASH_SCREEN_PREFRENCE, "false");
	
	}
	public void getRemoveUserValues(Context mContext)
	{
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.clear();
		editor.commit();
		
	
	}
}
