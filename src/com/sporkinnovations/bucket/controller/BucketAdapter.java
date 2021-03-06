package com.sporkinnovations.bucket.controller;

import java.util.ArrayList;

import com.sporkinnovations.bucket.Constants;
import com.sporkinnovations.bucket.Flags;
import com.sporkinnovations.bucket.model.Wish;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BucketAdapter extends ArrayAdapter{

	public BucketAdapter(Context context, int resource, ArrayList<Wish> wishes) {
		super(context, resource, wishes);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
		Wish currentWish = (Wish) this.getItem(position);
		if (currentWish.hasVote()) {
			float saturation = currentWish.getTotalVoteValue();
			String displayText = currentWish.toString() + (Flags.DEBUG ? " : " + saturation : "");
			view.setText(displayText);
			
			float[] color = new float[] {0, saturation * Constants.SATURATION_FACTOR, Constants.VALUE_FACTOR};
			view.setBackgroundColor(Color.HSVToColor(color));
		}
		return view;
		
	}
	

}
