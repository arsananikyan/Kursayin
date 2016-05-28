package com.example.arsen.kursayin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.arsen.kursayin.LvlsAdapter;
import com.example.arsen.kursayin.R;
import com.example.arsen.kursayin.utils.Utils;

public class LvlChooserActivity extends AppCompatActivity {

	private GridView lvlsGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lvl_chooser);
		lvlsGridView = (GridView) findViewById(R.id.lvls_gridview);
		LvlsAdapter lvlsAdapter = new LvlsAdapter(this);
		lvlsGridView.setAdapter(lvlsAdapter);
		lvlsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Utils.startPlayingLvl(LvlChooserActivity.this,i+1);
			}
		});
	}
}
