package com.example.arsen.kursayin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.arsen.kursayin.R;

public class LvlChooserActivity extends AppCompatActivity {

	private GridView lvlsGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lvl_chooser);
		lvlsGridView = (GridView) findViewById(R.id.lvls_gridview);
	}
}
