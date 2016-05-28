package com.example.arsen.kursayin;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by arsen on 5/28/16.
 */
public class LvlsAdapter extends BaseAdapter {

	private int maxLvlsCount;
	private int maxOpenedLvl;

	public LvlsAdapter(int maxLvlsCount, int maxOpenedLvl) {
		this.maxLvlsCount = maxLvlsCount;
		this.maxLvlsCount = maxOpenedLvl;
	}
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		return null;
	}
}
