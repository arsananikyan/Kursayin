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
import com.example.arsen.kursayin.utils.Utils;

public class PlayActivity extends AppCompatActivity {

	private DrawingView drawingView;

	private View menu;
	private TextView weightEditText;
	private View doneBtn;

	private View replayBtn;
	private View startBtn;

	private View doneContainer;
	private TextView doneMessageTextView;
	private TextView doneMessageBtn;

	private TextView timerTextView;
	private TextView lvlInformation;

	private int currentLvl;
	private int neededPlanetsCount;

	private DrawingView.Planet selectedPlanet;
	private int addedPlanetsCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		initViews();
		currentLvl = getIntent().getIntExtra(Constants.EXTRA_PLAYING_LVL, 1);
		neededPlanetsCount = GameSettings.getInstance().getPlanetsCountInLvl(currentLvl);
		lvlInformation.setText("LVL:" + currentLvl + "\nPlanets:" + neededPlanetsCount);
		startBtn.setEnabled(false);
		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startBtn.setEnabled(false);
				drawingView.startPlaying();
			}
		});
		replayBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replay();
			}
		});

		initDrawingViewListeners();

		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String weightText = weightEditText.getText().toString();
				if ("".equals(weightText) || weightText.equals("0"))
					return;
				selectedPlanet.weight = Long.parseLong(weightEditText.getText() + "");
				drawingView.invalidate();
				closeKeyboard();
				menu.setVisibility(View.GONE);
				if(addedPlanetsCount < neededPlanetsCount) {
					drawingView.drawingMode = DrawingView.DrawingMode.DRAW_PLANET;
				} else {
					drawingView.drawingMode = DrawingView.DrawingMode.LET_MODIFY;
					startBtn.setEnabled(true);
				}
			}
		});

	}

	private void initDrawingViewListeners() {
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

		drawingView.setGameEndListener(new DrawingView.GameEndListener() {
			@Override
			public void onSuccess() {
				doneMessageBtn.setText(R.string.success_btn_text);
				doneMessageTextView.setText(R.string.success_message);
				doneContainer.setVisibility(View.VISIBLE);
				GameSettings.getInstance().winLvl(currentLvl);
				doneMessageBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Utils.startPlayingLvl(PlayActivity.this,currentLvl+1);
						finish();
					}
				});
			}

			@Override
			public void onFail() {
				doneMessageBtn.setText(R.string.fail_btn_text);
				doneMessageTextView.setText(R.string.fail_message);
				doneContainer.setVisibility(View.VISIBLE);
				doneMessageBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						replay();
					}
				});
			}
		});

		drawingView.setTimerTickListener(new DrawingView.TimerTickListener() {
			@Override
			public void onTick(int time) {
				timerTextView.setText(time+"");
			}
		});
	}

	private void initViews() {
		menu = findViewById(R.id.menu);
		drawingView = (DrawingView) findViewById(R.id.drawingView);
		weightEditText = (TextView) findViewById(R.id.weight_edit_text);
		doneBtn = findViewById(R.id.done_btn);
		startBtn = findViewById(R.id.start_btn);
		replayBtn = findViewById(R.id.replay_btn);
		lvlInformation = (TextView) findViewById(R.id.lvl_information);
		doneContainer = findViewById(R.id.done_message_container);
		doneMessageBtn = (TextView) findViewById(R.id.done_message_btn);
		doneMessageTextView = (TextView) findViewById(R.id.done_message_text_view);
		timerTextView = (TextView) findViewById(R.id.timer_text_view);
	}

	private void replay() {
		Utils.startPlayingLvl(this,currentLvl);
		finish();
	}

	private void closeKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Override
	public void onBackPressed() {
		Utils.openHomeScreen(this);
		finish();
	}
}
