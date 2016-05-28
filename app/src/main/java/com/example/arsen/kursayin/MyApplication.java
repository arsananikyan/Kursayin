package com.example.arsen.kursayin;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by arsen on 5/28/16.
 */
public class MyApplication extends Application {
	private static MyApplication instance;
	public static MyApplication get() { return instance; }

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
