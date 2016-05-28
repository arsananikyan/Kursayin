package com.example.arsen.kursayin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.arsen.kursayin.R;
import com.example.arsen.kursayin.utils.Utils;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

	private int maxOpenedLvl;
	private View startBtn;
	private View continueBtn;
	private View chooseLvlBtn;
	private View btnsContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		initViews();
		maxOpenedLvl = Utils.getMaxOpenedLvl(this);
		if(maxOpenedLvl == 1) {
			btnsContainer.setVisibility(View.GONE);
		} else {
			startBtn.setVisibility(View.GONE);
		}
		startBtn.setOnClickListener(this);
		continueBtn.setOnClickListener(this);
		chooseLvlBtn.setOnClickListener(this);
	}

	private void initViews() {
		startBtn = findViewById(R.id.start_btn);
		continueBtn = findViewById(R.id.continue_btn);
		chooseLvlBtn = findViewById(R.id.choose_lvl_btn);
		btnsContainer = findViewById(R.id.home_btns_container);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.continue_btn || view.getId() == R.id.start_btn) {
			Utils.startPlayingLvl(this,maxOpenedLvl);
		} else if(view.getId() == R.id.choose_lvl_btn) {
			Utils.openLvlChooser(this);
		}
	}
}
