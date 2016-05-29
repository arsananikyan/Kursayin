package com.example.arsen.kursayin.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.arsen.kursayin.activities.HomeScreenActivity;
import com.example.arsen.kursayin.activities.LvlChooserActivity;
import com.example.arsen.kursayin.activities.PlayActivity;

/**
 * Created by arsen on 5/28/16.
 */
public class Utils {
	public static void startPlayingLvl(Activity activity, int lvl) {
		Intent playActivityIntent = new Intent(activity, PlayActivity.class);
		playActivityIntent.putExtra(Constants.EXTRA_PLAYING_LVL, lvl);
		activity.startActivity(playActivityIntent);
	}

	public static void openLvlChooser(Activity activity) {
		Intent chooserActivityIntent = new Intent(activity, LvlChooserActivity.class);
		activity.startActivity(chooserActivityIntent);
	}

	public static void openHomeScreen(Activity activity) {
		Intent homeScreenIntent = new Intent(activity, HomeScreenActivity.class);
		activity.startActivity(homeScreenIntent);
	}
}
