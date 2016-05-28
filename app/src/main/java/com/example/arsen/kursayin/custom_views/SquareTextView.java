package com.example.arsen.kursayin.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by arsen on 5/29/16.
 */
public class SquareTextView extends TextView {
	public SquareTextView(Context context) {
		super(context);
	}

	public SquareTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
