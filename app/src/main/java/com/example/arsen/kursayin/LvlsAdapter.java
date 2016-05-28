package com.example.arsen.kursayin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.arsen.kursayin.custom_views.SquareTextView;


/**
 * Created by arsen on 5/28/16.
 */
public class LvlsAdapter extends BaseAdapter {

	private int maxLvlsCount;
	private int maxOpenedLvl;
	private LayoutInflater inflater;

	public LvlsAdapter(Context context) {
		this.maxLvlsCount = GameSettings.getInstance().getMaxLvlsCount();
		this.maxOpenedLvl = GameSettings.getInstance().getMaxOpenedLvl();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return maxLvlsCount;
	}

	@Override
	public Integer getItem(int i) {
		return i + 1;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean isEnabled(int position) {
		return position+1<=maxOpenedLvl;
	}

	@Override
	public int getItemViewType(int position) {
		return position + 1 > maxOpenedLvl ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Integer lvl = getItem(position);
		SquareTextView textView;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_lvl, parent, false);
			textView = (SquareTextView) convertView.findViewById(R.id.item_lvl_text_view);
			convertView.setTag(textView);
		} else {
			textView = (SquareTextView) convertView.getTag();
		}
		textView.setText(lvl.toString());
		if (lvl > maxOpenedLvl) {
			textView.setBackgroundResource(R.color.lvl_grid_item_gray);
			textView.setClickable(false);
		}
		return convertView;
	}
}
