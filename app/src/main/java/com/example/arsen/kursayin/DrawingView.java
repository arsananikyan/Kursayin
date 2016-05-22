package com.example.arsen.kursayin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by arsen on 5/18/16.
 */
public class DrawingView extends View {

	private int width;
	private int height;
	private Paint paint;
	private ArrayList<Planet> planets;
	private Planet currentPlanet;
	private PlanetSelectedCallback planetSelectedCallback;
	private final static float PLANET_RADIUS = 30;
	private final static long TICK_TIME = 1000 / 60;
	private Thread thread;
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
		paint = new Paint();
		paint.setColor(Color.RED);
		handler = new Handler();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Planet planet : planets) {
			canvas.drawCircle(planet.x, planet.y, PLANET_RADIUS, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		final int action = event.getActionMasked();


		switch (action) {
			case MotionEvent.ACTION_DOWN:
				currentPlanet = new Planet(event.getX(), event.getY(), 0, 0, 0);
				planets.add(currentPlanet);
				if (planetSelectedCallback != null) {
					planetSelectedCallback.onPlanetSelected(currentPlanet);
				}
				invalidate();
				break;
			case MotionEvent.ACTION_POINTER_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:

				break;
			case MotionEvent.ACTION_UP:
				break;

			default:
				break;
		}

		invalidate();

		return true;
	}

	public void startMovie() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for ( ; ; ) {
					try {
						Thread.sleep(TICK_TIME);
						for (Planet curPlanet : planets) {
							double sumAx = 0;
							double sumAy = 0;
							for (Planet planet : planets) {
								if (planet == curPlanet)
									continue;
								sumAx += planet.weight * (planet.x - curPlanet.x) / pow3DistanceBetweenPlanets(planet, curPlanet);
								sumAy += planet.weight * (planet.y - curPlanet.y) / pow3DistanceBetweenPlanets(planet, curPlanet);
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
		thread.start();
	}

	public void pauseMovie() {
		thread.interrupt();
	}

	private double pow3DistanceBetweenPlanets(Planet p1, Planet p2) {
		double dist = Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2));
		return dist * dist * dist;
	}


	public static class Planet {
		public float x;
		public float y;
		public float newX;
		public float newY;
		public double newVx;
		public double newVy;
		public double vx;
		public double vy;
		public long weight;

		public Planet(float x, float y, long weight, double vx, double vy) {
			this.x = x;
			this.y = y;
			this.weight = weight;
			this.vx = vx;
			this.vy = vy;
		}

		public void applyNewValues() {
			x = newX;
			y = newY;
			vx = newVx;
			vy = newVy;
		}

	}

	public void setPlanetSelectedCallback(PlanetSelectedCallback planetSelectedCallback) {
		this.planetSelectedCallback = planetSelectedCallback;
	}

	public interface PlanetSelectedCallback {
		void onPlanetSelected(Planet planet);
	}
}
