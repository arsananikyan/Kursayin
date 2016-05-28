package com.example.arsen.kursayin.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.arsen.kursayin.activities.PlayActivity;

/**
 * Created by arsen on 5/28/16.
 */
public class Utils {
	public static void startPlayingLvl(Activity activity, int lvl) {
		Intent playActivityIntent = new Intent(activity, PlayActivity.class);
		playActivityIntent.putExtra(Constants.EXTRA_PLAYING_LVL, lvl);
		activity.startActivity(playActivityIntent);
		activity.finish();
	}

	public static void openLvlChooser(Activity activity) {
		Intent chooserActivityIntent = new Intent(activity, PlayActivity.class);
		activity.startActivity(chooserActivityIntent);
	}

	public static int getMaxOpenedLvl(Activity activity) {
		SharedPreferences sharedPref = activity.getPreferences(activity.MODE_PRIVATE);
		int maxOpenedLvl = sharedPref.getInt(Constants.MAX_OPENED_LVL_PREF,1);
		return maxOpenedLvl;
	}
}
