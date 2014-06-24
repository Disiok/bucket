package com.sporkinnovations.bucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Bucket {
	ArrayList<Wish> mWishes;

	public Bucket() {
		mWishes = new ArrayList<Wish>();
	}
	public ArrayList<Wish> getWishes() {
		return mWishes;
	}
	
	public void addWish(Wish wish) {
		mWishes.add(wish);
	}
	@SuppressWarnings("unchecked")
	public void sortWishes() {
		Collections.sort(mWishes);
	}
	public void load(final Context context, final BucketAdapter bucketAdapter) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Wish");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parseWishes, ParseException error) {
				if (error == null) {
					for (ParseObject parseWish: parseWishes) {
						mWishes.add(new Wish(parseWish.getString("message")));
					}
					bucketAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(context, "Loading failed", Toast.LENGTH_LONG).show();
				}
				
			}
			
		});
	}
}
