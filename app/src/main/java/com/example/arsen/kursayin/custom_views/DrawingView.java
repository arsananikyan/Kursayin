package com.example.arsen.kursayin.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.arsen.kursayin.GameSettings;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by arsen on 5/18/16.
 */
public class DrawingView extends View {

	private static final float PLANET_RADIUS = 30;
	private static final long TICK_TIME = 1000 / 60;
	private static final int WIN_TIME_IN_SECONDS = GameSettings.getInstance().getWinTimeInSeconds();

	private int width;
	private int height;
	private Paint planetPaint;
	private Paint vectorPaint;
	private ArrayList<Planet> planets;
	private Planet selectedPlanet;

	private PlanetSelectedCallback planetSelectedCallback;
	private GameEndListener gameEndListener;
	private TimerTickListener timerTickListener;

	private boolean isPlayingThreadStoped;
	private boolean isTimerThreadStoped;
	private Thread playingThread;
	private Thread timerThread;

	public DrawingMode drawingMode;
	private Handler handler;

	public DrawingView(Context context) {
		super(context);
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
	}

	private void init(AttributeSet attrs) {
		planets = new ArrayList<>();
		planetPaint = new Paint();

		vectorPaint = new Paint();
		vectorPaint.setStyle(Paint.Style.STROKE);
		vectorPaint.setStrokeWidth(5);
		vectorPaint.setColor(Color.RED);

		handler = new Handler();
		drawingMode = DrawingMode.DRAW_PLANET;
		initPlayingThread();
		initTimerThread();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Planet planet : planets) {
			planetPaint.setColor(planet.getColor());
			canvas.drawCircle(planet.x, planet.y, PLANET_RADIUS, planetPaint);
			if (drawingMode != DrawingMode.FREEZE) {
				canvas.drawPath(planet.vector.getPath(), vectorPaint);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		final int action = event.getActionMasked();
		if (drawingMode == DrawingMode.FREEZE)
			return true;
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (drawingMode == DrawingMode.DRAW_PLANET) {
					selectedPlanet = addPlanetAndReturn(event.getX(), event.getY());
					if (planetSelectedCallback != null) {
						drawingMode = DrawingMode.DRAW_SPEED_VECTOR;
						planetSelectedCallback.onPlanetSelected(selectedPlanet, true);
						invalidate();
						return true;
					}
				}
				if (drawingMode == DrawingMode.DRAW_SPEED_VECTOR && selectedPlanet != null) {
					selectedPlanet.vector.setEndCoordinates(event.getX(), event.getY());
					invalidate();
					return true;
				}
				if (drawingMode == DrawingMode.LET_MODIFY) {
					selectedPlanet = findPlanetIn(event.getX(), event.getY());
					if (selectedPlanet != null && planetSelectedCallback != null) {
						drawingMode = DrawingMode.DRAW_SPEED_VECTOR;
						planetSelectedCallback.onPlanetSelected(selectedPlanet, false);
						return true;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (drawingMode == DrawingMode.DRAW_SPEED_VECTOR || selectedPlanet != null) {
					selectedPlanet.vector.setEndCoordinates(event.getX(), event.getY());
					invalidate();
					return true;
				}
				break;
		}

		return true;
	}

	private void initTimerThread() {
		timerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= WIN_TIME_IN_SECONDS && !isTimerThreadStoped; i++) {
					try {
						Thread.sleep(1000);
						if(timerTickListener != null) {
							final int finalI = i;
							handler.post(new Runnable() {
								@Override
								public void run() {
									timerTickListener.onTick(finalI);
								}
							});
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(!isTimerThreadStoped) {
					isPlayingThreadStoped = true;
					isTimerThreadStoped = true;
					if (gameEndListener != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								gameEndListener.onSuccess();
							}
						});
					}
				}
			}
		});
	}

	private void initPlayingThread() {
		playingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isPlayingThreadStoped) {
					try {
						Thread.sleep(TICK_TIME);
						for (Planet curPlanet : planets) {
							if (checkIsOutOfBorders(curPlanet)) {
								stopPlayingWithLoose();
							}
							double sumAx = 0;
							double sumAy = 0;
							for (Planet planet : planets) {
								if (planet == curPlanet)
									continue;
								double dist = distanceBetweenPlanets(planet, curPlanet);
								if (dist < PLANET_RADIUS * 2) {
									stopPlayingWithLoose();
								}
								sumAx += planet.weight * (planet.x - curPlanet.x) / (dist * dist * dist);
								sumAy += planet.weight * (planet.y - curPlanet.y) / (dist * dist * dist);
							}
							curPlanet.newVx = curPlanet.vx + sumAx * TICK_TIME;
							curPlanet.newVy = curPlanet.vy + sumAy * TICK_TIME;

							curPlanet.newX = (float) (curPlanet.x + curPlanet.vx * TICK_TIME + sumAx * TICK_TIME * TICK_TIME / 2);
							curPlanet.newY = (float) (curPlanet.y + curPlanet.vy * TICK_TIME + sumAy * TICK_TIME * TICK_TIME / 2);

						}
						for (Planet planet : planets) {
							planet.applyNewValues();
						}
						handler.post(new Runnable() {
							@Override
							public void run() {
								invalidate();
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void startPlaying() {
		drawingMode = DrawingMode.FREEZE;
		setPlanetsSpeed();
		isPlayingThreadStoped = false;
		isTimerThreadStoped = false;
		playingThread.start();
		timerThread.start();
	}

	private void stopPlayingWithLoose() {
		isPlayingThreadStoped = true;
		isTimerThreadStoped = true;
		if (gameEndListener != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					gameEndListener.onFail();
				}
			});
		}
	}

	private boolean checkIsOutOfBorders(Planet planet) {
		float x = planet.x + PLANET_RADIUS;
		float y = planet.y + PLANET_RADIUS;
		return (x < 0 || x > width || y < 0 || y > height);
	}

	private void setPlanetsSpeed() {
		for (Planet planet : planets) {
			planet.vx = (planet.vector.x1 - planet.vector.x0) / 1000;
			planet.vy = (planet.vector.y1 - planet.vector.y0) / 1000;
		}
	}

	private double distanceBetweenPlanets(Planet p1, Planet p2) {
		return Math.hypot(p2.x - p1.x, p2.y - p1.y);
	}

	private Planet addPlanetAndReturn(float x, float y) {

		Planet newPlanet = new Planet(x, y);
		planets.add(newPlanet);
		return newPlanet;
	}

	private Planet findPlanetIn(float x, float y) {
		for (int i = planets.size() - 1; i >= 0; i--) {
			if (Math.hypot(planets.get(i).x - x, planets.get(i).y - y) < PLANET_RADIUS + 10) {
				return planets.get(i);
			}
		}
		return null;
	}

	public enum DrawingMode {
		DRAW_PLANET,
		DRAW_SPEED_VECTOR,
		LET_MODIFY,
		FREEZE
	}

	public static class Planet {
		private int color;
		private Random random;
		public float x;
		public float y;
		public float newX;
		public float newY;
		public double newVx;
		public double newVy;
		public double vx;
		public double vy;
		public long weight;
		public Vector vector;

		public Planet(float x, float y) {
			random = new Random();
			this.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
			this.x = x;
			this.y = y;
			this.weight = 0;
			this.vx = 0;
			this.vy = 0;
			vector = new Vector(x, y);
		}

		public void applyNewValues() {
			x = newX;
			y = newY;
			vx = newVx;
			vy = newVy;
		}

		public int getColor() {
			return color;
		}

	}

	public static class Vector {
		public float x0;
		public float y0;

		public float x1;
		public float y1;
		private Path path;

		public Vector(float x0, float y0) {
			this.x0 = x0;
			this.y0 = y0;
			path = new Path();
			x1 = 0;
			y1 = 0;
		}

		public void setEndCoordinates(float x1, float y1) {
			this.x1 = x1;
			this.y1 = y1;

			path = new Path();
			path.moveTo(x0, y0);
			path.lineTo(x1, y1);
			path.close();
		}

		public Path getPath() {
			return path;
		}
	}

	public void setPlanetSelectedCallback(PlanetSelectedCallback planetSelectedCallback) {
		this.planetSelectedCallback = planetSelectedCallback;
	}

	public void setGameEndListener(GameEndListener gameEndListener) {
		this.gameEndListener = gameEndListener;
	}

	public void setTimerTickListener(TimerTickListener timerTickListener) {
		this.timerTickListener = timerTickListener;
	}

	public interface PlanetSelectedCallback {
		void onPlanetSelected(Planet planet, boolean isNewPlanet);
	}

	public interface GameEndListener {
		void onSuccess();
		void onFail();
	}

	public interface TimerTickListener {
		void onTick(int timeInSeconds);
	}
}
