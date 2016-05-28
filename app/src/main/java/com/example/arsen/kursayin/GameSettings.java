package com.example.arsen.kursayin;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.arsen.kursayin.utils.Constants;

/**
 * Created by arsen on 5/28/16.
 */
public class GameSettings {
	private static int MAX_LVLS_COUNT = 10;

	private static GameSettings instance = null;

	public static GameSettings getInstance() {
		if (instance == null) {
			instance = new GameSettings();
		}
		return instance;
	}


	private int maxLvlsCount;
	private int maxOpenedLvl;
	private Context context;

	private GameSettings() {
		context = MyApplication.get().getApplicationContext();
		maxLvlsCount = MAX_LVLS_COUNT;
		updateMaxOpenedLvl();
	}

	public void updateMaxOpenedLvl() {
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.APP_SHARED_PREFS, context.MODE_PRIVATE);
		maxOpenedLvl = sharedPref.getInt(Constants.MAX_OPENED_LVL_PREF, 1);
	}

	public int getMaxLvlsCount() {
		return maxLvlsCount;
	}

	public int getMaxOpenedLvl() {
		return maxOpenedLvl;
	}
}
