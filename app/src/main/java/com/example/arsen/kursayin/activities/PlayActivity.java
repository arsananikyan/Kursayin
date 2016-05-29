package com.example.arsen.kursayin.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.arsen.kursayin.GameSettings;
import com.example.arsen.kursayin.R;
import com.example.arsen.kursayin.custom_views.DrawingView;
import com.example.arsen.kursayin.utils.Constants;

public class PlayActivity extends AppCompatActivity {

	private View menu;
	private DrawingView drawingView;
	private TextView weightEditText;
	private View doneBtn;
	private View startBtn;
	private View pauseBtn;
	private int currentLvl;
	private int neededPlanetsCount;
	private TextView lvlInformation;
	private DrawingView.Planet selectedPlanet;
	private int addedPlanetsCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		init();
		currentLvl = getIntent().getIntExtra(Constants.EXTRA_PLAYING_LVL, 1);
		neededPlanetsCount = GameSettings.getInstance().getPlanetsCountInLvl(currentLvl);
		lvlInformation.setText("LVL:" + currentLvl + "\nPlanets:" + neededPlanetsCount);
		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawingView.startPlaying();
			}
		});

		drawingView.setPlanetSelectedCallback(new DrawingView.PlanetSelectedCallback() {
			@Override
			public void onPlanetSelected(DrawingView.Planet planet, boolean isNewPlanet) {
				if (isNewPlanet) {
					addedPlanetsCount++;
				}
				weightEditText.setText(Long.toString(planet.weight));
				menu.setVisibility(View.VISIBLE);
				selectedPlanet = planet;
			}
		});
		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String weightText = weightEditText.getText().toString();
				if ("".equals(weightText) || weightText.equals(0))
					return;
				selectedPlanet.weight = Long.parseLong(weightEditText.getText() + "");
				drawingView.invalidate();
				closeKeyboard();
				menu.setVisibility(View.GONE);
				if(addedPlanetsCount < neededPlanetsCount)
					drawingView.drawingMode = DrawingView.DrawingMode.DRAW_PLANET;
				else
					drawingView.drawingMode = DrawingView.DrawingMode.LET_MODIFY;
			}
		});
	}

	private void init() {
		menu = findViewById(R.id.menu);
		drawingView = (DrawingView) findViewById(R.id.drawingView);
		weightEditText = (TextView) findViewById(R.id.weight_edit_text);
		doneBtn = findViewById(R.id.done_btn);
		startBtn = findViewById(R.id.start_btn);
		pauseBtn = findViewById(R.id.pause_btn);
		lvlInformation = (TextView) findViewById(R.id.lvl_information);
	}

	private void closeKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

}
