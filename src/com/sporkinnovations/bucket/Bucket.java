package com.sporkinnovations.bucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Bucket {
	ArrayList<Wish> mWishes;
	Context mContext;

	public Bucket(Context context) {
		mWishes = new ArrayList<Wish>();
		mContext = context;
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
		// Determine whether to retrieve from local database or cloud database
		if (!hasNetworkConnection()) {
			query.fromLocalDatastore();
		}
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parseWishes, ParseException error) {
				if (error == null) {
					for (ParseObject parseWish : parseWishes) {
						mWishes.add(new Wish(parseWish));
					}
					bucketAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(context, "Loading failed" + error.getMessage(), Toast.LENGTH_LONG)
							.show();
				}

			}

		});
	}

	public boolean hasNetworkConnection() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	public void remove(int index) {
		mWishes.remove(index);
	}
}
