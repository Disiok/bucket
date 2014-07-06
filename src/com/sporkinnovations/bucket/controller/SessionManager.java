package com.sporkinnovations.bucket.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.parse.ParseUser;
import com.sporkinnovations.bucket.activity.LoginActivity;

public class SessionManager {
	private Activity mActivity;
	public SessionManager(Activity activity) {
		mActivity = activity;
	}
	public boolean isLoggedIn() {
		return ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getObjectId() != null;
	}
	
	public void checkLogin() {
		if (!isLoggedIn()) {
			Intent intent = new Intent(mActivity, LoginActivity.class);
			mActivity.startActivity(intent);
			mActivity.finish();
		}
	}
}
