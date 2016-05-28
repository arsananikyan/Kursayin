package com.example.arsen.kursayin.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.arsen.kursayin.R;
import com.example.arsen.kursayin.custom_views.DrawingView;

public class PlayActivity extends AppCompatActivity {

	private View menu;
	private DrawingView drawingView;
	private TextView weightEditText;
	private View doneBtn;
	private View startBtn;
	private View pauseBtn;
	private DrawingView.Planet selectedPlanet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		init();

		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawingView.startMovie();
			}
		});

		pauseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawingView.pauseMovie();
			}
		});

		drawingView.setPlanetSelectedCallback(new DrawingView.PlanetSelectedCallback() {
			@Override
			public void onPlanetSelected(DrawingView.Planet planet) {
				menu.setVisibility(View.VISIBLE);
				selectedPlanet = planet;
			}
		});
		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedPlanet.weight = Long.parseLong(weightEditText.getText() + "");
				drawingView.invalidate();
				closeKeyboard();
				menu.setVisibility(View.GONE);
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
	}

	private void closeKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

}
