package com.example.arsen.kursayin;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.arsen.kursayin.utils.Constants;

import java.util.ArrayList;

/**
 * Created by arsen on 5/28/16.
 */
public class GameSettings {
	private static int MAX_LVLS_COUNT = 10;
	private static int WIN_TIME_IN_SECONDS = 5;

	private static GameSettings instance = null;

	public static GameSettings getInstance() {
		if (instance == null) {
			instance = new GameSettings();

		}
		return instance;
	}


	private ArrayList<Integer> planetsCountInLvl;
	private int maxLvlsCount;
	private int maxOpenedLvl;
	private Context context;

	private GameSettings() {
		context = MyApplication.get().getApplicationContext();
		maxLvlsCount = MAX_LVLS_COUNT;
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.APP_SHARED_PREFS, context.MODE_PRIVATE);
		maxOpenedLvl = sharedPref.getInt(Constants.MAX_OPENED_LVL_PREF, 1);
		initLvls(maxLvlsCount);
	}

	private void initLvls(int maxLvlsCount) {
		planetsCountInLvl = new ArrayList<>(maxLvlsCount);
		/*0*/planetsCountInLvl.add(0);
		/*1*/planetsCountInLvl.add(2);
		/*2*/planetsCountInLvl.add(3);
		/*3*/planetsCountInLvl.add(4);
		/*4*/planetsCountInLvl.add(5);
		/*5*/planetsCountInLvl.add(6);
		/*6*/planetsCountInLvl.add(7);
		/*7*/planetsCountInLvl.add(8);
		/*8*/planetsCountInLvl.add(9);
		/*9*/planetsCountInLvl.add(10);
	}

	public int getPlanetsCountInLvl(int lvl) {
		return planetsCountInLvl.get(lvl);
	}

	public void winLvl(int lvl) {
		if(lvl == maxOpenedLvl) {
			SharedPreferences sharedPref = context.getSharedPreferences(Constants.APP_SHARED_PREFS, context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(Constants.MAX_OPENED_LVL_PREF, maxOpenedLvl + 1);
			maxOpenedLvl++;
			editor.commit();
		}
	}


	public int getWinTimeInSeconds() {
		return WIN_TIME_IN_SECONDS;
	}

	public int getMaxLvlsCount() {
		return maxLvlsCount;
	}

	public int getMaxOpenedLvl() {
		return maxOpenedLvl;
	}
}
