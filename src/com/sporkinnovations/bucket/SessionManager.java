package com.sporkinnovations.bucket;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseUser;

public class SessionManager {
	private Context mContext;
	public SessionManager(Context context) {
		mContext = context;
	}
	public boolean isLoggedIn() {
		return ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getObjectId() != null;
	}
	
	public void checkLogin() {
		if (!isLoggedIn()) {
			Intent i = new Intent(mContext, LoginActivity.class);
			mContext.startActivity(i);
		}
	}
}
